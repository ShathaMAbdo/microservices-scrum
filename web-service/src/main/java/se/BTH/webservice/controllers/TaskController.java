package se.BTH.webservice.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import se.BTH.webservice.models.*;
import se.BTH.webservice.services.UserService;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/api/task")
public class TaskController {

    private static Logger log = LoggerFactory.getLogger(TaskController.class);

    @Autowired
    @LoadBalanced
    protected RestTemplate restTemplate;

    @Autowired
    private UserService userService;
    // Displaying the tasks list for custom sprint .
    @RequestMapping(value = "/tasks", method = RequestMethod.GET)
    public String getTasks(@RequestParam(value = "sprintid", required = true) String sprintid, Model model, Principal user) {
        log.info("Request to fetch all tasks for custom sprint from the mongo database");
        Sprint sprint=restTemplate.getForObject("http://SPRINTMANAG-SERVICE/getsprint/{sprintid}", Sprint.class);
        List<Task> task_list = sprint.getTasks();
        Boolean isAdmin=userService.isAdmin(user.getName());
        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("tasks", task_list);
        model.addAttribute("sprintid", sprintid);
        model.addAttribute("sprintname", sprint.getName());
        return "task";
    }

    // Opening the add new task form page.
    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String addTask(@RequestParam(value = "sprintid", required = true) String sprintid, Model model) {
        log.info("Request to open the new task form page");
        Sprint sprint=restTemplate.getForObject("http://SPRINTMANAG-SERVICE/getsprint/{sprintid}", Sprint.class);
        model.addAttribute("taskAttr", Task.builder().storyPoints(0).build());
        model.addAttribute("sprintid", sprintid);
        model.addAttribute("sprintname", sprint.getName());
        return "taskform";
    }

    // Opening the edit task form page.
    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String editTask(@RequestParam(value = "taskid", required = true) String id, @RequestParam(value = "sprintid", required = true) String sprintid, Model model) {
        log.info("Request to open the edit task form page");
        Sprint sprint=restTemplate.getForObject("http://SPRINTMANAG-SERVICE/getsprint/{sprintid}", Sprint.class);
        Task task=restTemplate.getForObject("http://SPRINTMANAG-SERVICE/gettask/{id}", Task.class);
        model.addAttribute("taskAttr", task);
        model.addAttribute("sprintid", sprintid);
        model.addAttribute("sprintname", sprint.getName());
        return "taskform";
    }

    // Deleting the specified task.
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public String delete(@RequestParam(value = "taskid", required = true) String id, @RequestParam(value = "sprintid", required = true) String sprintid, Model model) {
        Sprint sprint=restTemplate.getForObject("http://SPRINTMANAG-SERVICE/getsprint/{sprintid}", Sprint.class);
        Task task=restTemplate.getForObject("http://SPRINTMANAG-SERVICE/gettask/{id}", Task.class);
        List<Task> tasks = sprint.getTasks();
        tasks.remove(sprint.findTaskIndex(id));
        sprint.setTasks(tasks);
        restTemplate.postForObject("http://SPRINTMANAG-SERVICE/sprint",sprint, Sprint.class);
        for (SubTask temp : task.getSubTasks()) {
            String tempid=temp.getId();
            restTemplate.delete("http://SPRINTMANAG-SERVICE/deletesubtask/{tempid}");
        }
        restTemplate.delete("http://SPRINTMANAG-SERVICE/deletetask/{id}");
        return "redirect:/api/task/tasks?sprintid=" + sprintid;
    }

    // Adding a new task or updating an existing task.
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String save(@ModelAttribute("taskAttr") Task task, @RequestParam(value = "sprintid", required = true) String sprintid) {
        if (task.getId() != "") {
            String id=task.getId();
            Task temp=restTemplate.getForObject("http://SPRINTMANAG-SERVICE/gettask/{id}", Task.class);
            task.setSubTasks(temp.getSubTasks());
            restTemplate.postForObject("http://SPRINTMANAG-SERVICE/task",task, Task.class);
            Sprint sprint=restTemplate.getForObject("http://SPRINTMANAG-SERVICE/getsprint/{sprintid}", Sprint.class);
            List<Task> tasks = sprint.getTasks();
            int index=sprint.findTaskIndex(task.getId());
            tasks.remove(index);
            tasks.add(index, task);
            sprint.setTasks(tasks);
            restTemplate.postForObject("http://SPRINTMANAG-SERVICE/sprint",sprint, Sprint.class);
        }  else {
            Task task1 = Task.builder().name(task.getName()).storyPoints(task.getStoryPoints()).priority(task.getPriority())
                    .subTasks(new ArrayList<>()).build();
            restTemplate.postForObject("http://SPRINTMANAG-SERVICE/newtask",task1, Task.class);
            Sprint sprint=restTemplate.getForObject("http://SPRINTMANAG-SERVICE/getsprint/{sprintid}", Sprint.class);
            List<Task> tasks = sprint.getTasks();
            tasks.add(task1);
            sprint.setTasks(tasks);
            restTemplate.postForObject("http://SPRINTMANAG-SERVICE/sprint",sprint, Sprint.class);
        }

        return "redirect:/api/task/tasks?sprintid=" + sprintid;
    }
}

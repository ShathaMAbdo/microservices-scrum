package se.BTH.webservice.controllers;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import se.BTH.webservice.models.Sprint;
import se.BTH.webservice.models.Team;
import se.BTH.webservice.services.SprintService;
import se.BTH.webservice.services.UserService;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api/sprint")
public class SprintController {
    private static final Logger log = LoggerFactory.getLogger(SprintController.class);


    @Autowired
    private SprintService sprintService;

    @Autowired
    @LoadBalanced
    protected RestTemplate restTemplate;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/sprints", method = RequestMethod.GET)
    public String getsprints(Model model, Principal user) {
        log.info("Request to fetch all sprints from the mongo database");
        Boolean isAdmin = userService.isAdmin(user.getName());
        Sprint[] sprints = restTemplate.getForObject("http://SPRINTMANAG-SERVICE/sprints", Sprint[].class);
        List<Sprint> sprint_list = Arrays.asList(sprints);
        if (!isAdmin) {
            sprint_list = new ArrayList<>();
            for (Sprint sprint : sprints) {
                if (sprint.isUserInSprint(user))
                    sprint_list.add(sprint);
            }
        }
        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("sprints", sprint_list);
        return "sprint";
    }

    // Opening the add new sprint form page.
    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String addSprint(@Param(value = "name") String name, Model model) {
        log.info("Request to open the new sprint form page");
        Sprint sprint = Sprint.builder().name(name).build();
        // repository.save(sprint);
        model.addAttribute("sprintAttr", sprint);
        return "sprintform";
    }

    // Opening the edit sprint form page.
    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String editSprint(@RequestParam(value = "sprintid", required = true) String id, Model model, Principal user) {
        log.info("Request to open the edit Sprint form page");
        Boolean isAdmin = userService.isAdmin(user.getName());
        Sprint sprint = restTemplate.getForObject("http://SPRINTMANAG-SERVICE/getsprint/{id}", Sprint.class);
        model.addAttribute("sprintAttr", sprint);
        model.addAttribute("isAdmin", isAdmin);
        return "sprintform";
    }


    // view actualHours
    @RequestMapping(value = "/actualHours", method = RequestMethod.GET)
    public String editActualHours(@RequestParam(value = "sprintid", required = true) String id, Model model) {
        log.info("Request to open the edit actualHours form page");
        Sprint sprint = restTemplate.getForObject("http://SPRINTMANAG-SERVICE/getsprint/{id}", Sprint.class);
        model.addAttribute("sprintAttr", sprint);
        return "actualHours";
    }

    // view actualHours
    @RequestMapping(value = "/print", method = RequestMethod.GET)
    public String printSprint(@RequestParam(value = "sprintid", required = true) String id, Model model) {
        log.info("Request to open the print sprint form page");
        Sprint sprint = restTemplate.getForObject("http://SPRINTMANAG-SERVICE/getsprint/{id}", Sprint.class);
        model.addAttribute("sprintAttr", sprint);
        return "printList";
    }

    // Deleting the specified sprint.
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public String delete(@RequestParam(value = "id", required = true) String id, Model model) {
        restTemplate.delete("http://SPRINTMANAG-SERVICE/deletesprint/{id}");
        return "redirect:sprints";
    }

    // Adding a new sprint or updating an existing sprint.
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String save(@ModelAttribute("sprintAttr") Sprint sprint) {
        Sprint sprint1;
        if (!sprint.getId().equals("")) {
            sprint.calcDelivery();
            Sprint temp = restTemplate.getForObject("http://SPRINTMANAG-SERVICE/getsprint/{sprint.getId()}", Sprint.class);
            sprint.setTeam(temp.getTeam());
            restTemplate.postForObject("http://SPRINTMANAG-SERVICE/sprint", sprint, Sprint.class);
        } else {
            if (sprint.getTeam() == null) {
                sprint1 = Sprint.builder().name(sprint.getName()).daily_meeting(sprint.getDaily_meeting()).
                        start(sprint.getStart()).demo(sprint.getDemo()).goal(sprint.getGoal()).plannedPeriod(sprint.getPlannedPeriod())
                        .retrospective(sprint.getRetrospective()).review(sprint.getReview()).tasks(sprint.getTasks()).build();
            } else {
                sprint1 = Sprint.builder().name(sprint.getName()).daily_meeting(sprint.getDaily_meeting()).
                        start(sprint.getStart()).demo(sprint.getDemo()).goal(sprint.getGoal()).plannedPeriod(sprint.getPlannedPeriod())
                        .retrospective(sprint.getRetrospective()).review(sprint.getReview()).team(sprint.getTeam()).tasks(sprint.getTasks()).build();
            }

            sprint1.calcDelivery();
            restTemplate.postForObject("http://SPRINTMANAG-SERVICE/newsprint", sprint1, Sprint.class);
        }
        return "redirect:sprints";
    }


    //Select one team from teams
    @RequestMapping(value = "/teams", method = RequestMethod.GET)
    public String viewTeamsToSelect(@RequestParam(value = "id", required = true) String id, Model model) {
        log.info("Request to fetch all teams from the db for custom team and select team");
        Team[] temp = restTemplate.getForObject("http://SPRINTMANAG-SERVICE/teams", Team[].class);
        List<Team> teams = Arrays.asList(temp);
        teams.removeIf(team -> !team.isActive());
        model.addAttribute("teams", teams);
        model.addAttribute("sprintid", id);
        return "sprintteam";
    }

    @RequestMapping(value = "/addteam", method = RequestMethod.GET)
    public String addTeamToSprint(@RequestParam(value = "sprintid", required = true) String sprintid, @RequestParam(value = "teamid", required = true) String teamid, Model model) {
        Sprint sprint = restTemplate.getForObject("http://SPRINTMANAG-SERVICE/getsprint/{sprintid}", Sprint.class);
        Team team = restTemplate.getForObject("http://SPRINTMANAG-SERVICE/getteam/{teamid}", Team.class);
        sprint.setTeam(team);
        restTemplate.postForObject("http://SPRINTMANAG-SERVICE/sprint", sprint, Sprint.class);
        return "redirect:/api/sprint/edit?sprintid=" + sprintid;
    }

    @RequestMapping(value = "/sprintcharts", method = RequestMethod.GET)
    public String sprintcharts(@RequestParam(value = "sprintid", required = true) String sprintid, ModelMap modelMap) {
        modelMap.addAttribute("sprintid", sprintid);
        return "sprintcharts";
    }

    @RequestMapping(value = "/canvasjschart", method = RequestMethod.GET)
    public String canvasjschart(@RequestParam(value = "sprintid", required = true) String sprintid, ModelMap modelMap) {
        List<List<Map<Object, Object>>> canvasjsDataList = sprintService.getCanvasjsDataList(sprintid);
        Sprint sprint = restTemplate.getForObject("http://SPRINTMANAG-SERVICE/getsprint/{sprintid}", Sprint.class);
        modelMap.addAttribute("dataPointsList", canvasjsDataList);
        modelMap.addAttribute("sprintname", sprint.getName());
        modelMap.addAttribute("teamname", sprint.getTeam().getName());

        return "actualdonedaily";
    }

    @RequestMapping(value = "/canvasjschart1", method = RequestMethod.GET)
    public String canvasjschart1(@RequestParam(value = "sprintid", required = true) String sprintid, ModelMap modelMap) {
        List<List<Map<Object, Object>>> canvasjsDataList = sprintService.getCanvasjsDataList1(sprintid);
        Sprint sprint = restTemplate.getForObject("http://SPRINTMANAG-SERVICE/getsprint/{sprintid}", Sprint.class);
        modelMap.addAttribute("dataPointsList", canvasjsDataList);
        modelMap.addAttribute("sprintname", sprint.getName());
        modelMap.addAttribute("teamname", sprint.getTeam().getName());
        return "actualremaindaily";
    }

    @RequestMapping(value = "/canvasjschart2", method = RequestMethod.GET)
    public String canvasjschart2(@RequestParam(value = "sprintid", required = true) String sprintid, ModelMap modelMap) {
        List<List<Map<Object, Object>>> canvasjsDataList = sprintService.getCanvasjsDataList2(sprintid);
        Sprint sprint = restTemplate.getForObject("http://SPRINTMANAG-SERVICE/getsprint/{sprintid}", Sprint.class);
        modelMap.addAttribute("dataPointsList", canvasjsDataList);
        modelMap.addAttribute("sprintname", sprint.getName());
        modelMap.addAttribute("teamname", sprint.getTeam().getName());
        return "developerPerformance";
    }
}

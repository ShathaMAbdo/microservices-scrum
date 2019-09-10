package se.BTH.webservice.controllers;

import org.apache.log4j.Logger;
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
import java.util.Arrays;
import java.util.List;


@Controller
@RequestMapping("/api/team")
public class TeamController {

    private static Logger log = Logger.getLogger(TeamController.class);

    @Autowired
    @LoadBalanced
    protected RestTemplate restTemplate;
    @Autowired
    private UserService userService;

    // Displaying the initial teams list.
    @RequestMapping(value = "/teams", method = RequestMethod.GET)
    public String getTeams(Model model, Principal user) {
        log.debug("Request to fetch all teams from the mongo database");
        Boolean isAdmin = userService.isAdmin(user.getName());
        Team[] teams = restTemplate.getForObject("http://SPRINTMANAG-SERVICE/teams", Team[].class);
        List<Team> team_list = Arrays.asList(teams);
        if (!isAdmin) {
            team_list = new ArrayList<>();
            for (Team team : teams) {
                if (team.isUserInTeam(user))
                    team_list.add(team);
            }
        }
        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("teams", team_list);
        return "team";
    }

    // Opening the add new team form page.
    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String addTeam(Model model) {
        log.debug("Request to open the new team form page");
        Team team = Team.builder().active(true).build();
        model.addAttribute("teamAttr", team);
        return "teamform";
    }

    // Opening the edit team form page.
    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String editTeam(@RequestParam(value = "id", required = true) String id, Model model) {
        log.debug("Request to open the edit team form page");
        Team team = restTemplate.getForObject("http://SPRINTMANAG-SERVICE/getteam/{id}", Team.class);
        model.addAttribute("teamAttr", team);
        return "teamform";
    }

    // Opening the edit team form page.
    @RequestMapping(value = "/sprintteam", method = RequestMethod.GET)
    public String sprintteam(@RequestParam(value = "sprintid", required = true) String id, Model model, Principal user) {
        log.debug("Request to open the edit team form page");
        Team team;
        Sprint sprint = restTemplate.getForObject("http://SPRINTMANAG-SERVICE/getsprint/{id}", Sprint.class);
        if (sprint.getTeam() != null) {
            team = sprint.getTeam();
            List<User> member_list = team.getUsers();
            member_list.removeIf(u -> u.isActive() == false);
            team.setUsers(member_list);
        } else team = Team.builder().active(true).users(new ArrayList<>()).build();
        Boolean isAdmin = userService.isAdmin(user.getName());
        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("teamAttr", team);
        model.addAttribute("sprintid", id);
        return "sprintteamform";
    }

    // view all  users.
    @RequestMapping(value = "/members", method = RequestMethod.GET)
    public String members(@RequestParam("id") String id, Model model) {
        log.debug("Request to fetch all users from the mongo database");
        Team team = restTemplate.getForObject("http://SPRINTMANAG-SERVICE/getteam/{id}", Team.class);
        User[] users = restTemplate.getForObject("http://SPRINTMANAG-SERVICE/users", User[].class);
        List<User> user_list = Arrays.asList(users);
        user_list.removeIf(u -> !u.isActive());
        model.addAttribute("members", user_list);
        model.addAttribute("team", team);
        return "teammember";
    }

    // add member to team and redirect to team page.
    @RequestMapping(value = "/addmember", method = RequestMethod.GET)
    public String addmember(@RequestParam(value = "id", required = true) String id, @RequestParam(value = "teamid", required = true) String teamid, Model model) {
        Team team = restTemplate.getForObject("http://SPRINTMANAG-SERVICE/getteam/{teamid}", Team.class);
        List<User> members = team.getUsers();
        User user = restTemplate.getForObject("http://SPRINTMANAG-SERVICE/getuser/{id}", User.class);
        if (!team.isMememberExcit(user)) {
            team.getUsers().add(user);
            team.setUsers(members);
        }
        restTemplate.postForObject("http://SPRINTMANAG-SERVICE/team", team, Team.class);
        return "redirect:/api/team/edit?id=" + team.getId();
    }

    // Deleting the specified team.
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public String delete(@RequestParam(value = "id", required = true) String id, Model model) {
        restTemplate.delete("http://SPRINTMANAG-SERVICE/deleteteam/{id}");
        return "redirect:teams";
    }

    // Deleting the specified team.
    @RequestMapping(value = "/enable", method = RequestMethod.GET)
    public String enable(@RequestParam(value = "id", required = true) String id, Model model) {
        Team team = restTemplate.getForObject("http://SPRINTMANAG-SERVICE/getteam/{id}", Team.class);
        team.changeActive();
        restTemplate.postForObject("http://SPRINTMANAG-SERVICE/team", team, Team.class);
        return "redirect:teams";
    }

    // Adding a new team or updating an existing team.
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String save(@ModelAttribute("teamAttr") Team team) {
        List<User> users = new ArrayList<>();
        if (team.getId().equals("")) {
            Team team1 = Team.builder().name(team.getName()).active(true).users(users).build();
            restTemplate.postForObject("http://SPRINTMANAG-SERVICE/newteam", team1, Team.class);
        } else {
            List<User> members = team.getUsers();
            members.removeIf(u -> u.isActive() == false);
            team.setUsers(members);
            restTemplate.postForObject("http://SPRINTMANAG-SERVICE/team", team, Team.class);
        }
        return "redirect:teams";
    }
}

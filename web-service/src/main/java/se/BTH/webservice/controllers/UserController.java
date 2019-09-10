package se.BTH.webservice.controllers;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import se.BTH.webservice.models.*;
import se.BTH.webservice.security.SecurityService;
import se.BTH.webservice.services.UserService;
import se.BTH.webservice.validators.UserValidator;


import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
//@RequestMapping("/api/user")
public class UserController {

    private static Logger log = Logger.getLogger(UserController.class);

    @Autowired
    @LoadBalanced
    protected RestTemplate restTemplate;
    @Autowired
    private SecurityService securityService;
    @Autowired
    private UserValidator userValidator;
    @Autowired
    private UserService userService;


    // Displaying the initial users list.

    @RequestMapping(value = "/api/user/users", method = RequestMethod.GET)
    //  @PreAuthorize("hasRole('ADMIN')")
    public String getUsers(Model model, Principal user) {
        log.debug("Request to fetch all users from the mongo database" + user.getName());
        Boolean isAdmin = userService.isAdmin(user.getName());
        User[] users = restTemplate.getForObject("http://SPRINTMANAG-SERVICE/users", User[].class);
        List<User> user_list = Arrays.asList(users);
        if (isAdmin) {
            user_list = new ArrayList<>();
            User currentuser = restTemplate.getForObject("http://SPRINTMANAG-SERVICE/userbyname/{user.getName()}", User.class);
            user_list.add(currentuser);
        }
        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("users", user_list);
        return "user";
    }

    // Opening the add new user form page.
    @RequestMapping(value = "/api/user/add", method = RequestMethod.GET)
    public String addUser(Model model) {
        log.debug("Request to open the new user form page");
        List<Role> roles = new ArrayList<>();
        roles.add(Role.builder().name(RoleName.ROLE_USER).build());
        User user = User.builder().roles(roles).active(true).build();
        //  repository.save(user);
        model.addAttribute("userAttr", user);
        return "userform";
    }

    // user profile
    @RequestMapping(value = "/api/user/edit", method = RequestMethod.GET)
    public String editUser(Principal user, Model model) {
        log.debug("Request to open the edit user profile form page");
        User currentuser = restTemplate.getForObject("http://SPRINTMANAG-SERVICE/userbyname/{user.getName()}", User.class);
        model.addAttribute("userAttr", currentuser);
        return "userform";
    }


    @RequestMapping(value = "/api/user/profile", method = RequestMethod.GET)
    public String profile(User user, Model model) {
        log.debug("Request to open the edit user form page");
        model.addAttribute("userAttr", user);
        return "profile";
    }

    // Deleting the specified user.
    @RequestMapping(value = "/api/user/delete", method = RequestMethod.GET)
    public String delete(@RequestParam(value = "id", required = true) String id, Model model) {
        restTemplate.delete("http://SPRINTMANAG-SERVICE/deleteuser/{id}");
        return "redirect:users";
    }

    // unactivate the specified user.
    @RequestMapping(value = "/api/user/enable", method = RequestMethod.GET)
    public String enable(@RequestParam(value = "id", required = true) String id, Model model) {
        User user = restTemplate.getForObject("http://SPRINTMANAG-SERVICE/getuser/{id}", User.class);
        user.changeActive();
        restTemplate.postForObject("http://SPRINTMANAG-SERVICE/user", user, User.class);
        return "redirect:users";
    }

    @RequestMapping(value = "/api/user/admin", method = RequestMethod.GET)
    public String admin(@RequestParam(value = "id", required = true) String id, Model model) {
        User user = restTemplate.getForObject("http://SPRINTMANAG-SERVICE/getuser/{id}", User.class);
        List<Role> roles = user.getRoles();
        Role admin = restTemplate.getForObject("http://SPRINTMANAG-SERVICE/rolebyname/{RoleName.ROLE_ADMIN}", Role.class);
        Role oldAdmin = roles.stream().filter(r -> r.getName().equals(RoleName.ROLE_ADMIN)).findAny().orElse(null);
        if (oldAdmin == null) {
            roles.add(admin);
            user.setRoles(roles);
            restTemplate.postForObject("http://SPRINTMANAG-SERVICE/user", user, User.class);
        }
        return "redirect:users";
    }

    // Adding a new user or updating an existing user.
    @RequestMapping(value = "/api/user/save", method = RequestMethod.POST)
    public String save(@ModelAttribute("userAttr") User user) {                  // needs test for edit or create
        restTemplate.postForObject("http://SPRINTMANAG-SERVICE/user", user, User.class);
        return "redirect:/";
    }

    @GetMapping("/registration")
    public String registration(Model model) {
        List<Role> roles = new ArrayList<>();
        roles.add(Role.builder().name(RoleName.ROLE_USER).build());
        model.addAttribute("userForm", User.builder().roles(roles).active(true).build());

        return "registration";
    }

    @PostMapping("/registration")
    public String registration(@ModelAttribute("userForm") User userForm, BindingResult bindingResult, Model model) {
        userValidator.validate(userForm, bindingResult);

        if (bindingResult.hasErrors()) {
            return "registration";
        }
        List<Role> list = new ArrayList<>();
        list.add(Role.builder().name(RoleName.ROLE_USER).build());
        userService.save(User.builder().name(userForm.getName()).active(true).password(userForm.getPassword())
                .username(userForm.getUsername()).city(userForm.getCity()).phone(userForm.getPhone()).build());
        securityService.autoLogin(userForm.getUsername(), userForm.getPasswordConfirm());
        Boolean isAdmin = userService.isAdmin(userForm.getUsername());
        model.addAttribute("isAdmin", isAdmin);
        return "hello";
    }

    @GetMapping("/login")
    public String login(Model model, String error, String logout) {
        if (error != null)
            model.addAttribute("error", "Your username and password is invalid.");

        if (logout != null)
            model.addAttribute("message", "You have been logged out successfully.");

        return "/login";
    }

    @GetMapping(value = {"/", "/api/", "/api/hello"})
    public String hello(Model model, @RequestParam(value = "name", required = false, defaultValue = "Administrator") String name, Principal user) {
        model.addAttribute("name", name);
        Boolean isAdmin = userService.isAdmin(user.getName());
        model.addAttribute("isAdmin", isAdmin);
        return "hello";
    }
}
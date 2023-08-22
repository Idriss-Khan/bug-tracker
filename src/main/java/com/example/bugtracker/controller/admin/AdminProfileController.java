package com.example.bugtracker.controller.admin;

import com.example.bugtracker.model.Bug;
import com.example.bugtracker.model.Project;
import com.example.bugtracker.model.Role;
import com.example.bugtracker.model.User;
import com.example.bugtracker.service.ProjectService;
import com.example.bugtracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin/profile")
public class AdminProfileController {

    @Autowired
    private UserService userService;
    @Autowired
    private ProjectService projectService;


    // returns user profile page
    @GetMapping()
    public ModelAndView profilePage(Principal principal) {
        ModelAndView mav = new ModelAndView("admin/developer/profile");
        mav.addObject("pageTitle", "Profile Page");

        String email = principal.getName();
        User currentUser = userService.getUserByEmail(email);
        mav.addObject("user", currentUser);


        // Passing in projects and tasks
        Set<Project> projects = projectService.getProjectsForUser(currentUser);
        mav.addObject("userProjects", projects);

        Map<Project, List<Bug>> projectBugsMap = new HashMap<>();
        for (Project project : projects) {
            List<Bug> userBugs = project.getBugs().stream()
                    .filter(bug -> bug.getAssignedUser().equals(currentUser))
                    .collect(Collectors.toList());
            projectBugsMap.put(project, userBugs);
        }
        mav.addObject("projectBugsMap", projectBugsMap);

        return mav;
    }

    @PostMapping("/save")
    public RedirectView saveCurrentUser(@ModelAttribute("user") User updatedUser, Principal principal) {
        String email = principal.getName();
        User currentUser = userService.getUserByEmail(email);

        currentUser.setFirstName(updatedUser.getFirstName());
        currentUser.setLastName(updatedUser.getLastName());
        currentUser.setPhoneNumber(updatedUser.getPhoneNumber());
        currentUser.setPassword(updatedUser.getPassword());

        userService.save(currentUser);
        return new RedirectView("/admin/profile");
    }






}
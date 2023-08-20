package com.example.bugtracker.controller.admin;

import com.example.bugtracker.model.Bug;
import com.example.bugtracker.model.Project;
import com.example.bugtracker.model.User;
import com.example.bugtracker.service.BugService;
import com.example.bugtracker.service.ProjectService;
import com.example.bugtracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.HashSet;
import java.util.Set;


/**
 * Rest controller for handling the admin home page.
 */
@RestController
@RequestMapping("/admin")
public class AdminHomeController {

    @Autowired
    private UserService userService;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private BugService bugService;

    // Returns developer homepage, displays projects and bug tasks the user is working on, and shows
    // summary of bugs

    @GetMapping
    public ModelAndView getAdminHomePage(ModelAndView modelAndView, Principal principal) {
        modelAndView.setViewName("admin/index");
        modelAndView.addObject("pageTitle", "Dashboard");

        String email = principal.getName();
        User currentUser = userService.getUserByEmail(email);
        modelAndView.addObject("currentUser", currentUser);

        Set<Project> projects = projectService.getProjectsForUser(currentUser);
        modelAndView.addObject("userProjects", projects);

        // Fetch data for bug statistics
        int totalBugs = bugService.getTotalBugCount();
        int openBugs = bugService.getOpenBugCount();
        int closedBugs = bugService.getClosedBugCount();
        int dueToday = bugService.getBugsDueToday();
        int dueIn7Days = bugService.getBugsDueIn7Days();

        modelAndView.addObject("totalBugs", totalBugs);
        modelAndView.addObject("openBugs", openBugs);
        modelAndView.addObject("closedBugs", closedBugs);
        modelAndView.addObject("dueToday", dueToday);
        modelAndView.addObject("dueIn7Days", dueIn7Days);

        return modelAndView;
    }



}

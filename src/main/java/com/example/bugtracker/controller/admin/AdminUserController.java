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

/**
 * Rest controller for handling the admin user page.
 */
@RestController
@RequestMapping("/admin/user")
public class AdminUserController {

    @Autowired
    private UserService userService;
    @Autowired
    private ProjectService projectService;

    @GetMapping
    public ModelAndView getAdminUsersPage() {
        ModelAndView mav = new ModelAndView("admin/user/user");
        List<User> listUsers = userService.listAll(); // get all users
        mav.addObject("pageTitle", "Users");
        mav.addObject("users", listUsers); // Passing in users
        return mav;
    }

    // returns edit user page
    @GetMapping("/edit/{id}")
    public ModelAndView editUser(@PathVariable("id") Integer id) {
        ModelAndView mav = new ModelAndView("admin/user/edit_user");
        User user = userService.get(id);
        List<Role> listRoles = userService.getRoles();
        mav.addObject("pageTitle", "View User Details");
        mav.addObject("user", user);
        mav.addObject("listRoles", listRoles);

        // Passing in projects and tasks
        Set<Project> projects = projectService.getProjectsForUser(user);
        mav.addObject("userProjects", projects);

        Map<Project, List<Bug>> projectBugsMap = new HashMap<>();
        for (Project project : projects) {
            List<Bug> userBugs = project.getBugs().stream()
                    .filter(bug -> bug.getAssignedUser().equals(user))
                    .collect(Collectors.toList());
            projectBugsMap.put(project, userBugs);
        }
        mav.addObject("projectBugsMap", projectBugsMap);

        return mav;
    }

    @PostMapping("/save")
    public ModelAndView saveUser(User user) {
        ModelAndView mav = new ModelAndView("redirect:/admin/user");
        User existingUser = userService.getUserByEmail(user.getEmail());
        user.setProfilePicture(existingUser.getProfilePicture());
        userService.save(user);
        return mav;
    }



}

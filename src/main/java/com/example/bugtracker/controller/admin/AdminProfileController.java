package com.example.bugtracker.controller.admin;

import com.example.bugtracker.model.*;
import com.example.bugtracker.service.NotificationService;
import com.example.bugtracker.service.ProjectService;
import com.example.bugtracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin/profile")
public class AdminProfileController {

    @Autowired
    private UserService userService;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private NotificationService notificationService;


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
                    .filter(bug -> {
                        User assignedUser = bug.getAssignedUser();
                        return assignedUser != null && assignedUser.equals(currentUser);
                    })
                    .collect(Collectors.toList());
            projectBugsMap.put(project, userBugs);
        }
        mav.addObject("projectBugsMap", projectBugsMap);

        int notificationCount = notificationService.countUnreadNotifications(currentUser);
        mav.addObject("notificationCount", notificationCount);

        return mav;
    }


    @GetMapping("/notifications")
    public ModelAndView notificationPage(Principal principal) {
        ModelAndView mav = new ModelAndView("admin/developer/notification");
        mav.addObject("pageTitle", "Notifications");

        String email = principal.getName();
        User currentUser = userService.getUserByEmail(email);
        mav.addObject("user", currentUser);

        List<Notification> userNotifications = notificationService.getUserNotifications(currentUser);
        mav.addObject("userNotifications", userNotifications);

        int notificationCount = notificationService.countUnreadNotifications(currentUser);
        mav.addObject("notificationCount", notificationCount);

        return mav;
    }

    @PatchMapping("/markRead/{notificationId}")
    public RedirectView markNotificationAsRead(@PathVariable Integer notificationId) {
        notificationService.markNotificationAsRead(notificationId);
        RedirectView redirectView = new RedirectView("/admin/profile/notifications");
        redirectView.setExposeModelAttributes(false); // Optional: Prevent exposing model attributes in the URL
        return redirectView;
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
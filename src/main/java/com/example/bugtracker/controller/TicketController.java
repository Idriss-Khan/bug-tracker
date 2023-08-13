package com.example.bugtracker.controller;

import com.example.bugtracker.model.Bug;
import com.example.bugtracker.model.User;
import com.example.bugtracker.service.BugService;
import com.example.bugtracker.service.ProjectService;
import com.example.bugtracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.security.Principal;
import java.util.List;


/**
 * Rest controller for handling the ticket submissions.
 */
@RestController
@RequestMapping("/ticket")
public class TicketController {

    @Autowired
    private ProjectService projectService;
    @Autowired
    private UserService userService;

    @Autowired
    private BugService bugService;

    /**
     * Returns ticket submission page.
     */
    @GetMapping("/submit")
    public ModelAndView viewTicketSubmissionPage(Principal principal) {
        ModelAndView mav = new ModelAndView("submit_ticket");

        mav.addObject("bug", new Bug());
        mav.addObject("projects", projectService.getAllProjects());

        String email = principal.getName();
        User currentUser = userService.getUserByEmail(email);
        mav.addObject("user", currentUser);

        return mav;
    }

    @PostMapping("/submit")
    public ModelAndView submitBug(@ModelAttribute("bug") Bug bug, @RequestParam("bugImages") List<MultipartFile> bugImages) {
        bugService.createBug(bug, bugImages);
        ModelAndView mav = new ModelAndView("ticket_success");
        return mav;
    }


    /**
     * Returns bug detail page
     */
    @GetMapping("/view/{id}")
    public ModelAndView viewBugPage(@PathVariable("id") Integer id) {
        ModelAndView mav = new ModelAndView("bug_detail");
        Bug bug = bugService.getBugById(id);
        mav.addObject("bug", bug);
        return mav;
    }



}

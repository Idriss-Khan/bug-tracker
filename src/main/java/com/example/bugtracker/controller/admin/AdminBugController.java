package com.example.bugtracker.controller.admin;

import com.example.bugtracker.model.Bug;
import com.example.bugtracker.model.User;
import com.example.bugtracker.repository.BugImageRepository;
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
 * This REST controller handles requests related to bugs and tasks.
 */
@RestController
@RequestMapping("/admin/bug")
public class AdminBugController {

    @Autowired
    private ProjectService projectService;
    @Autowired
    private BugService bugService;
    @Autowired
    private UserService userService;

    /**
     * Returns page to submit bugs.
     * Takes in current user.
     */
    @GetMapping("/submit")
    public ModelAndView getSubmitBugPage(Principal principal) {
        ModelAndView mav = new ModelAndView("admin/bug/submit_bug");
        mav.addObject("pageTitle", "Submit Bug");
        mav.addObject("bug", new Bug());
        mav.addObject("projects", projectService.getAllProjects());

        String email = principal.getName();
        User currentUser = userService.getUserByEmail(email);
        mav.addObject("user", currentUser);

        return mav;
    }

    /**
     * Returns to project list page after adding project
     */
    @PostMapping("/submit")
    public RedirectView submitBug(@ModelAttribute("bug") Bug bug, @RequestParam("bugImages") List<MultipartFile> bugImages) {
        bugService.createBug(bug, bugImages);
        return new RedirectView("/admin/project");
    }

}

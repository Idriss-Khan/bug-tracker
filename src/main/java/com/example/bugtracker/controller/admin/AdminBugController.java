package com.example.bugtracker.controller.admin;

import com.example.bugtracker.model.Bug;
import com.example.bugtracker.model.Comment;
import com.example.bugtracker.model.User;
import com.example.bugtracker.service.BugService;
import com.example.bugtracker.service.CommentService;
import com.example.bugtracker.service.ProjectService;
import com.example.bugtracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import java.security.Principal;
import java.util.*;


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
    @Autowired
    private CommentService commentService;

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

    /**
     * Returns bug details page
     */
    @GetMapping("/view/{id}")
    public ModelAndView getBugDetailPage(@PathVariable("id") Integer id, Principal principal) {
        ModelAndView mav = new ModelAndView("admin/bug/bug_detail");
        Bug bug = bugService.getBugById(id);

        mav.addObject("pageTitle", "Bug Details");
        mav.addObject("bug", bug);

        List<Comment> comments = commentService.getCommentsForBug(bug);
        mav.addObject("comments", comments);

        String email = principal.getName();
        User currentUser = userService.getUserByEmail(email);
        mav.addObject("user", currentUser);

        return mav;
    }

    @PostMapping("/addcomment")
    public RedirectView addComment(
            @RequestParam("bugId") Integer bugId,
            @RequestParam("commentContent") String commentContent,
            @RequestParam(value = "parentCommentId", required = false) Integer parentCommentId,
            Principal principal) {
        Bug bug = bugService.getBugById(bugId);
        User currentUser = userService.getUserByEmail(principal.getName());

        Comment comment = new Comment();
        comment.setBug(bug);
        comment.setUser(currentUser);
        comment.setContent(commentContent);
        comment.setCreatedAt(new Date());

        if (parentCommentId != null) {
            Comment parentComment = commentService.getCommentById(parentCommentId);
            comment.setParentComment(parentComment);
        }

        commentService.saveComment(comment);

        return new RedirectView("/admin/bug/view/" + bugId);
    }



    /**
     * Returns to bug update page
     */
    @GetMapping("/edit/{id}")
    public ModelAndView getUpdateBugPage(@PathVariable("id") Integer id) {
        ModelAndView mav = new ModelAndView("admin/bug/update_bug");
        Bug bug = bugService.getBugById(id);
        mav.addObject("bug", bug);

        // Fetch associated users from the project
        Set<User> associatedUsers = bug.getProject().getAssociatedUsers();
        mav.addObject("associatedUsers", associatedUsers);

        mav.addObject("pageTitle", "Update Bug Details");
        return mav;
    }


    @PostMapping("/edit/{id}")
    public RedirectView updateBug(@PathVariable("id") Integer id, @ModelAttribute("bug") Bug updatedBug) {
        Bug existingBug = bugService.getBugById(id);

        bugService.updateBug(existingBug, updatedBug);
        return new RedirectView("/admin/bug/edit/" + id);
    }

}

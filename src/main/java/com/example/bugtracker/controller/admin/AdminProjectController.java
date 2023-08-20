package com.example.bugtracker.controller.admin;


import com.example.bugtracker.model.Project;
import com.example.bugtracker.model.User;
import com.example.bugtracker.service.ProjectService;
import com.example.bugtracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;


/**
 * This REST controller handles requests related to projects.
 */
@RestController
@RequestMapping("/admin/project")
public class AdminProjectController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private UserService userService;

    @GetMapping
    public ModelAndView getAdminProjectPage() {
        ModelAndView mav = new ModelAndView("admin/project/project");
        mav.addObject("pageTitle", "Projects");
        mav.addObject("projects", projectService.getAllProjects());
        return mav;
    }

    /**
     * Returns the page for creating a project.
     */
    @GetMapping("/create")
    public ModelAndView getAddProjectPage() {
        ModelAndView mav = new ModelAndView("admin/project/add_project");
        mav.addObject("pageTitle", "Add Project");
        mav.addObject("project", new Project());


        // Fetch users with role "PROJECT MANAGER"
        List<User> projectManagers = userService.getUsersByRole("PROJECT MANAGER");
        mav.addObject("projectManagers", projectManagers);

        return mav;
    }

    /**
     * Returns to project list page after adding project
     */
    @PostMapping
    public RedirectView addProject(@ModelAttribute("project") Project project) {
        projectService.createProject(project);
        return new RedirectView("/admin/project");
    }

    /**
     * Returns to project update page
     */
    @GetMapping("/edit/{id}")
    public ModelAndView getUpdateProjectPage(@PathVariable("id") Integer id) {
        ModelAndView mav = new ModelAndView("admin/project/update_project");

        Project project = projectService.getProjectById(id);
        mav.addObject("project", project);

        List<User> projectManagers = userService.getUsersByRole("PROJECT MANAGER");
        mav.addObject("projectManagers", projectManagers);

        List<User> developers = userService.getUsersByRole("DEVELOPER");
        mav.addObject("developers", developers);

        mav.addObject("pageTitle", "Update Project Details");
        return mav;
    }


    @PostMapping("/edit/{id}")
    public RedirectView updateProject(@PathVariable("id") Integer id, @ModelAttribute("project") Project project) {
        projectService.updateProject(project);
        return new RedirectView("/admin/project");
    }

    /**
     * Returns to project details page
     */
    @GetMapping("/view/{id}")
    public ModelAndView getProjectDetailPage(@PathVariable("id") Integer id) {
        ModelAndView mav = new ModelAndView("admin/project/project_detail");

        // Fetch the project details from the service/repository based on the given id
        Project project = projectService.getProjectById(id);

        mav.addObject("pageTitle", "Project Details");
        mav.addObject("project", project);

        return mav;
    }

    /**
     * Deletes a category with the specified ID.
     */
    @DeleteMapping("/{id}")
    public RedirectView deleteProject(@PathVariable("id") Integer id) {
        projectService.deleteProjectById(id);
        return new RedirectView("/admin/project");
    }


}

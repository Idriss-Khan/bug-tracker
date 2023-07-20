package com.example.bugtracker.controller.admin;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;


/**
 * Rest controller for handling the admin home page.
 */
@RestController
@RequestMapping("/admin")
public class AdminHomeController {

    // Returns admin home page
    @GetMapping
    public ModelAndView getAdminHomePage(ModelAndView modelAndView) {
        modelAndView.setViewName("admin/index");
        modelAndView.addObject("pageTitle", "Dashboard");
        return modelAndView;
    }
}

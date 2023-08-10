package com.example.bugtracker.controller;

import com.example.bugtracker.model.Bug;
import com.example.bugtracker.model.User;
import com.example.bugtracker.service.BugService;
import com.example.bugtracker.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.List;

/**
 * Rest controller for handling the homepage and registration.
 */
@RestController
@RequestMapping("/")
public class HomeController {

    @Autowired
    private UserService userService;
    @Autowired
    private BugService bugService;

    /**
     * Returns home page.
     * @param model Model object
     * @param principal Principal object
     * @return ModelAndView object of the home page
     */
    @GetMapping()
    public ModelAndView viewHomePage(Model model, Principal principal) {
        ModelAndView mav = new ModelAndView("index");

        if (principal != null) {
            String email = principal.getName();
            User currentUser = userService.getUserByEmail(email);
            List<Bug> userBugs = bugService.getBugsByUser(currentUser);
            mav.addObject("userBugs", userBugs);
        }

        return mav;
    }




    /**
     * Returns registration page.
     * @return ModelAndView object of the registration page
     */
    @GetMapping("/register")
    public ModelAndView showRegistrationForm() {
        ModelAndView mav = new ModelAndView("signup_form");
        mav.addObject("user", new User());
        return mav;
    }

    /**
     * Returns page for registering the user.
     * @param user User object
     * @param result BindingResult object
     * @return ModelAndView of the success page or the registration page with error message
     */
    @PostMapping("/process_register")
    public ModelAndView processRegister(@Valid User user, BindingResult result) {
        ModelAndView mav;
        if (result.hasErrors()) {
            mav = new ModelAndView("signup_form");
            return mav;
        }

        // Error Message
        if (userService.emailExists(user.getEmail())) {
            mav = new ModelAndView("signup_form");
            mav.addObject("errorMessage", "Email already exists!");
            return mav;
        }

        // Successful register
        userService.saveWithDefaultRole(user);
        mav = new ModelAndView("register_success");
        return mav;
    }

    /**
     * Returns login page.
     * @return ModelAndView representing the login page
     */
    @GetMapping("/login")
    public ModelAndView loginPage() {
        ModelAndView mav = new ModelAndView("login");
        return mav;
    }



}

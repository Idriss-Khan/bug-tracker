package com.example.bugtracker.controller;

import com.example.bugtracker.model.User;
import com.example.bugtracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import java.security.Principal;


@RestController
@RequestMapping("/profile")
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * Returns the profile page for the logged-in user.
     */
    @GetMapping()
    public ModelAndView profilePage(Principal principal) {
        ModelAndView mav = new ModelAndView("user/profile_page");
        String email = principal.getName();
        User currentUser = userService.getUserByEmail(email);
        mav.addObject("user", currentUser);
        return mav;
    }

    /**
     * Handles POST requests for editing the user's profile image.
     */
    @PostMapping("/edit-profile-image")
    public RedirectView saveCurrentUser(@ModelAttribute("user") User user, Principal principal, MultipartFile profilePicFile) {
        String email = principal.getName();
        User currentUser = userService.getUserByEmail(email);
        userService.updateProfileImage(currentUser, profilePicFile);
        return new RedirectView("/profile");
    }

    /**
     * Handles POST requests for editing the user's profile.
     */
    @PostMapping("/edit-profile")
    public RedirectView saveCurrentUser(@ModelAttribute("user") User updatedUser, Principal principal) {
        String email = principal.getName();
        User currentUser = userService.getUserByEmail(email);

        currentUser.setFirstName(updatedUser.getFirstName());
        currentUser.setLastName(updatedUser.getLastName());
        currentUser.setPassword(updatedUser.getPassword());

        userService.save(currentUser);
        return new RedirectView("/profile");
    }


}

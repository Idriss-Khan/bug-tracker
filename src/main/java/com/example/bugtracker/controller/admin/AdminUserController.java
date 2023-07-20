package com.example.bugtracker.controller.admin;

import com.example.bugtracker.model.Role;
import com.example.bugtracker.model.User;
import com.example.bugtracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * Rest controller for handling the admin user page.
 */
@RestController
@RequestMapping("/admin/user")
public class AdminUserController {

    @Autowired
    private UserService userService;

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
        return mav;
    }

    // saves user
    @PostMapping("/save")
    public ModelAndView saveUser(User user) {
        ModelAndView mav = new ModelAndView("redirect:/admin/user");
        userService.save(user);
        return mav;
    }



}

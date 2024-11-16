package com.saleshalal.SEProject.controller;

import com.saleshalal.SEProject.model.UserModel;
import com.saleshalal.SEProject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/*
 * This class is the controller for the user registration and login pages.
 * GET requests to /auth/register will show the registration form.
 * POST requests to /auth/register will register the user.
 * On successful registration, the user will be redirected to the login page.
 * GET requests to /auth/login will show the login form.
 * POST requests to /auth/login will log the user in.
 *
 */
@Controller
@RequestMapping("/register")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new UserModel());
        return "customer";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") UserModel user) {
        userService.registerUser(user, user.getRole());
        return "redirect:login";
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String loginUser(@RequestParam String email, @RequestParam String password, Model model) {
        if (userService.validateLogin(email, password)) {
            return "redirect:/index";
        }
        model.addAttribute("error", "Invalid credentials");
        return "login";
    }
}
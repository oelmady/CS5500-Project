package com.saleshalal.SEProject.controller;

import com.saleshalal.SEProject.model.ERole;
import com.saleshalal.SEProject.model.UserModel;
import com.saleshalal.SEProject.model.UserRole;
import com.saleshalal.SEProject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    // Login - common for all users
    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    // Registration choice page
    @GetMapping("/register")
    public String showRegistrationChoice() {
        return "register-choice";  // A page where users choose customer or vendor
    }

    // Customer registration
    @GetMapping("/register/customer")
    public String showCustomerRegistration(Model model) {
        model.addAttribute("user", new UserModel());
        model.addAttribute("userType", "CUSTOMER");
        return "register/customer";
    }

    // Vendor registration
    @GetMapping("/register/vendor")
    public String showVendorRegistration(Model model) {
        model.addAttribute("user", new UserModel());
        model.addAttribute("userType", "VENDOR");
        return "register/vendor";
    }

    // todo: Handle registration for both types
    @PostMapping("/register/{userType}")
    public String registerUser(@PathVariable String userType,
                               @ModelAttribute("user") UserModel user,
                               Model model) {
        try {
            userService.registerUser(user, UserRole.valueOf(userType.toUpperCase()));
            return "redirect:/login?registered=true";
        } catch (Exception e) {
            model.addAttribute("error", "Registration failed: " + e.getMessage());
            return "register/" + userType.toLowerCase();
        }
    }
}
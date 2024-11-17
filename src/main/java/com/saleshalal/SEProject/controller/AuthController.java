package com.saleshalal.SEProject.controller;

import com.saleshalal.SEProject.model.Customer;
import com.saleshalal.SEProject.model.Vendor;
import com.saleshalal.SEProject.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    // Login page (common for all)
    @GetMapping("/login")
    public String showLoginForm() {
        logger.info("Showing login form");
        return "login";
    }

    // Customer registration
    @GetMapping("/register/customer")
    public String showCustomerRegistration(Model model) {
        model.addAttribute("customer", new Customer());
        logger.info("Showing customer registration form");
        return "register/customer";
    }

    @PostMapping("/register/customer")
    public String registerCustomer(@ModelAttribute Customer customer,
                                   BindingResult result,
                                   Model model) {
        logger.info("Registering customer");
        try {
            userService.registerCustomer(customer);
            return "redirect:/login?registered=true";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "register/customer";
        }
    }

    // Vendor registration
    @GetMapping("/register/vendor")
    public String showVendorRegistration(Model model) {
        logger.info("Showing vendor registration form");
        model.addAttribute("vendor", new Vendor());
        return "register/vendor";
    }

    @PostMapping("/register/vendor")
    public String registerVendor(@ModelAttribute Vendor vendor,
                                 BindingResult result,
                                 Model model) {
        logger.info("Registering vendor");
        if (result.hasErrors()) {
            return "register/vendor";
        }

        try {
            userService.registerVendor(vendor);
            return "redirect:/login?registered=true";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "register/vendor";
        }
    }
}
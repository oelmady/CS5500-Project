package com.saleshalal.SEProject.controller;

import com.saleshalal.SEProject.model.Customer;
import com.saleshalal.SEProject.model.Vendor;
import com.saleshalal.SEProject.service.UserService;
import com.saleshalal.SEProject.data.CustomerDTO;
import com.saleshalal.SEProject.data.VendorDTO;

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
        model.addAttribute("customerDTO", new CustomerDTO());
        logger.info("Showing customer registration form");
        return "customer/customer-registration";
    }

    // Customer registration using DTO
    @PostMapping("/register/customer")
    public String registerCustomer(@ModelAttribute CustomerDTO customerDTO,
                                   BindingResult result,
                                   Model model) {
        logger.info("Registering customer with email: {}", customerDTO.getEmail());

        if (result.hasErrors()) {
            logger.warn("Validation errors found during customer registration");
            return "customer/customer-registration";
        }
        try {
            userService.registerCustomer(customerDTO);
            logger.info("Customer registered successfully: {}", customerDTO.getEmail());
            return "redirect:/login?registered=true";
        } catch (Exception e) {
            logger.error("Error occurred while registering customer: {}", e.getMessage(), e);
            model.addAttribute("error", e.getMessage());
            return "customer/customer-registration";
        }
    }

    // Vendor registration
    @GetMapping("/register/vendor")
    public String showVendorRegistration(Model model) {
        logger.info("Showing vendor registration form");
        model.addAttribute("vendorDTO", new VendorDTO());
        return "vendor/vendor-registration";
    }

    // Refactored vendor registration using DTO
    @PostMapping("/register/vendor")
    public String registerVendor(@ModelAttribute VendorDTO vendorDTO,
                                 BindingResult result,
                                 Model model) {
        logger.info("Registering vendor with business name: {}", vendorDTO.getBusinessName());

        if (result.hasErrors()) {
            logger.warn("Validation errors found during vendor registration");
            return "vendor/vendor-registration";
        }

        try {
            userService.registerVendor(vendorDTO);
            logger.info("Vendor registered successfully: {}", vendorDTO.getBusinessName());
            return "redirect:/login?registered=true";
        } catch (Exception e) {
            logger.error("Error occurred while registering vendor: {}", e.getMessage(), e);
            model.addAttribute("error", e.getMessage());
            return "vendor/vendor-registration";
        }
    }
}
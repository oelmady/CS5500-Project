package com.saleshalal.SEProject.controller;

import com.saleshalal.SEProject.model.Customer;
import com.saleshalal.SEProject.model.Vendor;
import com.saleshalal.SEProject.service.UserService;
import com.saleshalal.SEProject.data.CustomerDTO;
import com.saleshalal.SEProject.data.VendorDTO;

import org.apache.velocity.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

/**
 * Implements login and registration functionality.
 */
@Controller
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Shows the login form.
     * This is called whenever a user is redirected to the login page.
     * @return the view name to be rendered
     */
    @GetMapping("/login")
    public String showLoginForm() {
        logger.info("Showing login form");
        return "login";
    }

    /**
     * Shows the customer registration form.
     *
     * @param model the model to pass data to the view
     * @return the customer registration view to be rendered
     */
    @GetMapping("/register/customer")
    public String showCustomerRegistration(Model model) {
        model.addAttribute("customerDTO", new CustomerDTO());
        logger.info("Showing customer registration form");
        return "customer/customer-registration";
    }

    /**
     * Handles the customer registration form submission.
     *
     * @param customerDTO the data transfer object containing customer registration details
     * @param result      the binding result for validation errors
     * @param model       the model to pass data to the view
     * @return the customer registration view to be rendered
     */
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
            return "redirect:/login";
        } catch (Exception e) {
            logger.error("Error occurred while registering customer: {}", e.getMessage(), e);
            model.addAttribute("error", e.getMessage());
            return "customer/customer-registration";
        }
    }

    /**
     * Shows the vendor registration form.
     *
     * @param model the model to pass data to the view
     * @return the vendor registration view name to be rendered
     */
    @GetMapping("/register/vendor")
    public String showVendorRegistration(Model model) {
        logger.info("Showing vendor registration form");
        model.addAttribute("vendorDTO", new VendorDTO());
        return "vendor/vendor-registration";
    }

    /**
     * Handles the vendor registration form submission.
     *
     * @param vendorDTO the data transfer object containing vendor registration details
     * @param result    the binding result for validation errors
     * @param model     the model to pass data to the view
     * @return the view name to be rendered
     */
    @PostMapping("/register/vendor")
    public String registerVendor(@ModelAttribute VendorDTO vendorDTO, BindingResult result,
                                 Model model) {
        logger.info("Registering vendor with business name: {}", vendorDTO.getBusinessName());

        if (result.hasErrors()) {
            logger.warn("Validation errors found during vendor registration");
            return "vendor/vendor-registration";
        }

        try {
            userService.registerVendor(vendorDTO);
            logger.info("Vendor registered successfully: {}", vendorDTO.getBusinessName());
            return "redirect:/login";
        } catch (Exception e) {
            logger.error("Error occurred while registering vendor: {}", e.getMessage(), e);
            model.addAttribute("error", e.getMessage());
            return "vendor/vendor-registration";
        }
    }

    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password) {
        logger.info("POST /login called with email: {}", email);
        return "redirect:/customer-dashboard";
    }


    /**
     * Logs a user into the appropriate dashboard based on their role.
     *
     * @param email    the email of the user to log in
     * @param password the password of the user to log in
     * @param model    the model to pass data to the view
     * @return the view name to be rendered
     */
    @PostMapping("/TODO")
    public String login(@RequestParam("email") String email,
                        @RequestParam("password") String password,
                        Model model) {
        logger.info("POST login for user: {}", email);
        int credential = userService.validateUser(email, password);
        // Validate user credentials
        if (credential == 1) {
            logger.info("Login successful for user: {}", email);

            if (userService.isCustomer(email)) {
                logger.info("User is a customer");
                return "redirect:/customer-dashboard";
            } else if (userService.isVendor(email)) {
                logger.info("User is a vendor");
                return "redirect:/vendor-dashboard";
            }
        } else if (credential == 0) {
            logger.error("Invalid credentials for user: {}", email);
            // Display error message
            model.addAttribute("error", "Invalid password. Try again.");
            return "login";
        }

        logger.error("User not found: {}", email);
        // Display error message
        model.addAttribute("error", "User not found");
        return "login";
    }

}
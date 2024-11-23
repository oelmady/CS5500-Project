package com.saleshalal.SEProject.controller;

import com.saleshalal.SEProject.data.CustomerDTO;
import com.saleshalal.SEProject.data.VendorDTO;
import com.saleshalal.SEProject.service.UserService;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.security.Principal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private Model model;

    @Mock
    private BindingResult bindingResult;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void showLoginForm_ReturnsLoginView() {
        String viewName = authController.showLoginForm();

        assertEquals("login", viewName);
    }

    @Test
    void showCustomerRegistration_ReturnsCustomerRegistrationView() {
        String viewName = authController.showCustomerRegistration(model);

        assertEquals("customer/customer-registration", viewName);
        verify(model, times(1)).addAttribute(eq("customerDTO"), any(CustomerDTO.class));
    }

    @Test
    void registerCustomer_ValidCustomer_RedirectsToLogin() {
        CustomerDTO customerDTO = new CustomerDTO();
        when(bindingResult.hasErrors()).thenReturn(false);

        String viewName = authController.registerCustomer(customerDTO, bindingResult, model);

        assertEquals("redirect:/login", viewName);
        verify(userService, times(1)).registerCustomer(customerDTO);
    }

    @Test
    void registerCustomer_InvalidCustomer_ReturnsCustomerRegistrationView() {
        CustomerDTO customerDTO = new CustomerDTO();
        when(bindingResult.hasErrors()).thenReturn(true);

        String viewName = authController.registerCustomer(customerDTO, bindingResult, model);

        assertEquals("customer/customer-registration", viewName);
        verify(userService, times(0)).registerCustomer(any(CustomerDTO.class));
    }

    @Test
    void registerCustomer_ExceptionThrown_ReturnsCustomerRegistrationView() {
        CustomerDTO customerDTO = new CustomerDTO();
        when(bindingResult.hasErrors()).thenReturn(false);
        doThrow(new RuntimeException("Error")).when(userService).registerCustomer(customerDTO);

        String viewName = authController.registerCustomer(customerDTO, bindingResult, model);

        assertEquals("customer/customer-registration", viewName);
        verify(model, times(1)).addAttribute(eq("error"), anyString());
    }

    @Test
    void showVendorRegistration_ReturnsVendorRegistrationView() {
        String viewName = authController.showVendorRegistration(model);

        assertEquals("vendor/vendor-registration", viewName);
        verify(model, times(1)).addAttribute(eq("vendorDTO"), any(VendorDTO.class));
    }

    @Test
    void registerVendor_ValidVendor_RedirectsToLogin() {
        VendorDTO vendorDTO = new VendorDTO();
        when(bindingResult.hasErrors()).thenReturn(false);

        String viewName = authController.registerVendor(vendorDTO, bindingResult, model);

        assertEquals("redirect:/login", viewName);
        verify(userService, times(1)).registerVendor(vendorDTO);
    }

    @Test
    void registerVendor_InvalidVendor_ReturnsVendorRegistrationView() {
        VendorDTO vendorDTO = new VendorDTO();
        when(bindingResult.hasErrors()).thenReturn(true);

        String viewName = authController.registerVendor(vendorDTO, bindingResult, model);

        assertEquals("vendor/vendor-registration", viewName);
        verify(userService, times(0)).registerVendor(any(VendorDTO.class));
    }

    @Test
    void registerVendor_ExceptionThrown_ReturnsVendorRegistrationView() {
        VendorDTO vendorDTO = new VendorDTO();
        when(bindingResult.hasErrors()).thenReturn(false);
        doThrow(new RuntimeException("Error")).when(userService).registerVendor(vendorDTO);

        String viewName = authController.registerVendor(vendorDTO, bindingResult, model);

        assertEquals("vendor/vendor-registration", viewName);
        verify(model, times(1)).addAttribute(eq("error"), anyString());
    }

    /**
     * Test that a user with a valid customer registration logs in and is redirected to the
     * customer dashboard.
     */
    @Test
    void login_Customer_RedirectsToCustomerDashboard() {
        String email = "customer@example.com";
        String password = "password";

        when(userService.validateUser(email, password)).thenReturn(1);
        when(userService.isCustomer(email)).thenReturn(true);

        String viewName = authController.login(email, password, model);

        assertEquals("redirect:/customer-dashboard", viewName);
        verify(userService, times(1)).validateUser(email, password);
        verify(userService, times(1)).isCustomer(email);
    }

    /**
     * Test that a user with a valid vendor registration logs in and is redirected to the vendor dashboard.
     */
    @Test
    void login_Vendor_RedirectsToVendorDashboard() {
        // Arrange email and password
        String email = "email@example.com";
        String password = "password";

        // Mock the validateUser method to return 1
        when(userService.validateUser(email, password)).thenReturn(1);

        // Mock the isVendor method to return true
        when(userService.isVendor(email)).thenReturn(true);

        // Call the login post method
        String viewName = authController.login(email, password, model);

        // Assert that the user is redirected to the vendor dashboard
        assertEquals("redirect:/vendor-dashboard", viewName);
    }

    /**
     * Test that a user with invalid credentials is redirected to the login page.
     * The user is not redirected to the customer or vendor dashboard.
     */
    @Test
    void login_InvalidUser_ThrowsResourceNotFoundException() {
        // Arrange email and password
        String email = "email@example.com";
        String password = "password";

        // Mock the validateUser method to return 0
        when(userService.validateUser(email, password)).thenReturn(0);

        // Call the login post method
        String viewName = authController.login(email, password, model);

        // Assert that the user is redirected to the login page
        assertEquals("login", viewName);
    }
}
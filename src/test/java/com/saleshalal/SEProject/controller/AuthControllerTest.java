package com.saleshalal.SEProject.controller;

import com.saleshalal.SEProject.data.CustomerDTO;
import com.saleshalal.SEProject.data.VendorDTO;
import com.saleshalal.SEProject.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

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

        assertEquals("redirect:/login?registered=true", viewName);
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

        assertEquals("redirect:/login?registered=true", viewName);
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
}
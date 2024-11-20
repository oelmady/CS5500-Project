package com.saleshalal.SEProject.service;

import com.saleshalal.SEProject.data.CustomerDTO;
import com.saleshalal.SEProject.data.VendorDTO;
import com.saleshalal.SEProject.model.Customer;
import com.saleshalal.SEProject.model.UserRole;
import com.saleshalal.SEProject.model.Vendor;
import com.saleshalal.SEProject.repository.CustomerRepository;
import com.saleshalal.SEProject.repository.VendorRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

/**
 * Test user login and registration functionality using DTOs.
 */
class UserServiceTest {
    @InjectMocks
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private VendorRepository vendorRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Tests that a customer can be registered.
     */
    @Test
    void registerCustomer() {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setEmail("test@example.com");
        customerDTO.setPassword("password");

        when(customerRepository.findByEmail(customerDTO.getEmail())).thenReturn(Optional.empty());

        userService.registerCustomer(customerDTO);

        verify(customerRepository).save(any(Customer.class));
    }

    /**
     * Tests that a vendor can be registered.
     */
    @Test
    void registerVendor() {
        VendorDTO vendorDTO = new VendorDTO();
        vendorDTO.setEmail("hi@example.com");
        vendorDTO.setPassword("password");

        // Mock that the vendor does not exist
        when(vendorRepository.findByEmail(vendorDTO.getEmail())).thenReturn(Optional.empty());

        userService.registerVendor(vendorDTO);

        verify(vendorRepository).save(any(Vendor.class));
    }

    /**
     * Tests that a user can be loaded by email.
     */
    @Test
    void loadUserByUsername() {
        String email = "test@example.com";
        Customer customer = new Customer();
        customer.setEmail(email);
        customer.setPassword("password");
        customer.setRole(UserRole.CUSTOMER);

        // Mock that the customer exists
        when(customerRepository.findByEmail(email)).thenReturn(Optional.of(customer));

        UserDetails userDetails = userService.loadUserByUsername(email);

        // Check that the user details are correct
        assertNotNull(userDetails);
        assertEquals(email, userDetails.getUsername());
        verify(customerRepository).findByEmail(email);
    }

    /**
     * Tests that a UsernameNotFoundException is thrown when the user is not found.
     */
    @Test
    void loadUserByUsername_UserNotFound() {
        String email = "notfound@example.com";

        // Mock that the user does not exist
        when(customerRepository.findByEmail(email)).thenReturn(Optional.empty());
        when(vendorRepository.findByEmail(email)).thenReturn(Optional.empty());

        // Check that a UsernameNotFoundException is thrown
        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername(email));
        verify(customerRepository).findByEmail(email);
        verify(vendorRepository).findByEmail(email);
    }
}
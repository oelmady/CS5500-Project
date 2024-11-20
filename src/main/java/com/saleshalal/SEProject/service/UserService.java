package com.saleshalal.SEProject.service;

import com.saleshalal.SEProject.data.CustomerDTO;
import com.saleshalal.SEProject.data.VendorDTO;
import com.saleshalal.SEProject.model.Customer;
import com.saleshalal.SEProject.model.AUser;
import com.saleshalal.SEProject.model.UserRole;
import com.saleshalal.SEProject.model.Vendor;
import com.saleshalal.SEProject.repository.CustomerRepository;

import com.saleshalal.SEProject.repository.VendorRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service class for managing user login and registration.
 */
@Service
@Transactional
public class UserService implements UserDetailsService {

    private final CustomerRepository customerRepository;
    private final VendorRepository vendorRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(CustomerRepository customerRepository,
                       VendorRepository vendorRepository,
                       PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.vendorRepository = vendorRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Registers a new customer.
     *
     * @param customerDTO the customer data to register
     * @throws RuntimeException if the email is already registered
     */
    public void registerCustomer(CustomerDTO customerDTO) {
        if (customerRepository.findByEmail(customerDTO.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered");
        }
        customerDTO.setPassword(passwordEncoder.encode(customerDTO.getPassword()));
        Customer customer = new Customer(customerDTO);
        customerRepository.save(customer);
    }

    /**
     * Registers a new vendor.
     *
     * @param vendorDTO the vendor data to register
     * @throws RuntimeException if the email is already registered
     */
    public void registerVendor(VendorDTO vendorDTO) {
        if (vendorRepository.findByEmail(vendorDTO.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered");
        }
        vendorDTO.setPassword(passwordEncoder.encode(vendorDTO.getPassword()));
        Vendor vendor = new Vendor(vendorDTO);
        vendorRepository.save(vendor);
    }

    /**
     * Loads a user by email.
     *
     * @param email the email to load the user by
     * @return the user details
     * @throws UsernameNotFoundException if the user is not found
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Check if the user is a customer
        Optional<Customer> customer = customerRepository.findByEmail(email);
        if (customer.isPresent()) {
            return buildUserDetails(customer.get());
        }

        // Check if the user is a vendor
        Optional<Vendor> vendor = vendorRepository.findByEmail(email);
        if (vendor.isPresent()) {
            return buildUserDetails(vendor.get());
        }

        throw new UsernameNotFoundException("User not found");
    }

    /**
     * Builds UserDetails object from AUser.
     *
     * @param user the user to build details for
     * @return the UserDetails object
     */
    private UserDetails buildUserDetails(AUser user) {
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .build();
    }
}


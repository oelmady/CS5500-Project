package com.saleshalal.SEProject.service;

import com.saleshalal.SEProject.model.Customer;
import com.saleshalal.SEProject.model.User;
import com.saleshalal.SEProject.model.UserRole;
import com.saleshalal.SEProject.model.Vendor;
import com.saleshalal.SEProject.repository.CustomerRepository;
import com.saleshalal.SEProject.repository.UserRepository;

import com.saleshalal.SEProject.repository.VendorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final VendorRepository vendorRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository,
                       CustomerRepository customerRepository,
                       VendorRepository vendorRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.customerRepository = customerRepository;
        this.vendorRepository = vendorRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void registerCustomer(Customer customer) {
        if (userRepository.findByEmail(customer.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered");
        }

        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        customer.setRole(UserRole.CUSTOMER);
        customerRepository.save(customer);
    }

    public void registerVendor(Vendor vendor) {
        if (userRepository.findByEmail(vendor.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered");
        }

        // Add vendor-specific validation
        if (vendor.getBusinessName() == null || vendor.getBusinessRegistration() == null) {
            throw new RuntimeException("Business details are required");
        }

        vendor.setPassword(passwordEncoder.encode(vendor.getPassword()));
        vendor.setRole(UserRole.VENDOR);
        vendorRepository.save(vendor);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .build();
    }
}
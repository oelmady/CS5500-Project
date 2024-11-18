package com.saleshalal.SEProject.service;

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

    public void registerCustomer(Customer customer) {
        if (customerRepository.findByEmail(customer.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered");
        }

        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        customer.setRole(UserRole.CUSTOMER);
        customerRepository.save(customer);
    }

    public void registerVendor(Vendor vendor) {
        if (vendorRepository.findByEmail(vendor.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered");
        }

        if (vendor.getBusinessName() == null || vendor.getBusinessNumber() == null) {
            throw new RuntimeException("Business details are required");
        }

        vendor.setPassword(passwordEncoder.encode(vendor.getPassword()));
        vendor.setRole(UserRole.VENDOR);
        vendorRepository.save(vendor);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Customer> customer = customerRepository.findByEmail(email);
        if (customer.isPresent()) {
            return buildUserDetails(customer.get());
        }

        Optional<Vendor> vendor = vendorRepository.findByEmail(email);
        if (vendor.isPresent()) {
            return buildUserDetails(vendor.get());
        }

        throw new UsernameNotFoundException("User not found");
    }

    private UserDetails buildUserDetails(AUser user) {
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .build();
    }
}


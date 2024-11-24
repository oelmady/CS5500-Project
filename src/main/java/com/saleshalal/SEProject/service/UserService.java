package com.saleshalal.SEProject.service;

import com.saleshalal.SEProject.config.CustomLogger;
import com.saleshalal.SEProject.data.CustomerDTO;
import com.saleshalal.SEProject.data.VendorDTO;
import com.saleshalal.SEProject.model.Customer;
import com.saleshalal.SEProject.model.AUser;
import com.saleshalal.SEProject.model.UserRole;
import com.saleshalal.SEProject.model.Vendor;
import com.saleshalal.SEProject.repository.CustomerRepository;

import com.saleshalal.SEProject.repository.VendorRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
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

    private final CustomLogger logger = new CustomLogger(UserService.class);
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
        logger.info("Registering new customer: {}", customerDTO.getEmail());
        if (customerRepository.findByEmail(customerDTO.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered");
        }
        logger.info("Success! Registering new customer: {}", customerDTO.getEmail());
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
        logger.info("Registering new vendor: {}", vendorDTO.getEmail());
        if (vendorRepository.findByEmail(vendorDTO.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered");
        }
        logger.info("Success! Registering vendor: {}", vendorDTO.getEmail());
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
        logger.info("Loading user by email: {}", email);
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
        logger.error("User not found: {}", email);
        throw new UsernameNotFoundException("User not found");
    }

    /**
     * Builds UserDetails object from AUser.
     *
     * @param user the user to build details for
     * @return the UserDetails object
     */
    private UserDetails buildUserDetails(AUser user) {
        return User.withUsername(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .build();
    }

    /**
     * Checks if a user is a customer.
     *
     * @param email the email to check
     * @return true if the user is registered as a customer
     */
    public boolean isCustomer(String email) {
        // check customer repository
        return customerRepository.findByEmail(email).isPresent();
    }

    /**
     * Checks if a user is a vendor.
     *
     * @param email the email to check
     * @return true if the user is registered as a vendor
     */
    public boolean isVendor(String email) {
        // check vendor repository
        return vendorRepository.findByEmail(email).isPresent();
    }

    /**
     * Validates a user's credentials for login. Returns coded values for different scenarios.
     *
     * @param email    the input email
     * @param password the input password
     * @return 1 if the user is valid, 0 if the password is incorrect, -1 if the user is not found
     */
    public int validateUser(String email, String password) {
        logger.info("Validating user: {}", email);
        Optional<Customer> customer = customerRepository.findByEmail(email);
        if (customer.isPresent()) {
            logger.info("Customer found: {}", email);
            return passwordEncoder.matches(password, customer.get().getPassword()) ? 1 : 0;
        }
        Optional<Vendor> vendor = vendorRepository.findByEmail(email);
        if (vendor.isPresent()) {
            logger.info("Vendor found: {}", email);
            return passwordEncoder.matches(password, vendor.get().getPassword()) ? 1 : 0;
        }
        logger.error("User not found: {}", email);
        return -1;
    }

}
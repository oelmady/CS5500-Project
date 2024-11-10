package com.saleshalal.SEProject.service;

import com.saleshalal.SEProject.model.UserModel;
import com.saleshalal.SEProject.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

// todo: extend UserDetailsService for spring security auth
@Service
public class UserService {
  private final UserRepository userRepository;

  @Autowired
  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

  public UserModel registerUser(UserModel user) {
    String encodedPassword = passwordEncoder.encode(user.getPassword());
    // not using encoded password yet
    user.setPassword(user.getPassword());
    user.setUsername(user.getUsername());
    return this.userRepository.save(user);
  }

// Todo: validate the email, we will need to use regex
public boolean isValidEmail(String email) {
    String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
    return email.matches(emailRegex);
}

  // Todo: validate the email, we will need to use regex
  // https://www.baeldung.com/registration-with-spring-mvc-and-spring-security

  public boolean validateLogin(String username, String password) {
    UserModel user = userRepository.findByUsername(username);
    return user != null && password.equals(user.getPassword());
  }

}
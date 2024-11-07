package com.saleshalal.SEProject.service;

import com.saleshalal.SEProject.model.UserModel;
import com.saleshalal.SEProject.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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
    user.setEmail(user.getEmail());
    return this.userRepository.save(user);
  }

  public boolean validateLogin(String email, String password) {
    UserModel user = userRepository.findByEmail(email);
    return user != null && password.equals(user.getPassword());
  }


//  @Override
//  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//    UserModel user = userRepository.findByEmail(email);
//
//    if (user == null) {
//      throw new UsernameNotFoundException("User not found");
//    }
//
//    return org.springframework.security.core.userdetails.User
//            .withUsername(user.getEmail())
//            .password(user.getPassword())
//            .build();
//  }
}
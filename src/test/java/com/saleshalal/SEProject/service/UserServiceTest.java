package com.saleshalal.SEProject.service;

import com.saleshalal.SEProject.model.UserModel;
import com.saleshalal.SEProject.repository.UserRepository;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {

  @MockBean
  private UserRepository userRepository;

  @Autowired
  private UserService userService;

  String testEmail = "test@email.com";
  String testPassword = "testPassword";

  // Test registered account enters database and can be retrieved
  @Test
  void testRegisterUser_Success() {
    UserModel user = new UserModel();
    // attempt to register by setting email and password
  }

  @Test
  void testValidateCredentials_Success() {
  }

  @Test
  void testValidateCredentials_Failure_InvalidPassword() {
  }


  @Test
  void testValidateCredentials_Failure_InvalidEmail() {
  }

}
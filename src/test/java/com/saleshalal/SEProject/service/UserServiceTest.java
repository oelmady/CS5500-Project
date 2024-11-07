package com.saleshalal.SEProject.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.saleshalal.SEProject.model.UserModel;
import com.saleshalal.SEProject.repository.UserRepository;

@SpringBootTest
class UserServiceTest {

  @Autowired
  private UserService userService;

  @MockBean
  private UserRepository userRepository;

  @Test
  void testRegisterUser() {
    // Arrange
    UserModel user = new UserModel();
    user.setEmail("test@example.com");
    user.setPassword("password123");

    when(userRepository.save(any(UserModel.class))).thenReturn(user);

    // Act
    UserModel registeredUser = userService.registerUser(user);

    // Assert
    assertNotNull(registeredUser);
    assertEquals("test@example.com", registeredUser.getEmail());
    assertEquals("password123", registeredUser.getPassword());  // No encoding for now
    verify(userRepository, times(1)).save(user);
  }

  @Test
  void testValidateLogin_Success() {
    // Arrange
    String email = "test@example.com";
    String password = "password123";

    UserModel user = new UserModel();
    user.setEmail(email);
    user.setPassword(password);

    when(userRepository.findByEmail(email)).thenReturn(user);

    // Act
    boolean isValid = userService.validateLogin(email, password);

    // Assert
    assertTrue(isValid);
    verify(userRepository, times(1)).findByEmail(email);
  }

  @Test
  void testValidateLogin_InvalidPassword() {
    // Arrange
    String email = "test@example.com";
    String password = "password123";

    UserModel user = new UserModel();
    user.setEmail(email);
    user.setPassword("wrongPassword");

    when(userRepository.findByEmail(email)).thenReturn(user);

    // Act
    boolean isValid = userService.validateLogin(email, password);

    // Assert
    assertFalse(isValid);
    verify(userRepository, times(1)).findByEmail(email);
  }

  @Test
  void testValidateLogin_UserNotFound() {
    // Arrange
    String email = "test@example.com";
    String password = "password123";

    when(userRepository.findByEmail(email)).thenReturn(null);

    // Act
    boolean isValid = userService.validateLogin(email, password);

    // Assert
    assertFalse(isValid);
    verify(userRepository, times(1)).findByEmail(email);
  }
}
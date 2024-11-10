package com.saleshalal.SEProject.controller;

import com.saleshalal.SEProject.model.UserModel;
import com.saleshalal.SEProject.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerTest {
    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;


    // Test case for showing the registration form
    @Test
    public void testShowRegistrationForm() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeExists("user"));
    }

    // Test case for registering a user successfully
    @Test
    public void testRegisterUser_Success() throws Exception {
        // Mock user registration
        when(userService.registerUser(any(UserModel.class))).thenReturn(new UserModel());

        mockMvc.perform(post("/register")
                        .param("email", "test@example.com")
                        .param("password", "password123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/auth/login"));
    }

    // Test case for showing the login form
    @Test
    public void testShowLoginForm() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    // Test case for successful login
    @Test
    public void testLoginUser_Success() throws Exception {
        when(userService.validateLogin("test@example.com", "password123")).thenReturn(true);

        mockMvc.perform(post("/login")
                        .param("email", "test@example.com")
                        .param("password", "password123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/index"));
    }

    // Test case for unsuccessful login
    @Test
    public void testLoginUser_Failure() throws Exception {
        when(userService.validateLogin("test@example.com", "wrongpassword")).thenReturn(false);

        mockMvc.perform(post("/login")
                        .param("email", "test@example.com")
                        .param("password", "wrongpassword"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attributeExists("error"))
                .andExpect(model().attribute("error", "Invalid credentials"));
    }
}

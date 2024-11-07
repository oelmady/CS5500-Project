package com.saleshalal.SEProject.controller;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.saleshalal.SEProject.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    public void testLoginView_NewUser() throws Exception{
        // new users should be able to view the login page
        mockMvc.perform(get("/auth/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    @WithMockUser(username = "test@example.com", password = "password")
    public void testLoginView_ExistingUser() throws Exception {
        // Users should be able to view the login page
        mockMvc.perform(get("/auth/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    public void testRegisterView_NewUser() throws Exception {
        // New users should be able to view the registration page
        mockMvc.perform(get("/auth/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"));
    }

    @Test
    @WithMockUser(username = "test@example.com", password = "password")
    public void testRegisterView_ExistingUser() throws Exception {
        // Users should be able to view the registration page
        mockMvc.perform(get("/auth/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"));
    }

    // Todo: implement functionality to redirect existing email to login
//    @Test
//    @WithMockUser(username = "test@example.com", password = "password123")
//    public void testUserRegistration_ExistingAccount() throws Exception {
//        // If the user has an account, redirect to
//        mockMvc.perform(post("/auth/register")
//                        .param("email", "testuser@example.com")
//                        .param("password", "password123"))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl("/auth/login"));
//    }

    @Test
    public void testUserRegistration_Success(){

    }

    @Test
    public void testUserLogin_Success() throws Exception {
        // Mock the UserService to return a user when validating credentials
        String email = "testuser@example.com";
        String password = "password123";


        when(userService.validateLogin(email, password)).thenReturn(true);

        // Simulate redirect after logging in with valid credentials
        mockMvc.perform(post("/auth/login")
                .param("username", email)
                .param("password", password))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/index"));
    }

    @Test
    public void testUserLogin_InvalidCredentials(){

    }
}

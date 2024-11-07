package com.saleshalal.SEProject.controller;

import com.saleshalal.SEProject.model.UserModel;
import com.saleshalal.SEProject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
public class UserController {
// todo: update the html files to be more informative

  private final UserService userService;

  @Autowired
  public UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping("/register")
  public String showRegistrationForm(Model model) {
    model.addAttribute("user", new UserModel());
    return "register";
  }

  @PostMapping("/register")
  public String registerUser(@ModelAttribute("user") UserModel user) {
    userService.registerUser(user);
    return "redirect:/auth/login";
  }

  @GetMapping("/login")
  public String showLoginForm() {
    return "login";
  }

  @PostMapping("/login")
  public String loginUser(@RequestParam String email, @RequestParam String password, Model model) {
    if (userService.validateLogin(email, password)) {
      return "redirect:/index"; // no home page yet
    }
    model.addAttribute("error", "Invalid credentials");
    return "login";
  }
}
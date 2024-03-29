package com.gfa.programmersfoxclub.controller;

import com.gfa.programmersfoxclub.model.character.Fox;
import com.gfa.programmersfoxclub.model.user.User;
import com.gfa.programmersfoxclub.service.Logger;
import com.gfa.programmersfoxclub.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegisterController {
  private final UserService userService;
  private Logger logger;

  public RegisterController(UserService userService, Logger logger) {
    this.userService = userService;
    this.logger = logger;
  }

  @GetMapping("/register")
  public String renderRegistrationForm(Model model) {
    return "register/form";
  }

  @PostMapping("/register")
  public String createUser(Model model, User user) {
    userService.createUser(user.getUsername(), user.getPassword(), user.getEmail());
    model.addAttribute("fox", new Fox());
    model.addAttribute("user", userService.getLoggedInUser());
    model.addAttribute("actionHistoryLogger", logger);
    logger.saveLoginAction();
    return "redirect:/fox/create";
  }
}

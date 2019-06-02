package com.gfa.programmersfoxclub.controller;

import com.gfa.programmersfoxclub.model.user.User;
import com.gfa.programmersfoxclub.service.IUserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {
  private final IUserService userService;

  public LoginController(IUserService userService) {
    this.userService = userService;
  }

  @GetMapping("/login")
  public String renderLoginForm(){
    return "login/form";
  }

  @PostMapping("/login")
  public String loginUser(User user) {
    if(userService.validateAndLoginUser(user)) {
      return "redirect:/";
    }
    userService.createUser(user.getUserName(), user.getPassword());
    return "redirect:/";
  }

  @GetMapping("/logout")
  public String logut() {
    userService.logout();
    return "redirect:/login";
  }
}

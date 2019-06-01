package com.gfa.programmersfoxclub.controller;


import com.gfa.programmersfoxclub.model.user.User;
import com.gfa.programmersfoxclub.service.IFoxService;
import com.gfa.programmersfoxclub.service.IUserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
  private IFoxService foxService;
  private IUserService userService;

  public HomeController(IFoxService foxService, IUserService userService) {
    this.foxService = foxService;
    this.userService = userService;
  }

  @GetMapping("/")
  public void renderIndex() {
  }

  @GetMapping("/login")
  public String renderLogin(Model model) {
    model.addAttribute("fox", new User());
    return "create_fox";
  }
}

package com.gfa.programmersfoxclub.controller;


import com.gfa.programmersfoxclub.model.character.Fox;
import com.gfa.programmersfoxclub.service.IFoxService;
import com.gfa.programmersfoxclub.service.IUserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FoxController {
  private IFoxService foxService;
  private IUserService userService;

  public FoxController(IFoxService foxService, IUserService userService) {
    this.foxService = foxService;
    this.userService = userService;
  }

  @GetMapping("/create_fox")
  public String renderCreateFox(Model model) {
    model.addAttribute("fox", new Fox());
    return "create_fox";
  }
}

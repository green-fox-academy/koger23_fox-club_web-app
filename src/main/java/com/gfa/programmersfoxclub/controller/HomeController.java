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
  public String renderIndex(Model model) {
    User user = userService.getLoggedInUser();
    if(user == null) {
      return "redirect:/login";
    }
    model.addAttribute("fox", foxService.findFoxByOwner(userService.getLoggedInUser()));
    model.addAttribute("user", userService.getLoggedInUser());
    return "index";
  }
}

package com.gfa.programmersfoxclub.controller;


import com.gfa.programmersfoxclub.model.character.Fox;
import com.gfa.programmersfoxclub.model.trick.Trick;
import com.gfa.programmersfoxclub.model.user.User;
import com.gfa.programmersfoxclub.service.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@Getter
@Setter
public class HomeController {
  private FoxService foxService;
  private UserService userService;
  private Logger logger;
  private SessionService sessionService;
  private TrickService trickRepository;
  private NutritionService nutritionService;

  public HomeController(FoxService foxService, UserService userService, Logger logger, SessionService sessionService, TrickService trickRepository, NutritionService nutritionService) {
    this.foxService = foxService;
    this.userService = userService;
    this.logger = logger;
    this.sessionService = sessionService;
    this.trickRepository = trickRepository;
    this.nutritionService = nutritionService;
  }

  @GetMapping("/")
  public String renderIndex(Model model) {
    User user = userService.getLoggedInUser();
    if (user == null) {
      return "redirect:/login";
    }
    long activeFoxId = user.getFoxList().get(user.getActiveFoxIndex()).getId();
    model.addAttribute("fox", foxService.findById(activeFoxId));
    model.addAttribute("user", user);
    model.addAttribute("actionHistoryLogger", logger);
    sessionService.updateFoxAndNutrition();
    return "index";
  }

  @GetMapping("/actionhistory")
  public String actionHistory(Model model) {
    if (userService.getLoggedInUser() == null) {
      return "redirect:/login";
    }
    model.addAttribute("fox", userService.getLoggedInUser().getFoxList().get(userService.getLoggedInUser().getActiveFoxIndex()));
    model.addAttribute("actionHistoryLogger", logger);
    return "actionhistory";
  }

  @GetMapping("/trickcenter")
  public String trickCenter(Model model) {
    if (userService.getLoggedInUser() == null) {
      return "redirect:/login";
    }
    Fox activeFox = userService.getLoggedInUser().getFoxList().get(userService.getLoggedInUser().getActiveFoxIndex());
    model.addAttribute("fox", activeFox);
    model.addAttribute("tricklist", trickRepository.findAll());
    return "trickcenter";
  }

  @PostMapping("/tricksave")
  public String saveTrick(Model model, @RequestParam("trick") Trick trick) {
    Fox fox = userService.getLoggedInUser().getFoxList().get(userService.getLoggedInUser().getActiveFoxIndex());
    if (fox.getTrick_list().stream()
            .filter(trick1 -> trick1.getName().equals(trick.getName())).count() == 0) {
      fox.getTrick_list().add(trick);
      logger.saveTrickAction(trick.getName());
      foxService.save(fox);
    }
    model.addAttribute("fox", fox);
    return "redirect:/";
  }
}

package com.gfa.programmersfoxclub.controller;


import com.gfa.programmersfoxclub.model.character.Fox;
import com.gfa.programmersfoxclub.model.nutrition.Drink;
import com.gfa.programmersfoxclub.model.nutrition.Food;
import com.gfa.programmersfoxclub.model.trick.Trick;
import com.gfa.programmersfoxclub.model.user.User;
import com.gfa.programmersfoxclub.repository.NutritionRepository;
import com.gfa.programmersfoxclub.repository.TrickRepository;
import com.gfa.programmersfoxclub.service.IFoxService;
import com.gfa.programmersfoxclub.service.ISessionService;
import com.gfa.programmersfoxclub.service.IUserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class FoxController {
  private IFoxService foxService;
  private IUserService userService;
  private NutritionRepository nutritionRepository;
  private TrickRepository trickRepository;
  private ISessionService sessionService;

  public FoxController(IFoxService foxService, IUserService userService, NutritionRepository nutritionRepository, TrickRepository trickRepository, ISessionService sessionService) {
    this.foxService = foxService;
    this.userService = userService;
    this.nutritionRepository = nutritionRepository;
    this.trickRepository = trickRepository;
    this.sessionService = sessionService;
  }

  @GetMapping("/create_fox")
  public String renderCreateFox(Model model) {
    User user = userService.getLoggedInUser();
    if(user == null) {
      return "redirect:/login";
    }
    model.addAttribute("fox", new Fox());
    return "create_fox";
  }

  @PostMapping("/create_fox")
  public String createFox(Model model, Fox fox) {
    User user = userService.getLoggedInUser();
    if(user == null) {
      return "redirect:/login";
    }
    fox.setOwner(user);
    Food defaultFood = new Food();
    Drink defaultDrink = new Drink();
    nutritionRepository.save(defaultFood);
    nutritionRepository.save(defaultDrink);
    fox.setFood(defaultFood);
    fox.setDrink(defaultDrink);

    Trick default_trick = new Trick();
    trickRepository.save(default_trick);
    fox.getTrick_list().add(default_trick);

    foxService.save(fox);
    userService.updateUsersActiveFoxIndex(userService.getLoggedInUser().getFoxList().size() - 1);
    model.addAttribute(fox);
    model.addAttribute("user", user);
    return "redirect:/";
  }

  @GetMapping("/nutritionstore")
  public String nutritionStore(Model model) {
    User user = userService.getLoggedInUser();
    if(user == null) {
      return "redirect:/login";
    }
    model.addAttribute("fox", userService.getLoggedInUser().getFoxList().get(userService.getLoggedInUser().getActiveFoxIndex()));
    return "nutritionstore";
  }

  @PostMapping("/nutritionsave")
  public String saveNutritions(Model model, @RequestParam("food") String food,
                               @RequestParam("drink") String drink) {
    sessionService.saveNutrition(food, drink);
    model.addAttribute("fox", userService.getLoggedInUser().getFoxList().get(userService.getLoggedInUser().getActiveFoxIndex()));
    return "redirect:/";
  }

  @GetMapping("/fox/select")
  public String renderFoxList(Model model) {
    List<Fox> foxList = userService.getLoggedInUser().getFoxList();
    model.addAttribute("foxlist", foxList);
    return "foxlist";
  }
  @PostMapping("/fox/select")
  public String setActiveFox(@RequestParam("activefox") int activefox) {
    User user = userService.getLoggedInUser();
    userService.updateUsersActiveFoxIndex(activefox - 1);
    return "redirect:/";
  }
}

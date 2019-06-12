package com.gfa.programmersfoxclub.controller;


import com.gfa.programmersfoxclub.model.character.Fox;
import com.gfa.programmersfoxclub.model.nutrition.Drink;
import com.gfa.programmersfoxclub.model.nutrition.Food;
import com.gfa.programmersfoxclub.model.nutrition.Nutrition;
import com.gfa.programmersfoxclub.model.trick.Trick;
import com.gfa.programmersfoxclub.model.user.User;
import com.gfa.programmersfoxclub.repository.NutritionRepository;
import com.gfa.programmersfoxclub.repository.TrickRepository;
import com.gfa.programmersfoxclub.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/fox")
public class FoxController {
  private FoxService foxService;
  private UserService userService;
  private NutritionRepository nutritionRepository;
  private TrickRepository trickRepository;
  private SessionService sessionService;
  private NutritionService nutritionService;
  private HealthService healthService;

  public FoxController(FoxService foxService, UserService userService, NutritionRepository nutritionRepository, TrickRepository trickRepository, SessionService sessionService, NutritionService nutritionService, HealthService healthService) {
    this.foxService = foxService;
    this.userService = userService;
    this.nutritionRepository = nutritionRepository;
    this.trickRepository = trickRepository;
    this.sessionService = sessionService;
    this.nutritionService = nutritionService;
    this.healthService = healthService;
  }

  @GetMapping("/create")
  public String renderCreateFox(Model model) {
    User user = userService.getLoggedInUser();
    if (user == null) {
      return "redirect:/login";
    }
    model.addAttribute("fox", new Fox());
    return "create_fox";
  }

  @PostMapping("/create")
  public String createFox(Model model, Fox fox) {
    User user = userService.getLoggedInUser();
    if (user == null) {
      return "redirect:/login";
    }
    fox.setOwner(user);
    user.getFoxList().add(fox);
    Food defaultFood = nutritionRepository.findFoodById(7L);
    Drink defaultDrink = nutritionRepository.findDrinkById(1L);
    Trick default_trick = trickRepository.findTrickById(1);
    fox.setFood(defaultFood);
    fox.setDrink(defaultDrink);
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
    if (user == null) {
      return "redirect:/login";
    }
    List<Nutrition> nutritionList = new ArrayList<>();
    nutritionRepository.findAll().forEach(nutritionList::add);
    model.addAttribute("fox", userService.getLoggedInUser().getFoxList().get(userService.getLoggedInUser().getActiveFoxIndex()));
    model.addAttribute("foodList", nutritionList.stream()
            .filter(nutrition -> nutrition instanceof Food)
            .collect(Collectors.toList()));
    model.addAttribute("drinkList", nutritionList.stream()
            .filter(nutrition -> nutrition instanceof Drink)
            .collect(Collectors.toList()));
    return "nutritionstore";
  }

  @PostMapping("/feed")
  public String updateFood(Model model, @RequestParam("food") String foodName) {
    nutritionService.saveNutrition(foodName);
    model.addAttribute("fox", userService.getLoggedInUser().getFoxList().get(userService.getLoggedInUser().getActiveFoxIndex()));
    return "redirect:/";
  }

  @PostMapping("/drink")
  public String updateDrink(Model model, @RequestParam("drink") String drinkName) {
    nutritionService.saveNutrition(drinkName);
    model.addAttribute("fox", userService.getLoggedInUser().getFoxList().get(userService.getLoggedInUser().getActiveFoxIndex()));
    return "redirect:/";
  }

  @GetMapping("/select")
  public String renderFoxList(Model model) {
    List<Fox> foxList = userService.getLoggedInUser().getFoxList();
    model.addAttribute("foxlist", foxList);
    return "foxlist";
  }

  @PostMapping("/select")
  public String setActiveFox(@RequestParam("activefox") int activefox) {
    User user = userService.getLoggedInUser();
    userService.updateUsersActiveFoxIndex(activefox - 1);
    return "redirect:/";
  }

  @GetMapping("/heal")
  public String healFox(RedirectAttributes redirectAttributes) {
    Fox fox = foxService.findById(userService.getLoggedInUser().getFoxList().get(userService.getLoggedInUser().getActiveFoxIndex()).getId());
    if (!healthService.heal(fox)) {
      if (!fox.isAlive()) {
        redirectAttributes.addFlashAttribute("healMessage", "Sorry, your fox is dead.");
      } else if (fox.getHealthPoints() == fox.getMAX_HEALTH()) {
        redirectAttributes.addFlashAttribute("healMessage", "You already healed him.");
      } else {
        redirectAttributes.addFlashAttribute("healMessage", "You cannot heal the fox! Check food / drink level.");
      }
    }
    return "redirect:/";
  }
}

package com.gfa.programmersfoxclub.controller;


import com.gfa.programmersfoxclub.model.character.Fox;
import com.gfa.programmersfoxclub.model.nutrition.Drink;
import com.gfa.programmersfoxclub.model.nutrition.Food;
import com.gfa.programmersfoxclub.model.nutrition.Nutrition;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/fox")
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

  @GetMapping("/create")
  public String renderCreateFox(Model model) {
    User user = userService.getLoggedInUser();
    if(user == null) {
      return "redirect:/login";
    }
    model.addAttribute("fox", new Fox());
    return "create_fox";
  }

  @PostMapping("/create")
  public String createFox(Model model, Fox fox) {
    User user = userService.getLoggedInUser();
    if(user == null) {
      return "redirect:/login";
    }
    fox.setOwner(user);
    user.getFoxList().add(fox);
    Food defaultFood = nutritionRepository.findFoodById(7L);
    Drink defaultDrink = nutritionRepository.findDrinkById(1L);
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

  @PostMapping("/nutritionsave")
  public String saveNutritions(Model model, @RequestParam("food") String food,
                               @RequestParam("drink") String drink) {
    sessionService.saveNutrition(food, drink);
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
}

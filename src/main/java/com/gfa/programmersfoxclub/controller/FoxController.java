package com.gfa.programmersfoxclub.controller;


import com.gfa.programmersfoxclub.model.character.Fox;
import com.gfa.programmersfoxclub.model.nutrition.Drink;
import com.gfa.programmersfoxclub.model.nutrition.Food;
import com.gfa.programmersfoxclub.model.trick.Trick;
import com.gfa.programmersfoxclub.model.user.User;
import com.gfa.programmersfoxclub.repository.NutritionRepository;
import com.gfa.programmersfoxclub.repository.TrickRepository;
import com.gfa.programmersfoxclub.service.IFoxService;
import com.gfa.programmersfoxclub.service.IUserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class FoxController {
  private IFoxService foxService;
  private IUserService userService;
  private NutritionRepository nutritionRepository;
  private TrickRepository trickRepository;

  public FoxController(IFoxService foxService, IUserService userService, NutritionRepository nutritionRepository, TrickRepository trickRepository) {
    this.foxService = foxService;
    this.userService = userService;
    this.nutritionRepository = nutritionRepository;
    this.trickRepository = trickRepository;
  }

  @GetMapping("/create_fox")
  public String renderCreateFox(Model model) {
    model.addAttribute("fox", new Fox());
    return "create_fox";
  }

  @PostMapping("/create_fox")
  public String createFox(Model model, Fox fox) {
    User user = userService.getLoggedInUser();
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
//    model.addAttribute("fox", foxService.findFoxByOwner());
    model.addAttribute("fox", fox);
    return "index";
  }
}

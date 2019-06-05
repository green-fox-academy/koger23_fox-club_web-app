package com.gfa.programmersfoxclub.service;

import com.gfa.programmersfoxclub.model.character.Fox;
import org.springframework.stereotype.Service;

@Service
public class SessionServiceImp implements ISessionService {
  private ILogger logger;
  private INutritionService nutritionService;
  private IFoxService foxService;
  private IUserService userService;

  public SessionServiceImp(INutritionService nutritionService, IFoxService foxService, IUserService userService, ILogger logger) {
    this.nutritionService = nutritionService;
    this.foxService = foxService;
    this.userService = userService;
    this.logger = logger;
  }

  public void saveNutrition(String food, String drink) {
    String foodBefore = foxService.findFoxByOwner(userService.getLoggedInUser()).getFood().getName();
    String drinkBefore = foxService.findFoxByOwner(userService.getLoggedInUser()).getDrink().getName();

    foxService.findFoxByOwner(userService.getLoggedInUser()).getFood().setName(food.toLowerCase());
    logger.saveNutritionChange(foxService.findFoxByOwner(userService.getLoggedInUser()).getFood(), foodBefore, foxService.findFoxByOwner(userService.getLoggedInUser()).getFood().getName());

    foxService.findFoxByOwner(userService.getLoggedInUser()).getDrink().setName(drink.toLowerCase());
    logger.saveNutritionChange(foxService.findFoxByOwner(userService.getLoggedInUser()).getDrink(), drinkBefore, foxService.findFoxByOwner(userService.getLoggedInUser()).getDrink().getName());
  }

  public void updateFoxAndNutrition() {
    Fox fox = foxService.findFoxByOwner(userService.getLoggedInUser());

    if (fox.isAlive()) {
      nutritionService.reduceNutritionLevel(fox);
    }
  }
}

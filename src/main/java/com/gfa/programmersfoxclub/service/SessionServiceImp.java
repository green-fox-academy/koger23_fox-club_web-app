package com.gfa.programmersfoxclub.service;

import com.gfa.programmersfoxclub.model.character.Fox;
import com.gfa.programmersfoxclub.model.nutrition.Drink;
import com.gfa.programmersfoxclub.model.nutrition.Food;
import com.gfa.programmersfoxclub.model.nutrition.Nutrition;
import com.gfa.programmersfoxclub.repository.NutritionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SessionServiceImp implements SessionService {
  private Logger logger;
  private NutritionService nutritionService;
  private FoxService foxService;
  private UserService userService;
  private NutritionRepository nutritionRepository;

  public SessionServiceImp(Logger logger, NutritionService nutritionService, FoxService foxService, UserService userService, NutritionRepository nutritionRepository) {
    this.logger = logger;
    this.nutritionService = nutritionService;
    this.foxService = foxService;
    this.userService = userService;
    this.nutritionRepository = nutritionRepository;
  }

  public void saveNutrition(String nutritionName) {
    Fox activeFox = userService.getLoggedInUser().getFoxList().get(userService.getLoggedInUser().getActiveFoxIndex());
    Nutrition nutrition = nutritionRepository.findByName(nutritionName);

    if (nutrition.getType() == Nutrition.Type.FOOD) {
      Food foodBefore = activeFox.getFood();
      Food foodAfter = nutritionRepository.findFoodByName(nutritionName);
      activeFox.setFood(foodAfter);
      logger.saveNutritionChange(activeFox.getFood(), foodBefore.getName(), activeFox.getFood().getName());
    } else if (nutrition.getType() == Nutrition.Type.DRINK) {
      Drink drinkBefore = activeFox.getDrink();
      Drink drinkAfter = nutritionRepository.findDrinkByName(nutritionName);
      activeFox.setDrink(drinkAfter);
      logger.saveNutritionChange(activeFox.getDrink(), drinkBefore.getName(), activeFox.getDrink().getName());
    }
    foxService.update(activeFox);
  }

  public void updateFoxAndNutrition() {
    List<Fox> foxList = foxService.findAll();
    for (Fox fox : foxList) {
      if (fox.isAlive()) {
        nutritionService.reduceNutritionLevel(fox);
        foxService.update(fox);
      }
    }
  }
}

package com.gfa.programmersfoxclub.service;

import com.gfa.programmersfoxclub.model.character.Fox;
import com.gfa.programmersfoxclub.model.nutrition.Drink;
import com.gfa.programmersfoxclub.model.nutrition.Food;
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

  public void saveNutrition(String food, String drink) {
    Fox activeFox = userService.getLoggedInUser().getFoxList().get(userService.getLoggedInUser().getActiveFoxIndex());
    Food foodBefore = activeFox.getFood();
    Drink drinkBefore = activeFox.getDrink();

    Food foodAfter = nutritionRepository.findFoodByName(food);
    Drink drinkAfter = nutritionRepository.findDrinkByName(drink);

    activeFox.setFood(foodAfter);
    activeFox.setDrink(drinkAfter);
    logger.saveNutritionChange(activeFox.getFood(), foodBefore.getName(), activeFox.getFood().getName());
    logger.saveNutritionChange(activeFox.getDrink(), drinkBefore.getName(), activeFox.getDrink().getName());

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

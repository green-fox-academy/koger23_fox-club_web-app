package com.gfa.programmersfoxclub.service;

import com.gfa.programmersfoxclub.model.character.Fox;
import com.gfa.programmersfoxclub.model.nutrition.Drink;
import com.gfa.programmersfoxclub.model.nutrition.Food;
import com.gfa.programmersfoxclub.model.nutrition.Nutrition;
import com.gfa.programmersfoxclub.repository.NutritionRepository;
import com.gfa.programmersfoxclub.utils.date.DateUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NutritionServiceImp implements NutritionService {
  private NutritionRepository nutritionRepository;
  private FoxService foxService;
  private UserService userService;
  private Logger logger;

  public NutritionServiceImp(NutritionRepository nutritionRepository, FoxService foxService, UserService userService, Logger logger) {
    this.nutritionRepository = nutritionRepository;
    this.foxService = foxService;
    this.userService = userService;
    this.logger = logger;
  }

  public void reduceNutritionLevel(Fox fox) {
    List<Nutrition> foxNutritionList = fox.getNutritions();
    long currentMinutes = DateUtils.getCurrentDateTimeInMinutes();
    long foodDelta = currentMinutes - fox.getLastFeedInMinutes();
    long drinkDelta = currentMinutes - fox.getLastDrinkInMinutes();
    boolean foodUpdateAble = false;
    boolean drinkUpdateAble = false;
    for (Nutrition nutrition : foxNutritionList) {
      switch (nutrition.getType()) {
        case FOOD:
          double elapsedFood = foodDelta / nutrition.getReductionTimeMinutes();
          if (elapsedFood >= 1) {
            foodUpdateAble = true;
          }
          if (fox.getLastFeedInMinutes() != 0 && foodUpdateAble) {
            for (int i = 0; i < elapsedFood; i++) {
              checkLevels(fox, nutrition);
            }
            fox.setLastFeedInMinutes(currentMinutes);
            foodUpdateAble = false;
          }
        case DRINK:
          double elapsedDrink = drinkDelta / nutrition.getReductionTimeMinutes();
          if (elapsedDrink >= 1) {
            drinkUpdateAble = true;
          }
          if (fox.getLastDrinkInMinutes() != 0 && drinkUpdateAble) {
            for (int i = 0; i < elapsedDrink; i++) {
              checkLevels(fox, nutrition);
            }
            fox.setLastDrinkInMinutes(currentMinutes);
            drinkUpdateAble = false;
          }
      }
      foxService.update(fox);
    }
  }

  public void checkLevels(Fox fox, Nutrition nutrition) {
    int beforeLevel = nutrition.getLevel();
    if (beforeLevel > 0) {
      switch (nutrition.getType()) {
        case FOOD:
          increaseHungerLevel(fox);
        case DRINK:
          increaseThirstLevel(fox);
      }
      nutrition.setLevel(beforeLevel - 1);
    } else if (beforeLevel == 0) {
      switch (nutrition.getType()) {
        case FOOD:
          increaseHungerLevel(fox);
        case DRINK:
          increaseThirstLevel(fox);
      }
    }
  }

  public void increaseThirstLevel(Fox fox) {
    if (fox.getThirstLevel() < fox.getMAX_THIRST_LEVEL() && fox.getDrink().getLevel() == 0) {
      fox.setThirstLevel(fox.getThirstLevel() + 1);
    } else if (fox.getDrink().getLevel() == 0 && fox.getThirstLevel() == fox.getMAX_THIRST_LEVEL()) {
      reduceFoxHealth(fox);
    }
  }

  public void increaseHungerLevel(Fox fox) {
    if (fox.getHungerLevel() < fox.getMAX_HUNGER_LEVEL() && fox.getFood().getLevel() == 0) {
      fox.setHungerLevel(fox.getHungerLevel() + 1);
    } else if (fox.getFood().getLevel() == 0 && fox.getHungerLevel() == fox.getMAX_HUNGER_LEVEL()) {
      reduceFoxHealth(fox);
    }
  }

  public void reduceFoxHealth(Fox fox) {
    if (fox.getHealthPoints() > 0) {
      fox.setHealthPoints(fox.getHealthPoints() - 1);
    } else {
      fox.setAlive(false);
    }
  }

  public void saveNutrition(String nutritionName) {
    Fox activeFox = userService.getLoggedInUser().getFoxList().get(userService.getLoggedInUser().getActiveFoxIndex());
    Nutrition nutrition = nutritionRepository.findByName(nutritionName);
    if (activeFox.isAlive()) {
      if (nutrition.getType() == Nutrition.Type.FOOD) {
        Food foodBefore = activeFox.getFood();
        Food foodAfter = nutritionRepository.findFoodByName(nutritionName);
        foodAfter.setLevel(foodAfter.getMAX_LEVEL());
        nutritionRepository.save(foodAfter);
        activeFox.setFood(foodAfter);
        feed(activeFox, foodAfter);
        logger.saveNutritionChange(activeFox.getFood(), foodBefore.getName(), activeFox.getFood().getName());
      } else if (nutrition.getType() == Nutrition.Type.DRINK) {
        Drink drinkBefore = activeFox.getDrink();
        Drink drinkAfter = nutritionRepository.findDrinkByName(nutritionName);
        drinkAfter.setLevel(drinkAfter.getMAX_LEVEL());
        activeFox.setDrink(drinkAfter);
        feed(activeFox, drinkAfter);
        logger.saveNutritionChange(activeFox.getDrink(), drinkBefore.getName(), activeFox.getDrink().getName());
      }
      foxService.update(activeFox);
    }
  }

  public void feed(Fox fox, Nutrition nutrition) {
    if (nutrition.getType() == Nutrition.Type.FOOD) {
      fox.setHungerLevel(nutrition.getHungerReductionPoints());
      fox.setLastFeedInMinutes(DateUtils.getCurrentDateTimeInMinutes());
      fox.setHungerLevel(0);
    } else if (nutrition.getType() == Nutrition.Type.DRINK) {
      fox.setThirstLevel(nutrition.getThirstReductionPoints());
      fox.setLastDrinkInMinutes(DateUtils.getCurrentDateTimeInMinutes());
      fox.setThirstLevel(0);
    }
    foxService.update(fox);
  }

  public void saveNutritionIfNotExists(Nutrition nutrition) {
    if (!nutritionRepository.findByNameIsNotLike(nutrition.getName())) {
      nutritionRepository.save(nutrition);
    }
  }
}

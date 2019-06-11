package com.gfa.programmersfoxclub.service;

import com.gfa.programmersfoxclub.model.character.Fox;
import com.gfa.programmersfoxclub.model.nutrition.Nutrition;
import com.gfa.programmersfoxclub.repository.NutritionRepository;
import com.gfa.programmersfoxclub.utils.date.DateUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NutritionServiceImp implements INutritionService {
  private long startMinutes;
  private long lastFoodMinutes;
  private long lastDrinkMinutes;
  private NutritionRepository nutritionRepository;

  public NutritionServiceImp(NutritionRepository nutritionRepository) {
    this.startMinutes = DateUtils.getCurrentDateTimeInMinutes();
    this.lastFoodMinutes = startMinutes;
    this.lastDrinkMinutes = startMinutes;
    this.nutritionRepository = nutritionRepository;
  }

  public void reduceNutritionLevel(Fox fox) {
    List<Nutrition> foxNutritionList = fox.getNutritions();
    long currentMinutes = DateUtils.getCurrentDateTimeInMinutes();
    long foodDelta = currentMinutes - lastFoodMinutes;
    long drinkDelta = currentMinutes - lastDrinkMinutes;
    boolean foodUpdateAble = false;
    boolean drinkUpdateAble = false;
    if (fox.getNutritions().size() > 0) {
      for (Nutrition nutrition : foxNutritionList) {
        switch (nutrition.getType()) {
          case FOOD:
            double elapsedFood = foodDelta / nutrition.getReductionTimeMinutes();
            if (elapsedFood >= 1) {
              foodUpdateAble = true;
            }
            if (lastFoodMinutes != 0 && foodUpdateAble) {
              for (int i = 0; i < elapsedFood; i++) {
                checkLevels(fox, nutrition);
              }
              lastFoodMinutes = currentMinutes;
              foodUpdateAble = false;
            }
          case DRINK:
            double elapsedDrink = drinkDelta / nutrition.getReductionTimeMinutes();
            if (elapsedDrink >= 1) {
              drinkUpdateAble = true;
            }
            if (lastDrinkMinutes != 0 && drinkUpdateAble) {
              for (int i = 0; i < elapsedDrink; i++) {
                checkLevels(fox, nutrition);
              }
              lastDrinkMinutes = currentMinutes;
              drinkUpdateAble = false;
            }
        }
      }
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

  public void feed(Fox fox, Nutrition nutrition) {
    if (fox.getFood() == nutrition) {
      fox.setHungerLevel(nutrition.getHungerReductionPoints());
    } else {
      fox.setThirstLevel(nutrition.getThirstReductionPoints());
    }
    startMinutes = DateUtils.getCurrentDateTimeInMinutes();
  }

  public void saveNutritionIfNotExists(Nutrition nutrition) {
    if (!nutritionRepository.findByNameIsNotLike(nutrition.getName())) {
      nutritionRepository.save(nutrition);
    }
  }
}

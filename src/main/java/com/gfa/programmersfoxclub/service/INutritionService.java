package com.gfa.programmersfoxclub.service;

import com.gfa.programmersfoxclub.model.character.Fox;
import com.gfa.programmersfoxclub.model.nutrition.Nutrition;


public interface INutritionService {
  void reduceNutritionLevel(Fox fox);

  void checkLevels(Fox fox, Nutrition nutrition);

  void increaseThirstLevel(Fox fox);

  void increaseHungerLevel(Fox fox);

  void reduceFoxHealth(Fox fox);

  void feed(Fox fox, Nutrition nutrition);
}

package com.gfa.programmersfoxclub.service;

import com.gfa.programmersfoxclub.model.nutrition.Nutrition;

import java.util.List;

public interface Logger {
  List<String> getLastFiveActionList();

  void setLastFiveActionList(List<String> lastFiveActionList);

  void saveLoginAction();

  void saveNutritionChange(Nutrition nutrition, String before, String after);

  void saveTrickAction(String trick);

  List<String> getActionList();

  void setActionList(List<String> actionList);

  List<String> getLastFiveAction();
}

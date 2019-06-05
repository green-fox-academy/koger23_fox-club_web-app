package com.gfa.programmersfoxclub.service;

import com.gfa.programmersfoxclub.controller.HomeController;

public interface ISessionService {
  void saveNutrition(String food, String drink);

  void updateFoxAndNutrition();
}

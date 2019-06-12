package com.gfa.programmersfoxclub.service;

import com.gfa.programmersfoxclub.model.character.Fox;
import org.springframework.stereotype.Service;

@Service
public class HealthServiceImp implements HealthService {
  private FoxService foxService;

  public HealthServiceImp(FoxService foxService) {
    this.foxService = foxService;
  }

  public void healTotally(Fox fox) {
    fox.setHealthPoints(fox.getMAX_HEALTH());
  }

  public boolean heal(Fox fox) {
    if (fox.getFood().getLevel() > 0 && fox.getDrink().getLevel() > 0 && fox.getHealthPoints() < fox.getMAX_HEALTH() && fox.isAlive()) {
      fox.setHealthPoints(fox.getMAX_HEALTH());
      foxService.save(fox);
      return true;
    }
    return false;
  }

  public void harm(Fox fox, int damage) {
    if (fox.getHealthPoints() > 0) {
      fox.setHealthPoints(fox.getHealthPoints() - damage);
    }
  }
}

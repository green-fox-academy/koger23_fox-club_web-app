package com.gfa.programmersfoxclub.service;

import com.gfa.programmersfoxclub.model.character.Fox;
import org.springframework.stereotype.Service;

@Service
public class HealthServiceImp implements IHealthService {
  public void healTotally(Fox fox) {
    fox.setHealthPoints(fox.getMAX_HEALTH());
  }

  public void heal(Fox fox, int healBy) {
    fox.setHealthPoints(fox.getHealthPoints() + healBy);
  }

  public void harm(Fox fox, int damage) {
    if (fox.getHealthPoints() > 0) {
      fox.setHealthPoints(fox.getHealthPoints() - damage);
    }
  }
}

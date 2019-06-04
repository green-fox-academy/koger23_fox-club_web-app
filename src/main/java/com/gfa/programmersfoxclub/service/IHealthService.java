package com.gfa.programmersfoxclub.service;

import com.gfa.programmersfoxclub.model.character.Fox;

public interface IHealthService {
  void healTotally(Fox fox);

  void heal(Fox fox, int healBy);

  void harm(Fox fox, int damage);
}

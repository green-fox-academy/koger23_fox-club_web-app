package com.gfa.programmersfoxclub.service;

import com.gfa.programmersfoxclub.model.character.Fox;

public interface HealthService {
  void healTotally(Fox fox);

  boolean heal(Fox fox);

  void harm(Fox fox, int damage);
}

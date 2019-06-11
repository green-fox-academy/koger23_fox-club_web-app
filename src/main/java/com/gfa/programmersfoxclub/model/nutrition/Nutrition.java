package com.gfa.programmersfoxclub.model.nutrition;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="nutrition", discriminatorType = DiscriminatorType.INTEGER)
public abstract class Nutrition {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  private Type type;
  private final int MAX_LEVEL = 10;
  private int level;
  private int reductionTimeMinutes;
  private int hungerReductionPoints;
  private int thirstReductionPoints;
  private String name;

  public Nutrition(Type type) {
    this.type = type;
    this.level = MAX_LEVEL;
    hungerReductionPoints = 0;
    thirstReductionPoints = 0;
  }

  public enum Type {
    DRINK,
    FOOD
  }

  public String getTypeEnum() {
    switch(type) {
      case FOOD:
        return "Food";
      case DRINK:
        return "Drink";
    }
    return "Nutrition";
  }
}

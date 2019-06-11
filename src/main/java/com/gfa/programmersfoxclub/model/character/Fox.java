package com.gfa.programmersfoxclub.model.character;


import com.gfa.programmersfoxclub.model.nutrition.Drink;
import com.gfa.programmersfoxclub.model.nutrition.Food;
import com.gfa.programmersfoxclub.model.nutrition.Nutrition;
import com.gfa.programmersfoxclub.model.trick.Trick;
import com.gfa.programmersfoxclub.model.user.User;
import com.gfa.programmersfoxclub.utils.date.DateUtils;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Fox {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;
  private final int MAX_HEALTH = 100;
  private String name = "";
  private int healthPoints = 100;
  @ManyToOne(fetch = FetchType.EAGER)
  private Food food;
  @ManyToOne(fetch = FetchType.EAGER)
  private Drink drink;
  private String color = "green";
  @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  @JoinTable(
          name = "fox_trick",
          joinColumns = {@JoinColumn(name = "fox_id")},
          inverseJoinColumns = {@JoinColumn(name = "trick_id")})
  private List<Trick> trick_list;
  private boolean alive = true;
  private String birthDayString = "";
  private long birthDateInMinutes = 0;
  private int thirstLevel = 0;
  private int hungerLevel = 0;
  private final int MAX_HUNGER_LEVEL = 10;
  private final int MAX_THIRST_LEVEL = 5;
  @ManyToOne(fetch = FetchType.LAZY)
  private User owner;

  public Fox() {
    this.birthDayString = DateUtils.getStringDateTime();
    this.birthDateInMinutes = DateUtils.getCurrentDateTimeInMinutes();
    trick_list = new ArrayList<>();
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public int getMAX_HEALTH() {
    return MAX_HEALTH;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getHealthPoints() {
    return healthPoints;
  }

  public void setHealthPoints(int healthPoints) {
    this.healthPoints = healthPoints;
  }

  public Food getFood() {
    return food;
  }

  public void setFood(Food food) {
    this.food = food;
  }

  public Drink getDrink() {
    return drink;
  }

  public void setDrink(Drink drink) {
    this.drink = drink;
  }

  public String getColor() {
    return color;
  }

  public void setColor(String color) {
    this.color = color;
  }

  public List<Trick> getTrick_list() {
    return trick_list;
  }

  public void setTrick_list(List<Trick> trick_list) {
    this.trick_list = trick_list;
  }

  public boolean isAlive() {
    return alive;
  }

  public void setAlive(boolean alive) {
    this.alive = alive;
  }

  public String getBirthDayString() {
    return birthDayString;
  }

  public void setBirthDayString(String birthDayString) {
    this.birthDayString = birthDayString;
  }

  public long getBirthDateInMinutes() {
    return birthDateInMinutes;
  }

  public void setBirthDateInMinutes(long birthDateInMinutes) {
    this.birthDateInMinutes = birthDateInMinutes;
  }

  public int getThirstLevel() {
    return thirstLevel;
  }

  public void setThirstLevel(int thirstLevel) {
    this.thirstLevel = thirstLevel;
  }

  public int getHungerLevel() {
    return hungerLevel;
  }

  public void setHungerLevel(int hungerLevel) {
    this.hungerLevel = hungerLevel;
  }

  public int getMAX_HUNGER_LEVEL() {
    return MAX_HUNGER_LEVEL;
  }

  public int getMAX_THIRST_LEVEL() {
    return MAX_THIRST_LEVEL;
  }

  public User getOwner() {
    return owner;
  }

  public void setOwner(User owner) {
    this.owner = owner;
  }

  public List<Nutrition> getNutritions() {
    List<Nutrition> nutritionList = new ArrayList<>();
    nutritionList.add(this.drink);
    nutritionList.add(this.food);
    return nutritionList;
  }
}

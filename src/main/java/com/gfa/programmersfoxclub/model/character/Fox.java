package com.gfa.programmersfoxclub.model.character;


import com.gfa.programmersfoxclub.model.nutrition.Drink;
import com.gfa.programmersfoxclub.model.nutrition.Food;
import com.gfa.programmersfoxclub.model.trick.Trick;
import com.gfa.programmersfoxclub.model.user.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Fox {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;
  private final int MAX_HEALTH = 100;
  private String name = "";
  private int healthPoints = 100;
  @ManyToOne(fetch = FetchType.LAZY)
  private Food food;
  @ManyToOne(fetch = FetchType.LAZY)
  private Drink drink;
  private String color = "green";
  @ManyToMany(cascade = CascadeType.ALL)
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
    trick_list = new ArrayList<>();
  }
}

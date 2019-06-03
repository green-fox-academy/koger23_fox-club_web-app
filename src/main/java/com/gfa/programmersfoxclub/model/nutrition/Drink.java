package com.gfa.programmersfoxclub.model.nutrition;

import com.gfa.programmersfoxclub.model.character.Fox;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@DiscriminatorValue("1")
public class Drink extends Nutrition {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  private String name;
  private final int reductionTimeMinutes = 2;
  @OneToMany(targetEntity = Fox.class, fetch = FetchType.EAGER, mappedBy = "drink")
  private List<Fox> fox;

  public Drink() {
    super(Type.DRINK);
    setReductionTimeMinutes(reductionTimeMinutes);
    fox = new ArrayList<>();
    name = "beer";
  }
}

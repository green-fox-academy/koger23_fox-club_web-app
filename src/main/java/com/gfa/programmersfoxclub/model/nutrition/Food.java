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
@DiscriminatorValue("2")
public class Food extends Nutrition {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;
  private final int reductionTimeMinutes = 2;
  @OneToMany(targetEntity = Fox.class, fetch = FetchType.EAGER, mappedBy = "food")
  private List<Fox> fox;

  public Food() {
    super(Type.FOOD);
    setReductionTimeMinutes(reductionTimeMinutes);
    fox = new ArrayList<>();
    setName("pizza");
  }

  public Food(String name) {
    super(Type.FOOD);
    setReductionTimeMinutes(reductionTimeMinutes);
    fox = new ArrayList<>();
    setName(name);
  }
}

package com.gfa.programmersfoxclub.model.user;

import com.gfa.programmersfoxclub.model.character.Fox;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;
  private String userName;
  private String email;
  @OneToMany(targetEntity = Fox.class, fetch = FetchType.EAGER, mappedBy = "owner")
  private List<Fox> foxList;

  public User() {
    foxList = new ArrayList<>();
  }
}

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
  private String username;
  private String email;
  @Transient
  private String password;
  private String passwordHash;
  @OneToMany(targetEntity = Fox.class, fetch = FetchType.EAGER, mappedBy = "owner")
  private List<Long> foxList;

  public User() {
    foxList = new ArrayList<>();
  }

  public User(String username, String password) {
    this.username = username;
    this.password = password;
    foxList = new ArrayList<>();
  }
  public User(String username, String password, String email) {
    this.username = username;
    this.password = password;
    this.email = email;
    foxList = new ArrayList<>();
  }
}

package com.gfa.programmersfoxclub.repository;


import com.gfa.programmersfoxclub.model.character.Fox;
import com.gfa.programmersfoxclub.model.user.User;
import org.springframework.data.repository.CrudRepository;

public interface IFoxRepository extends CrudRepository<Fox, Long> {
  Fox findByName(String name);

  int countAllByName(String name);

  Fox findByOwner(User owner);
}

package com.gfa.programmersfoxclub.repository;


import com.gfa.programmersfoxclub.model.character.Fox;
import org.springframework.data.repository.CrudRepository;

public interface IFoxRepository extends CrudRepository<Fox, Long> {
  Fox findByName(String name);

  int countAllByName(String name);
}

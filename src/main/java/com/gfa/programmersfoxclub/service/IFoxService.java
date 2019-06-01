package com.gfa.programmersfoxclub.service;


import com.gfa.programmersfoxclub.model.character.Fox;

import java.util.List;

public interface IFoxService {

  List<Fox> findAll();

  Fox findById(long id);

  void save(Fox fox);

  void update(Fox fox);

  Fox findFoxByName(String name);

  boolean checkFoxExists(String name);
}

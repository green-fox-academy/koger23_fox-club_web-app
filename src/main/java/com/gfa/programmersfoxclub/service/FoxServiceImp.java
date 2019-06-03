package com.gfa.programmersfoxclub.service;

import com.gfa.programmersfoxclub.model.character.Fox;
import com.gfa.programmersfoxclub.model.user.User;
import com.gfa.programmersfoxclub.repository.IFoxRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Setter
@Getter
public class FoxServiceImp implements IFoxService {
  private IFoxRepository foxRepository;

  public FoxServiceImp(IFoxRepository foxRepository) {
    this.foxRepository = foxRepository;
  }

  @Override
  public List<Fox> findAll() {
    List<Fox> foxList = new ArrayList<>();
    foxRepository.findAll().forEach(foxList::add);
    return foxList;
  }

  @Override
  public Fox findById(long id) {
    return foxRepository.findById(id).get();
  }

  @Override
  public void save(Fox fox) {
    foxRepository.save(fox);
  }

  @Override
  public void update(Fox fox) {
    foxRepository.save(fox);
  }

  @Override
  public Fox findFoxByName(String name) {
    return foxRepository.findByName(name);
  }

  @Override
  public boolean checkFoxExists(String name) {
    if (foxRepository.countAllByName(name) == 1) {
      return true;
    }
    return false;
  }

  @Override
  public Fox findFoxByOwner(User owner) {
//    return foxRepository.findFoxByOwner(owner);
    return null;
  }
}

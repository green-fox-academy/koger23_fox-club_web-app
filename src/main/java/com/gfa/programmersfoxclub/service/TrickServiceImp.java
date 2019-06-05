package com.gfa.programmersfoxclub.service;

import com.gfa.programmersfoxclub.model.trick.Trick;
import com.gfa.programmersfoxclub.repository.TrickRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TrickServiceImp implements ITrickService {
  private TrickRepository trickRepository;

  public TrickServiceImp(TrickRepository trickRepository) {
    this.trickRepository = trickRepository;
  }

  @Override
  public List<Trick> findAll() {
    List<Trick> trickList = new ArrayList<>();
    trickRepository.findAll().forEach(trickList::add);
    return trickList;
  }
}

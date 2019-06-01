package com.gfa.programmersfoxclub.repository;

import com.gfa.programmersfoxclub.model.trick.Trick;
import org.springframework.data.repository.CrudRepository;

public interface TrickRepository extends CrudRepository<Trick, Long> {
}

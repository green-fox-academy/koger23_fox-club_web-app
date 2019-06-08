package com.gfa.programmersfoxclub.repository;

import com.gfa.programmersfoxclub.model.nutrition.Nutrition;
import org.springframework.data.repository.CrudRepository;

public interface NutritionRepository extends CrudRepository<Nutrition, Long> {
  boolean findByNameIsNotLike(String name);
}

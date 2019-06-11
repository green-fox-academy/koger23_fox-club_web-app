package com.gfa.programmersfoxclub.repository;

import com.gfa.programmersfoxclub.model.nutrition.Drink;
import com.gfa.programmersfoxclub.model.nutrition.Food;
import com.gfa.programmersfoxclub.model.nutrition.Nutrition;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface NutritionRepository extends CrudRepository<Nutrition, Long> {
  boolean findByNameIsNotLike(String name);
  Drink findDrinkById(long id);
  Food findFoodById(long id);
  List<Food> findAllFood();
  List<Drink> findAllDrink();
}

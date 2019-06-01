package com.gfa.programmersfoxclub.repository;


import com.gfa.programmersfoxclub.model.user.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
}

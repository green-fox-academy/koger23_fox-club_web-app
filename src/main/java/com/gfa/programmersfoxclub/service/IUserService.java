package com.gfa.programmersfoxclub.service;

import com.gfa.programmersfoxclub.model.Validation;
import com.gfa.programmersfoxclub.model.user.User;

public interface IUserService {
  User getLoggedInUser();
  void logout();
  boolean validateAndLoginUser(User user);
  Validation validateAndLoginUser(User user, Validation validation);
  User createUser(String username, String password);
  void logInUser(User user);
}

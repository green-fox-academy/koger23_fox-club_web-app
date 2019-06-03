package com.gfa.programmersfoxclub.service;

import com.gfa.programmersfoxclub.model.Validation;
import com.gfa.programmersfoxclub.model.user.User;
import com.gfa.programmersfoxclub.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Service
public class UserServiceImp implements IUserService {

  private final HttpSession session;
  private static final String sessionKey = "user";
  private final PasswordEncoder passwordEncoder;
  private final UserRepository userRepository;

  public UserServiceImp(HttpSession session, PasswordEncoder passwordEncoder, UserRepository userRepository) {
    this.session = session;
    this.passwordEncoder = passwordEncoder;
    this.userRepository = userRepository;
  }

  @Override
  public User getLoggedInUser() {
    return (User) session.getAttribute(sessionKey);
  }

  @Override
  public void logout() {
    session.setAttribute(sessionKey, null);
  }

  @Override
  public boolean validateAndLoginUser(User user) {
    User userFromDb = userRepository.findByUserName(user.getUserName());
    if (userFromDb == null) return false;
    if (passwordEncoder.matches(user.getPassword(), userFromDb.getPasswordHash())) {
      logInUser(user);
      return true;
    }
    return false;
  }

  @Override
  public Validation validateAndLoginUser(User user, Validation validation) {
    User userFromDb = userRepository.findByUserName(user.getUserName());
    if (userFromDb == null) {
      validation.setMessage("User does not exists.");
      return validation;
    }
    if (passwordEncoder.matches(user.getPassword(), userFromDb.getPasswordHash())) {
      logInUser(user);
      validation.setUsernameOK(true);
      validation.setPasswordOK(true);
      return validation;
    }
    validation.setMessage("Invalid username or password.");
    return validation;
  }

  @Override
  public User createUser(String userName, String password) {
    User newUser = new User(userName, password);
    newUser.setPasswordHash(passwordEncoder.encode(password));
    userRepository.save(newUser);
    User userFromDb = userRepository.findByUserName(newUser.getUserName());
    logInUser(userFromDb);
    return userFromDb;
  }

  @Override
  public void logInUser(User user) {
    User userFromDb = userRepository.findByUserName(user.getUserName());
    session.setAttribute(sessionKey, userFromDb);
  }
}

package com.gfa.programmersfoxclub.service;

import com.gfa.programmersfoxclub.model.Validation;
import com.gfa.programmersfoxclub.model.user.User;
import com.gfa.programmersfoxclub.repository.UserRepository;
import com.gfa.programmersfoxclub.utils.date.DateUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

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
    User userFromDb = userRepository.findByUsername(user.getUsername());
    if (userFromDb == null) return false;
    if (passwordEncoder.matches(user.getPassword(), userFromDb.getPasswordHash())) {
      logInUser(user);
      return true;
    }
    return false;
  }

  @Override
  public Validation validateAndLoginUser(User user, Validation validation) {
    User userFromDb = userRepository.findByUsername(user.getUsername());
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
  public User createUser(String username, String password, String email) {
    User newUser = new User(username, password, email);
    newUser.setPasswordHash(passwordEncoder.encode(password));
    newUser.setRegistrationDayString(DateUtils.getStringDateTime());
    userRepository.save(newUser);
    User userFromDb = userRepository.findByUsername(newUser.getUsername());
    logInUser(userFromDb);
    return userFromDb;
  }

  @Override
  public void logInUser(User user) {
    User userFromDb = userRepository.findByUsername(user.getUsername());
    session.setAttribute(sessionKey, userFromDb);
  }

  @Override
  public void updateUsersActiveFoxIndex(int activeFoxIndex) {
    User user = getLoggedInUser();
    User userToUpdate = userRepository.findByUsername(user.getUsername());
    userToUpdate.setActiveFoxIndex(activeFoxIndex);
    userRepository.save(userToUpdate);
  }

  public User findByUsername(String name) {
    return userRepository.findByUsername(name);
  }
}

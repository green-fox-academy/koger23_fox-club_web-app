package com.gfa.programmersfoxclub.service;


import com.gfa.programmersfoxclub.model.nutrition.Nutrition;
import com.gfa.programmersfoxclub.utils.date.DateUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LoggerImp implements ILogger {
  private List<String> actionList = new ArrayList<>();
  private List<String> lastFiveActionList = new ArrayList<>();

  public LoggerImp() {
  }

  public List<String> getLastFiveActionList() {
    return lastFiveActionList;
  }

  public void setLastFiveActionList(List<String> lastFiveActionList) {
    this.lastFiveActionList = lastFiveActionList;
  }

  public void saveLoginAction() {
    actionList.clear();
    String message = DateUtils.getStringDateTime() + ": logged in.";
    actionList.add(message);
  }

  public void saveNutritionChange(Nutrition nutrition, String before, String after) {
    if (before.equals(after)) {
      return;
    }
    String message = DateUtils.getStringDateTime() + ": " + nutrition.getType() + " changed from " + before + " to " + after;
    actionList.add(message);
  }

  public void saveTrickAction(String trick) {
    String message = DateUtils.getStringDateTime() + ": " + "Learned to " + trick;
    actionList.add(message);
  }

  public List<String> getActionList() {
    return actionList;
  }

  public void setActionList(List<String> actionList) {
    this.actionList = actionList;
  }

  public List<String> getLastFiveAction() {
    lastFiveActionList.clear();
    if (actionList.size() > 0) {
      for (int i = actionList.size() - 1; i >= 0; i--) {
        if (lastFiveActionList.size() == 5) {
          break;
        }
        lastFiveActionList.add(actionList.get(i));
      }
    }
    return lastFiveActionList;
  }
}

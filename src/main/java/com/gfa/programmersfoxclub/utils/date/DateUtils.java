package com.gfa.programmersfoxclub.utils.date;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
  public static String getStringDateTime() {
    DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
    Date date = new Date();
    return dateFormat.format(date);
  }

  public static Date parseDatetTime(String stringDateTime) {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
    Date dateTime = new Date();
    try {
      dateTime = simpleDateFormat.parse(stringDateTime);
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return dateTime;
  }

  public static long getCurrentDateTimeInMinutes() {
    Date date = new Date();
    return date.getTime() / 60000;
  }
}

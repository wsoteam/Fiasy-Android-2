package com.wsoteam.diet.presentation.plans;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateHelper {

  private static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

  public static Date stringToDate(String str){
    try {
      return formatter.parse(str);
    }catch (Exception e){
      return null;
    }
  }
  public static String dateToString(Date date){
    return formatter.format(date);
  }

  public static Date getCurrentDate(){
   return stringToDate(dateToString(new Date()));
  }
}

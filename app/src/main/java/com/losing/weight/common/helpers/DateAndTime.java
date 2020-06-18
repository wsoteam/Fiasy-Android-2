package com.losing.weight.common.helpers;

import com.losing.weight.common.Analytics.EventProperties;

import java.util.Calendar;

public class DateAndTime {
    public static Calendar dropTime(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 1);
        calendar.set(Calendar.MINUTE, 1);
        calendar.set(Calendar.SECOND, 1);
        calendar.set(Calendar.MILLISECOND, 1);
        return calendar;
    }

    public static String getDateType(int day, int month, int year) {
        Calendar calendar = Calendar.getInstance();
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentYear = calendar.get(Calendar.YEAR);

        if (currentDay == day && currentMonth == month && currentYear == year) {
            return EventProperties.food_date_today;
        } else if (currentDay > day && currentMonth >= month && currentYear >= year) {
            return EventProperties.food_date_future;
        } else {
            return EventProperties.food_date_past;
        }
    }
}

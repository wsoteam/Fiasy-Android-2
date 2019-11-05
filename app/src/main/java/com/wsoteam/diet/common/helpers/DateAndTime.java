package com.wsoteam.diet.common.helpers;

import java.util.Calendar;

public class DateAndTime {
    public static Calendar dropTime(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 1);
        calendar.set(Calendar.MINUTE, 1);
        calendar.set(Calendar.SECOND, 1);
        calendar.set(Calendar.MILLISECOND, 1);
        return calendar;
    }
}

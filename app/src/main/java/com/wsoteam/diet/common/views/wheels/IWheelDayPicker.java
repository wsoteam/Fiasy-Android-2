package com.wsoteam.diet.common.views.wheels;

public interface IWheelDayPicker {

    int getSelectedDay();

    void setSelectedDay(int day);

    int getCurrentDay();

    void setYearAndMonth(int year, int month);

    int getYear();

    void setYear(int year);

    int getMonth();

    void setMonth(int month);
}
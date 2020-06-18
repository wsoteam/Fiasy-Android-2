package com.losing.weight.common.views.wheels;

public interface IWheelDayPicker {

    int getSelectedDay();

    void setSelectedDay(int day);

    int getCurrentDay();

    void setYearAndMonth(int year, int month);

    int getYear();

    void setYear(int year);

    String getMonth();
//    int getMonth();

//    String getMonthText();

    void setMonth(int month);
}
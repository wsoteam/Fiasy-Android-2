package com.losing.weight.common.views.wheels;

public interface IWheelDatePicker {
    void setOnDateSelectedListener(WheelDatePicker.OnDateSelectedListener listener);

    String getCurrentDate();

    int getItemAlignYear();

    void setItemAlignYear(int align);

    int getItemAlignMonth();

    void setItemAlignMonth(int align);

    int getItemAlignDay();

    void setItemAlignDay(int align);

    WheelYearPicker getWheelYearPicker();

    WheelMonthPicker getWheelMonthPicker();

    WheelDayPicker getWheelDayPicker();
}
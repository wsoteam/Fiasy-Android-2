package com.wsoteam.diet.common.views.bar.formater;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;

public class XWeekFormatter extends ValueFormatter {
    ArrayList<String> days = new ArrayList<>();

    public XWeekFormatter() {
        days.add("пн");
        days.add("вт");
        days.add("ср");
        days.add("чт");
        days.add("пт");
        days.add("сб");
        days.add("вс");
    }

    @Override
    public String getFormattedValue(float value) {
        return days.get((int) value);
    }
}

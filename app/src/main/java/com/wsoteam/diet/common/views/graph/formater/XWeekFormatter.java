package com.wsoteam.diet.common.views.graph.formater;

import android.util.Log;

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
        Log.e("LOL", String.valueOf(value));
        return days.get((int) value);
    }
}

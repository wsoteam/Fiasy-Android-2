package com.wsoteam.diet.common.views.graph.formater;


import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;


public class XWeekFormatter extends ValueFormatter {
    List<String> days;

    public XWeekFormatter() {
        days = new ArrayList<>(Arrays.asList(
            new DateFormatSymbols(Locale.getDefault()).getShortWeekdays()));
        days.add(days.get(1));
        days.remove(0);
        days.remove(0);
    }

    @Override
    public String getFormattedValue(float value) {
        return days.get((int) value);
    }
}

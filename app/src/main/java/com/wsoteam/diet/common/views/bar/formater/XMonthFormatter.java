package com.wsoteam.diet.common.views.bar.formater;

import android.util.Log;

import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;

public class XMonthFormatter extends ValueFormatter {
    ArrayList<String> weeks;

    public XMonthFormatter(ArrayList<String> weeks) {
        this.weeks = weeks;
    }

    @Override
    public String getFormattedValue(float value) {
        Log.e("LOL", String.valueOf(value));
        return weeks.get((int) value);
    }
}

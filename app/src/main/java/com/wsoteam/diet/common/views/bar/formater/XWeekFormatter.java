package com.wsoteam.diet.common.views.bar.formater;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;

public class XWeekFormatter extends ValueFormatter {
    @Override
    public String getFormattedValue(float value) {
        return String.valueOf(value);
    }
}

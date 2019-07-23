package com.wsoteam.diet.common.views.bar.formater;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.ArrayList;

public class XWeekFormatter implements IAxisValueFormatter {
    private ArrayList<String> xLabels;

    public XWeekFormatter(ArrayList<String> xLabels) {
        this.xLabels = xLabels;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return null;
    }
}

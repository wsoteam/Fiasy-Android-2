package com.losing.weight.common.views.graph.formater;

import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;

public class XYearFormatter extends ValueFormatter {
    ArrayList<String> months = new ArrayList<>();

    public XYearFormatter() {
        months.add("1");
        months.add("2");
        months.add("3");
        months.add("4");
        months.add("5");
        months.add("6");
        months.add("7");
        months.add("8");
        months.add("9");
        months.add("10");
        months.add("11");
        months.add("12");
    }

    @Override
    public String getFormattedValue(float value) {
        return months.get((int) value);
    }
}
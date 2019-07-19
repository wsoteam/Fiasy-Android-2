package com.wsoteam.diet.common.views.bar.formater;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.SimpleDateFormat;
import java.util.Date;

public class XFormatter extends ValueFormatter {


    @Override
    public String getFormattedValue(float value) {
        Date date = new Date((long) value);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM");
        String correctName = dateFormat.format(date);
        return correctName;
    }
}

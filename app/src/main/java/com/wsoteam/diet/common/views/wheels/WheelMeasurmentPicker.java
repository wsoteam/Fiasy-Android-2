package com.wsoteam.diet.common.views.wheels;

import android.content.Context;
import android.util.AttributeSet;

import com.wsoteam.diet.R;

import java.util.ArrayList;
import java.util.List;

public class WheelMeasurmentPicker extends WheelPicker implements IWheelMeasurmentPicker {
    private final int START_MEAS_INTERAVAL = 0;
    private final int END_MEAS_INTERAVAL = 200;
    private List<Integer> weightValues = new ArrayList<>();
    private Context context;

    @Override
    public int getSelectedMeas() {
        return getCurrentItemPosition();
    }

    @Override
    public void setSelectedMeas(int meas) {
        setSelectedItemPosition(meas, false);
    }

    public WheelMeasurmentPicker(Context context) {
        super(context, null);
    }

    public WheelMeasurmentPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        setData(createMeasList());
    }

    private List<String> createMeasList() {
        List<String> measValuesStrings = new ArrayList<>();
        String unitValue = context.getResources().getString(R.string.meas_cwh);

        for (int i = START_MEAS_INTERAVAL; i <= END_MEAS_INTERAVAL; i++) {
            measValuesStrings.add(String.valueOf(i) + " " + unitValue);
            weightValues.add(i);
        }
        return measValuesStrings;
    }
}

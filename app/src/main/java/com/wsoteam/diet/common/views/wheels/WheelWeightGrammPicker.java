package com.wsoteam.diet.common.views.wheels;

import android.content.Context;
import android.util.AttributeSet;

import com.wsoteam.diet.R;

import java.util.ArrayList;
import java.util.List;

public class WheelWeightGrammPicker extends WheelPicker implements IWheelWeightGrammPicker {
    private final int START_WEIGHT_INTERAVAL = 0;
    private final int END_WEIGHT_INTERAVAL = 900;
    private final int STEP_WEIGHT_INTERAVAL = 100;
    private Context context;
    private List<Integer> weightValues = new ArrayList<>();

    @Override
    public int getSelectedWeight() {
        return 0;
    }

    @Override
    public void setSelectedWeight(int weight) {

    }

    public WheelWeightGrammPicker(Context context) {
        super(context, null);
    }

    public WheelWeightGrammPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        super.setData(createWeightList());
    }

    private List<String> createWeightList() {
        List<String> weightsValuesStrings = new ArrayList<>();
        String unitValue = context.getResources().getString(R.string.meas_gramm);

        for (int i = START_WEIGHT_INTERAVAL; i <= END_WEIGHT_INTERAVAL; i+= STEP_WEIGHT_INTERAVAL) {
            weightsValuesStrings.add(String.valueOf(i) + " " + unitValue);
            weightValues.add(i);
        }
        return weightsValuesStrings;
    }
}

package com.wsoteam.diet.common.views.wheels;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;

import com.wsoteam.diet.R;

import java.util.ArrayList;
import java.util.List;

public class WheelWeightKiloPicker extends WheelPicker implements IWheelWeightKiloPicker  {
    private final int START_WEIGHT_INTERAVAL = 30;
    private final int END_WEIGHT_INTERAVAL = 200;
    private Context context;
    private List<Integer> weightValues = new ArrayList<>();

    @Override
    public int getSelectedWeight() {
        return weightValues.get(getCurrentItemPosition());
    }


    @Override
    public void setSelectedWeight(int weight) {
        setSelectedItemPosition(weightValues.indexOf(weight), false);
    }


    public WheelWeightKiloPicker(Context context) {
        super(context, null);
    }

    public WheelWeightKiloPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        super.setData(createWeightList());
    }

    private List<String> createWeightList(){
        List<String> weightsValuesStrings = new ArrayList<>();
        String unitValue = context.getResources().getString(R.string.weight_unit);

        for (int i = START_WEIGHT_INTERAVAL; i <= END_WEIGHT_INTERAVAL; i++) {
            weightsValuesStrings.add(String.valueOf(i) + " " + unitValue);
            weightValues.add(i);
        }
        return weightsValuesStrings;
    }
}

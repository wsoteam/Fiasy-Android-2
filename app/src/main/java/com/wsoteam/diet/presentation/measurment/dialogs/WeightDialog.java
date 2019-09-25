package com.wsoteam.diet.presentation.measurment.dialogs;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;

import com.wsoteam.diet.R;
import com.wsoteam.diet.Sync.WorkWithFirebaseDB;
import com.wsoteam.diet.common.views.wheels.WheelWeightGrammPicker;
import com.wsoteam.diet.common.views.wheels.WheelWeightKiloPicker;
import com.wsoteam.diet.presentation.measurment.POJO.Weight;

public class WeightDialog {
    private static WheelWeightGrammPicker gPicker;
    private static WheelWeightKiloPicker kPicker;


    public static void showWeightDialog(Context context, Weight weight, WeightCallback callback) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        AlertDialog alertDialog = builder.create();
        weight.setWeight(35.80);
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.alert_dialog_add_weight, null);
        gPicker = view.findViewById(R.id.whlWeightGrammPicker);
        kPicker = view.findViewById(R.id.whlWeightKiloPicker);
        Button btnSave = view.findViewById(R.id.btnSave);
        int valueKilo = (int) weight.getWeight();
        kPicker.setSelectedWeight(valueKilo);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Weight newWeight = getWeight(weight);
                WorkWithFirebaseDB.setWeight(newWeight, String.valueOf(newWeight.getTimeInMillis()));
                callback.update();
                alertDialog.cancel();
            }
        });

        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        alertDialog.setView(view);
        alertDialog.show();
    }

    private static Weight getWeight(Weight weight) {
        int kilo = kPicker.getSelectedWeight();
        double gramm = gPicker.getSelectedWeight();
        double globalValue = kilo + gramm / 1000;
        weight.setWeight(globalValue);
        return weight;
    }
}

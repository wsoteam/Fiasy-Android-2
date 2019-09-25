package com.wsoteam.diet.presentation.measurment.dialogs;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;

import com.wsoteam.diet.R;
import com.wsoteam.diet.common.views.wheels.WheelWeightGrammPicker;
import com.wsoteam.diet.common.views.wheels.WheelWeightKiloPicker;
import com.wsoteam.diet.presentation.measurment.POJO.Weight;

import java.math.BigDecimal;

public class WeightDialog {

    public static void showWeightDialog(Context context, Weight weight,  WeightCallback callback) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        AlertDialog alertDialog = builder.create();
        weight.setWeight(35.80);
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.alert_dialog_add_weight, null);
        WheelWeightGrammPicker gPicker = view.findViewById(R.id.whlWeightGrammPicker);
        WheelWeightKiloPicker kPicker = view.findViewById(R.id.whlWeightKiloPicker);
        Button btnSave = view.findViewById(R.id.btnSave);
        int valueKilo = (int) weight.getWeight();
        kPicker.setSelectedWeight(valueKilo);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.addWeight();
                alertDialog.cancel();
            }
        });

        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        alertDialog.setView(view);
        alertDialog.show();
    }
}

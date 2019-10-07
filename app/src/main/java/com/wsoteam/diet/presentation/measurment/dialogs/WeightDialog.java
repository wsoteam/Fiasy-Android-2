package com.wsoteam.diet.presentation.measurment.dialogs;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import android.widget.ImageButton;
import androidx.appcompat.app.AlertDialog;

import com.wsoteam.diet.R;
import com.wsoteam.diet.Sync.WorkWithFirebaseDB;
import com.wsoteam.diet.common.views.wheels.WheelWeightGrammPicker;
import com.wsoteam.diet.common.views.wheels.WheelWeightKiloPicker;
import com.wsoteam.diet.presentation.measurment.POJO.Weight;
import java.math.BigDecimal;
import java.math.BigInteger;

public class WeightDialog {
  private static WheelWeightGrammPicker gPicker;
  private static WheelWeightKiloPicker kPicker;

  public static void showWeightDialog(Context context, Weight weight, WeightCallback callback) {
    AlertDialog.Builder builder = new AlertDialog.Builder(context);
    AlertDialog alertDialog = builder.create();
    LayoutInflater layoutInflater =
        (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    View view = layoutInflater.inflate(R.layout.alert_dialog_add_weight, null);
    gPicker = view.findViewById(R.id.whlWeightGrammPicker);
    kPicker = view.findViewById(R.id.whlWeightKiloPicker);
    Button btnSave = view.findViewById(R.id.btnSave);
    Button btnCancel = view.findViewById(R.id.btnCancel);
    ImageButton ibDelete = view.findViewById(R.id.ibDelete);
    int valueKilo = (int) weight.getWeight();
    int valueGramm = getGramms(weight.getWeight());
    kPicker.setSelectedWeight(valueKilo);
    gPicker.setSelectedWeight(valueGramm);

    btnSave.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Weight newWeight = getWeight(weight);
        WorkWithFirebaseDB.setWeight(newWeight, String.valueOf(newWeight.getTimeInMillis()));
        callback.update(newWeight);
        alertDialog.cancel();
      }
    });

    btnCancel.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        alertDialog.cancel();
      }
    });

    ibDelete.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        Weight newWeight = getEmptyWeight(weight);
        WorkWithFirebaseDB.setWeight(newWeight, String.valueOf(newWeight.getTimeInMillis()));
        callback.update(newWeight);
        alertDialog.cancel();
      }
    });

    alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
    alertDialog.setView(view);
    alertDialog.show();
  }

  private static int getGramms(double weight) {
    String s = String.valueOf(weight);
    if (s.contains(".")){
      s = s.split("\\.")[1];
    }else if (s.contains(",")){
      s = s.split(",")[1];
    }
    int value = Integer.parseInt(s);
    return value;
  }

  private static Weight getWeight(Weight weight) {
    int kilo = kPicker.getSelectedWeight();
    double gramm = gPicker.getSelectedWeight();
    double globalValue = kilo + gramm / 1000;
    weight.setWeight(globalValue);
    return weight;
  }

  private static Weight getEmptyWeight(Weight weight) {
    weight.setWeight(0);
    return weight;
  }
}

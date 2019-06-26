package com.wsoteam.diet.Recipes.helper;


import android.content.Context;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.wsoteam.diet.R;

public class SuccessfulAlertDialog {

    public static AlertDialog start(Context context, String msg){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        AlertDialog alertDialog = builder.create();
        View view = LayoutInflater.from(context).inflate(R.layout.successful_alert_dialog, null);
        TextView textView = view.findViewById(R.id.tvDescription);
        textView.setText(msg);
        alertDialog.setView(view);
        alertDialog.show();
        new CountDownTimer(800, 100) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                alertDialog.dismiss();

            }
        }.start();
        return alertDialog;
    }
}

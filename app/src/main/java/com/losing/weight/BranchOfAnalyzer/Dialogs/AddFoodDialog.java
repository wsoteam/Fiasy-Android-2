package com.losing.weight.BranchOfAnalyzer.Dialogs;

import android.content.Context;
import androidx.appcompat.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import com.losing.weight.R;

public class AddFoodDialog {
    public static AlertDialog createChoiseEatingAlertDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        AlertDialog alertDialog = builder.create();
        View view = LayoutInflater.from(context).inflate(R.layout.alert_dialog_succes_add_food, null);
        alertDialog.setView(view);
        alertDialog.show();

        /*final Handler handler  = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (alertDialog.isShowing()) {
                    alertDialog.dismiss();
                }
            }
        };

        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                handler.removeCallbacks(runnable);
            }
        });

        handler.postDelayed(runnable, 2000);*/

        return alertDialog;
    }

}

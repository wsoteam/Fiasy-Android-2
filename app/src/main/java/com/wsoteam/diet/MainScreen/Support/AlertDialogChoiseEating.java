package com.wsoteam.diet.MainScreen.Support;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;

import com.wsoteam.diet.BranchOfAnalyzer.ActivityListAndSearch;
import com.wsoteam.diet.Config;
import com.wsoteam.diet.R;

public class AlertDialogChoiseEating {
    public static AlertDialog createChoiseEatingAlertDialog(Context context, String date) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        AlertDialog alertDialog = builder.create();
        View view = LayoutInflater.from(context).inflate(R.layout.alert_dialog_choise_eating_type, null);

        ImageButton ibChoiseEatingBreakFast = view.findViewById(R.id.ibChoiseEatingBreakFast);
        ImageButton ibChoiseEatingLunch = view.findViewById(R.id.ibChoiseEatingLunch);
        ImageButton ibChoiseEatingDinner = view.findViewById(R.id.ibChoiseEatingDinner);
        ImageButton ibChoiseEatingSnack = view.findViewById(R.id.ibChoiseEatingSnack);

        ibChoiseEatingBreakFast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, ActivityListAndSearch.class)
                        .putExtra(Config.TAG_CHOISE_EATING, Config.INTENT_CHOISE_BREAKFAST)
                        .putExtra(Config.INTENT_DATE_FOR_SAVE, date));
                alertDialog.cancel();
            }
        });
        ibChoiseEatingLunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, ActivityListAndSearch.class)
                        .putExtra(Config.TAG_CHOISE_EATING, Config.INTENT_CHOISE_LUNCH)
                        .putExtra(Config.INTENT_DATE_FOR_SAVE, date));
                alertDialog.cancel();
            }
        });
        ibChoiseEatingDinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, ActivityListAndSearch.class)
                        .putExtra(Config.TAG_CHOISE_EATING, Config.INTENT_CHOISE_DINNER)
                        .putExtra(Config.INTENT_DATE_FOR_SAVE, date));
                alertDialog.cancel();
            }
        });
        ibChoiseEatingSnack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, ActivityListAndSearch.class)
                        .putExtra(Config.TAG_CHOISE_EATING, Config.INTENT_CHOISE_SNACK)
                        .putExtra(Config.INTENT_DATE_FOR_SAVE, date));
                alertDialog.cancel();
            }
        });

        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        alertDialog.setView(view);

        return alertDialog;
    }


}

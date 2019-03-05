package com.wsoteam.diet.MainScreen.AlertDialogs;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.wsoteam.diet.BranchOfAnalyzer.ActivityListAndSearch;
import com.wsoteam.diet.Config;
import com.wsoteam.diet.R;

public class AlertDialogChoiseEating {
    public static AlertDialog createChoiseEatingAlertDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        AlertDialog alertDialog = builder.create();
        View view = LayoutInflater.from(context).inflate(R.layout.alert_dialog_choise_eating_type, null);

        CardView cvChoiseEatingBreakFast = view.findViewById(R.id.cvChoiseEatingBreakFast);
        CardView cvChoiseEatingLunch = view.findViewById(R.id.cvChoiseEatingLunch);
        CardView cvChoiseEatingDinner = view.findViewById(R.id.cvChoiseEatingDinner);
        CardView cvChoiseEatingSnack = view.findViewById(R.id.cvChoiseEatingSnack);

        Intent intent = new Intent(context, ActivityListAndSearch.class);

        cvChoiseEatingBreakFast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, ActivityListAndSearch.class)
                        .putExtra(Config.TAG_CHOISE_EATING, Config.INTENT_CHOISE_BREAKFAST));
                alertDialog.cancel();
            }
        });
        cvChoiseEatingLunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, ActivityListAndSearch.class)
                        .putExtra(Config.TAG_CHOISE_EATING, Config.INTENT_CHOISE_LUNCH));
                alertDialog.cancel();
            }
        });
        cvChoiseEatingDinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, ActivityListAndSearch.class)
                        .putExtra(Config.TAG_CHOISE_EATING, Config.INTENT_CHOISE_DINNER));
                alertDialog.cancel();
            }
        });
        cvChoiseEatingSnack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, ActivityListAndSearch.class)
                        .putExtra(Config.TAG_CHOISE_EATING, Config.INTENT_CHOISE_SNACK));
                alertDialog.cancel();
            }
        });

        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        alertDialog.setView(view);

        return alertDialog;
    }


}

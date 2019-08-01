package com.wsoteam.diet.Recipes.adding;

import android.content.Context;
import androidx.appcompat.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import com.wsoteam.diet.R;

public class AddRecipeAlertDialog {

    public static AlertDialog createChoiceEatingAlertDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        AlertDialog alertDialog = builder.create();
        View view = LayoutInflater.from(context).inflate(R.layout.adding_recipe_aelert_dialog_success, null);
        alertDialog.setView(view);
        alertDialog.show();

        return alertDialog;
    }
}

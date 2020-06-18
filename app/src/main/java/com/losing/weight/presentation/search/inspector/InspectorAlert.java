package com.losing.weight.presentation.search.inspector;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.losing.weight.Config;
import com.losing.weight.R;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AlertDialog;

public class InspectorAlert {
    public static void askChangeEatingId(int[] countFood, int eatingId, Context context, Inspector inspector) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        AlertDialog alertDialog = builder.create();
        LayoutInflater layoutInflater =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.alert_dialog_basket_change_eating, null);
        TextView tvCancel = view.findViewById(R.id.tvCancel);
        TextView tvContinue = view.findViewById(R.id.tvContinue);
        List<TextView> eatTvs = new ArrayList<>();
        eatTvs.add(view.findViewById(R.id.tvBreakfast));
        eatTvs.add(view.findViewById(R.id.tvLunch));
        eatTvs.add(view.findViewById(R.id.tvDinner));
        eatTvs.add(view.findViewById(R.id.tvSnack));
        TextView tvAsk = view.findViewById(R.id.tvAsk);

        for (int i = 0; i < eatTvs.size(); i++) {
            if (countFood[i] != Config.EMPTY_BASKET){
                eatTvs.get(i).setVisibility(View.VISIBLE);
                eatTvs.get(i).setText(formText(countFood[i], context, i));
            }
        }
        tvAsk.setText(context.getResources().getString(R.string.srch_insp_ask) + " " + context.getResources().getStringArray(R.array.srch_eating)[eatingId]);

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                inspector.getPermission(false);
                alertDialog.cancel();
            }
        });
        tvContinue.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                inspector.getPermission(true);
                alertDialog.cancel();
            }
        });

        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        alertDialog.setView(view);
        alertDialog.show();
    }

    private static Spannable formText(int count, Context context, int eatingId) {
        String staticText = context.getResources().getStringArray(R.array.srch_insp)[eatingId] + " ";
        Spannable spannable = new SpannableString(staticText + context.getResources().getQuantityString(R.plurals.srch_foods, count, count));
        spannable.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.srch_painted_string)), staticText.length(), spannable.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannable;
    }
}

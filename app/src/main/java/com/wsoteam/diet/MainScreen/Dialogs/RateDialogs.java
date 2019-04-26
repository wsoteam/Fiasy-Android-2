package com.wsoteam.diet.MainScreen.Dialogs;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;

import com.wsoteam.diet.R;

public class RateDialogs {

    public static void showGradeDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        AlertDialog alertDialog = builder.create();
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.alert_dialog_grade, null);

        Animation movFromLeft = AnimationUtils.loadAnimation(context, R.anim.anim_moving_from_left);
        Animation movOutToRight = AnimationUtils.loadAnimation(context, R.anim.anim_moving_out_to_right);
        Animation movFromBottom = AnimationUtils.loadAnimation(context, R.anim.moving_from_bottom);

        RatingBar ratingBar = view.findViewById(R.id.ratingBar);
        EditText edtReport = view.findViewById(R.id.edtRatingReport);
        TextView tvThank = view.findViewById(R.id.tvForGrade);
        ImageButton btnGradeClose = view.findViewById(R.id.btnGradeClose);
        Button btnGradeSend = view.findViewById(R.id.btnGradeSend);

        btnGradeClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
            }
        });

        btnGradeSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "sav@wsoteam.com", null));
                intent.putExtra(Intent.EXTRA_SUBJECT, "Отзыв");
                intent.putExtra(Intent.EXTRA_TEXT, edtReport.getText().toString());
                context.startActivity(Intent.createChooser(intent, "Send Email"));
                alertDialog.cancel();
            }
        });

        btnGradeSend.setVisibility(View.GONE);
        tvThank.setVisibility(View.GONE);
        edtReport.setVisibility(View.GONE);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                if (v >= 4) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("market://details?id=" + context.getPackageName()));
                    context.startActivity(intent);
                    alertDialog.cancel();
                } else {
                    if (edtReport.getVisibility() == View.GONE) {
                        edtReport.setVisibility(View.VISIBLE);
                        edtReport.startAnimation(movFromLeft);
                        ratingBar.startAnimation(movOutToRight);
                        ratingBar.setVisibility(View.GONE);
                        tvThank.setVisibility(View.VISIBLE);
                        tvThank.startAnimation(movFromLeft);
                        btnGradeSend.setVisibility(View.VISIBLE);
                        btnGradeSend.startAnimation(movFromBottom);
                    }
                }
            }
        });
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        alertDialog.setView(view);
        alertDialog.show();
    }

    public static void showGradeDialogFruits(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        AlertDialog alertDialog = builder.create();
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.alert_dialog_grade_fruits, null);

        Animation movFromLeft = AnimationUtils.loadAnimation(context, R.anim.anim_moving_from_left);
        Animation movOutToRight = AnimationUtils.loadAnimation(context, R.anim.anim_moving_out_to_right);
        Animation movFromBottom = AnimationUtils.loadAnimation(context, R.anim.moving_from_bottom);

        RatingBar ratingBar = view.findViewById(R.id.ratingBar);
        EditText edtReport = view.findViewById(R.id.edtRatingReport);
        TextView tvThank = view.findViewById(R.id.tvForGrade);
        ImageButton btnGradeClose = view.findViewById(R.id.btnGradeClose);
        Button btnGradeSend = view.findViewById(R.id.btnGradeSend);

        btnGradeClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
            }
        });

        btnGradeSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "sav@wsoteam.com", null));
                intent.putExtra(Intent.EXTRA_SUBJECT, "Отзыв");
                intent.putExtra(Intent.EXTRA_TEXT, edtReport.getText().toString());
                context.startActivity(Intent.createChooser(intent, "Send Email"));
                alertDialog.cancel();
            }
        });

        btnGradeSend.setVisibility(View.GONE);
        tvThank.setVisibility(View.GONE);
        edtReport.setVisibility(View.GONE);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                if (v >= 4) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("market://details?id=" + context.getPackageName()));
                    context.startActivity(intent);
                    alertDialog.cancel();
                } else {
                    if (edtReport.getVisibility() == View.GONE) {
                        edtReport.setVisibility(View.VISIBLE);
                        edtReport.startAnimation(movFromLeft);
                        ratingBar.startAnimation(movOutToRight);
                        ratingBar.setVisibility(View.GONE);
                        tvThank.setVisibility(View.VISIBLE);
                        tvThank.startAnimation(movFromLeft);
                        btnGradeSend.setVisibility(View.VISIBLE);
                        btnGradeSend.startAnimation(movFromBottom);
                    }
                }
            }
        });
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        alertDialog.setView(view);
        alertDialog.show();
    }


}

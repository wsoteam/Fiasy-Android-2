package com.losing.weight.presentation.plans.detail;


import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import com.losing.weight.R;
import java.util.Timer;
import java.util.TimerTask;

public class AlertDialogs {

 public static AlertDialog changePlan(Activity activity, View.OnClickListener listener,
     String planName) {

  AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity);

  LayoutInflater inflater = activity.getLayoutInflater();
  View dialogView = inflater.inflate(R.layout.alert_dialog_plan_change, null);
  dialogBuilder.setView(dialogView);

  Button cancelButton = dialogView.findViewById(R.id.btnCancel);
  Button changeButton = dialogView.findViewById(R.id.btnChange);
  TextView infoTextView = dialogView.findViewById(R.id.tvInfo);

  Spannable wordtoSpan =
      new SpannableString(String.format(activity.getString(R.string.tvInfo), planName));
  int startColor = 50;
  try {
   startColor = activity.getString(R.string.tvInfo).split("%s")[0].length();
  } catch (Exception e){
   e.printStackTrace();
  }

  wordtoSpan.setSpan(new ForegroundColorSpan(Color.parseColor("#de0067a1")), startColor,
      startColor + planName.length(),
      Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
  infoTextView.setText(wordtoSpan);
  AlertDialog alertDialog = dialogBuilder.create();
  cancelButton.setOnClickListener((View view) -> {
   alertDialog.dismiss();
  });

  changeButton.setOnClickListener(listener);

  return alertDialog;
 }

  public static AlertDialog planJoined(Activity activity, int delay){

   AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity);

   LayoutInflater inflater = activity.getLayoutInflater();
   View dialogView = inflater.inflate(R.layout.alert_dialog_plan_joined, null);
   dialogBuilder.setView(dialogView);

   AlertDialog alertDialog = dialogBuilder.create();
   alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

   final Timer t = new Timer();
   t.schedule(new TimerTask() {
    public void run() {
     alertDialog.dismiss(); // when the task active then close the dialog
     t.cancel(); // also just top the timer thread, otherwise, you may receive a crash report
    }
   }, delay);
   return alertDialog;
  }
}

package com.wsoteam.diet.BranchOfAnalyzer.Dialogs;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.wsoteam.diet.BranchOfAnalyzer.POJOClaim.Claim;
import com.wsoteam.diet.BranchOfAnalyzer.POJOFoodSQL.Food;
import com.wsoteam.diet.R;
import com.wsoteam.diet.Sync.WorkWithFirebaseDB;

import java.util.Calendar;

import butterknife.BindView;

public class ClaimForm {
    public static AlertDialog createChoiseEatingAlertDialog(Context context, Food food) {
        int MIN = 9;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        AlertDialog alertDialog = builder.create();
        View view = LayoutInflater.from(context).inflate(R.layout.alert_dialog_form_claim, null);
        EditText edtClaim = view.findViewById(R.id.edtTextClaim);
        Button cancel = view.findViewById(R.id.btnCancelClaim);
        Button send = view.findViewById(R.id.btnSendClaim);
        send.setClickable(false);

        edtClaim.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.e("LOL", "LOL");
                if (s.length() > MIN && !send.isClickable()){
                    send.setClickable(true);
                    send.setTextColor(context.getResources().getColor(R.color.button_claim_active));
                }else if (s.length() < MIN && send.isClickable()){
                    send.setClickable(false);
                    send.setTextColor(context.getResources().getColor(R.color.button_claim_inactive));
                }
            }


            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!edtClaim.getText().toString().equals("")
                        && !edtClaim.getText().toString().equals(" ")
                        && edtClaim.getText().toString().length() > MIN) {
                    WorkWithFirebaseDB.sendClaim(fillClaim(food, edtClaim.getText().toString()));
                    Toast.makeText(context, "Жалоба отправлена. Спасибо!", Toast.LENGTH_SHORT).show();
                    alertDialog.cancel();
                }
            }
        });
        alertDialog.setView(view);
        alertDialog.show();
        return alertDialog;
    }

    private static Claim fillClaim(Food food, String textClaim) {
        Claim claim = new Claim();
        claim.setDay(Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        claim.setMonth(Calendar.getInstance().get(Calendar.MONTH) + 1);
        claim.setYear(Calendar.getInstance().get(Calendar.YEAR));
        claim.setMinutes(Calendar.getInstance().get(Calendar.MINUTE));
        claim.setHours(Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
        claim.setUserId(FirebaseAuth.getInstance().getCurrentUser().getUid());
        claim.setTextClaim(textClaim);
        claim.setFoodInfo(food.getFullInfo());
        claim.setFoodId(food.getId());
        return claim;
    }
}

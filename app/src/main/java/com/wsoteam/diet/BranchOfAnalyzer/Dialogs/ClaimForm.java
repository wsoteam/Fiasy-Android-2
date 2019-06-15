package com.wsoteam.diet.BranchOfAnalyzer.Dialogs;

import android.content.Context;
import android.support.v7.app.AlertDialog;
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
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        AlertDialog alertDialog = builder.create();
        View view = LayoutInflater.from(context).inflate(R.layout.alert_dialog_form_claim, null);
        TextView tvTitle = view.findViewById(R.id.tvFoodTitle);
        EditText edtClaim = view.findViewById(R.id.edtTextClaim);
        Button cancel = view.findViewById(R.id.btnCancelClaim);
        Button send = view.findViewById(R.id.btnSendClaim);
        tvTitle.setText(food.getFullInfo());

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
                        || !edtClaim.getText().toString().equals(" ")
                        || edtClaim.getText().toString().length() > 3){
                    WorkWithFirebaseDB.sendClaim(fillClaim(food, edtClaim.getText().toString()));
                }else {
                    Toast.makeText(context, "Проверьте введенные данные", Toast.LENGTH_SHORT).show();
                }
                alertDialog.cancel();
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

package com.wsoteam.diet.BranchOfCalculating;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.wsoteam.diet.R;
import com.yandex.metrica.YandexMetrica;

public class ActivityCalculatorLorenc extends AppCompatActivity {
    EditText edtLorencHeight;
    Button btnCalculate;
    InterstitialAd interstitialAd;
    AdView adBanner;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(interstitialAd.isLoaded()){
            interstitialAd.show();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator_lorenc);
        edtLorencHeight = findViewById(R.id.edtLorencHeight);
        btnCalculate = findViewById(R.id.btnLorencCalculate);
        adBanner = findViewById(R.id.bnrCalculatorLorenc);

        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    createAlertDialog(calculate());
                } catch (Exception e) {
                    Toast.makeText(ActivityCalculatorLorenc.this, R.string.check_your_data, Toast.LENGTH_SHORT).show();
                }

            }
        });

        adBanner.loadAd(new AdRequest.Builder().build());
        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(getResources().getString(R.string.admob_interstitial));
        interstitialAd.loadAd(new AdRequest.Builder().build());

        YandexMetrica.reportEvent("Открыт экран: Калькулятор Брока");


    }

    private void createAlertDialog(double calculate) {
        TextView tvAlertDialogWeight;
        FloatingActionButton btnOk;

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog alertDialog = builder.create();
        View view = getLayoutInflater().inflate(R.layout.alert_dialog_weight, null);
        tvAlertDialogWeight = view.findViewById(R.id.tvAlertDialogWeight);
        btnOk = view.findViewById(R.id.btnAlertDialogOk);
        tvAlertDialogWeight.setText(String.valueOf(calculate) + " кг");
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
            }
        });
        alertDialog.setView(view);
        alertDialog.show();

    }

    private double calculate() {
        return Integer.parseInt(edtLorencHeight.getText().toString()) / 2 - 25;
    }
}

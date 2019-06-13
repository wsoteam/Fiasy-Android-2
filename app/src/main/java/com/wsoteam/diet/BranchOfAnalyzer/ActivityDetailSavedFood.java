package com.wsoteam.diet.BranchOfAnalyzer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amplitude.api.Amplitude;
import com.github.lzyzsd.circleprogress.DonutProgress;
import com.wsoteam.diet.AmplitudaEvents;
import com.wsoteam.diet.Authenticate.POJO.Box;
import com.wsoteam.diet.BranchOfAnalyzer.Dialogs.AddFoodDialog;
import com.wsoteam.diet.BranchOfAnalyzer.POJOEating.Breakfast;
import com.wsoteam.diet.BranchOfAnalyzer.POJOEating.Dinner;
import com.wsoteam.diet.BranchOfAnalyzer.POJOEating.Lunch;
import com.wsoteam.diet.BranchOfAnalyzer.POJOEating.Snack;
import com.wsoteam.diet.BranchOfAnalyzer.POJOFoodSQL.Food;
import com.wsoteam.diet.Config;
import com.wsoteam.diet.EntryPoint.ActivitySplash;
import com.wsoteam.diet.InApp.ActivitySubscription;
import com.wsoteam.diet.R;
import com.wsoteam.diet.Sync.WorkWithFirebaseDB;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ActivityDetailSavedFood extends AppCompatActivity {
    @BindView(R.id.tvActivityDetailOfFoodCollapsingTitle) TextView tvTitle;
    @BindView(R.id.tvActivityDetailOfFoodCalculateKcal) TextView tvCalculateKcal;
    @BindView(R.id.tvActivityDetailOfFoodCalculateFat) TextView tvCalculateFat;
    @BindView(R.id.tvActivityDetailOfFoodCalculateCarbo) TextView tvCalculateCarbohydrates;
    @BindView(R.id.tvActivityDetailOfFoodCalculateProtein) TextView tvCalculateProtein;
    @BindView(R.id.edtActivityDetailOfFoodPortion) EditText edtWeight;

    @BindView(R.id.tvCarbohydrates) TextView tvCarbohydrates;
    @BindView(R.id.tvFats) TextView tvFats;
    @BindView(R.id.tvProteins) TextView tvProteins;

    @BindView(R.id.bottom_sheet) LinearLayout bottomSheet;
    private BottomSheetBehavior bottomSheetBehavior;

    List<View> viewList;

    private final int BREAKFAST_POSITION = 0, LUNCH_POSITION = 1, DINNER_POSITION = 2, SNACK_POSITION = 3, EMPTY_FIELD = -1;
    private Food foodItem;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_saved_food);
        ButterKnife.bind(this);
        ButterKnife.apply(viewList, (view, value, index) -> view.setVisibility(value), View.GONE);
        foodItem = (Food) getIntent().getSerializableExtra(Config.INTENT_DETAIL_FOOD);
        bindFields();

        edtWeight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().equals(" ")
                        || charSequence.toString().equals("-")) {
                    edtWeight.setText("0");
                } else {
                    if (!edtWeight.getText().toString().equals("")) {
                        calculateMainParameters(charSequence);
                    } else {
                        tvCalculateProtein.setText("0 " + getString(R.string.gramm));
                        tvCalculateKcal.setText("0 " + getString(R.string.kcal));
                        tvCalculateCarbohydrates.setText("0 " + getString(R.string.gramm));
                        tvCalculateFat.setText("0 " + getString(R.string.gramm));
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        Amplitude.getInstance().logEvent(AmplitudaEvents.view_detail_food);

    }

    private void bindFields() {
        tvTitle.setText(foodItem.getName());
        tvFats.setText(String.valueOf(Math.round(foodItem.getFats() * 100)) + " г");
        tvCarbohydrates.setText(String.valueOf(Math.round(foodItem.getCarbohydrates() * 100)) + " г");
        tvProteins.setText(String.valueOf(Math.round(foodItem.getProteins() * 100)) + " г");
    }

    private boolean isPremiumUser() {
        SharedPreferences sharedPreferences = getSharedPreferences(Config.STATE_BILLING, MODE_PRIVATE);
        if (sharedPreferences.getBoolean(Config.STATE_BILLING, false)) {
            return true;
        } else {
            return false;
        }
    }

    private void savePortion(int idOfEating) {

        String wholeDate = getIntent().getStringExtra(Config.INTENT_DATE_FOR_SAVE);
        String[] arrayOfNumbersForDate = wholeDate.split("\\.");

        int day = Integer.parseInt(arrayOfNumbersForDate[0]);
        int month = Integer.parseInt(arrayOfNumbersForDate[1]) - 1;
        int year = Integer.parseInt(arrayOfNumbersForDate[2]);

        int kcal = Integer.parseInt(tvCalculateKcal.getText().toString().split(" ")[0]);
        int carbo = Integer.parseInt(tvCalculateCarbohydrates.getText().toString().split(" ")[0]);
        int prot = Integer.parseInt(tvCalculateProtein.getText().toString().split(" ")[0]);
        int fat = Integer.parseInt(tvCalculateFat.getText().toString().split(" ")[0]);

        int weight = Integer.parseInt(edtWeight.getText().toString());


        String name = foodItem.getName();
        String urlOfImage = "empty_url";

        Amplitude.getInstance().logEvent(AmplitudaEvents.success_add_food);
        switch (idOfEating) {
            case BREAKFAST_POSITION:
                WorkWithFirebaseDB.
                        addBreakfast(new Breakfast(name, urlOfImage, kcal, carbo, prot, fat, weight, day, month, year));
                break;
            case LUNCH_POSITION:
                WorkWithFirebaseDB.
                        addLunch(new Lunch(name, urlOfImage, kcal, carbo, prot, fat, weight, day, month, year));
                break;
            case DINNER_POSITION:
                WorkWithFirebaseDB.
                        addDinner(new Dinner(name, urlOfImage, kcal, carbo, prot, fat, weight, day, month, year));
                break;
            case SNACK_POSITION:
                WorkWithFirebaseDB.
                        addSnack(new Snack(name, urlOfImage, kcal, carbo, prot, fat, weight, day, month, year));
                break;
        }
        AlertDialog alertDialog = AddFoodDialog.createChoiseEatingAlertDialog(ActivityDetailSavedFood.this);
        alertDialog.show();
        getSharedPreferences(Config.IS_ADDED_FOOD, MODE_PRIVATE).edit().putBoolean(Config.IS_ADDED_FOOD, true).commit();
        new CountDownTimer(800, 100) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                alertDialog.dismiss();
                onBackPressed();
            }
        }.start();
    }


    private void calculateMainParameters(CharSequence stringPortion) {
        double portion = Double.parseDouble(stringPortion.toString());

        tvCalculateProtein.setText(String.valueOf(Math.round(portion * foodItem.getProteins())) + " " + getString(R.string.gramm));
        tvCalculateKcal.setText(String.valueOf(Math.round(portion * foodItem.getCalories())) + " " + getString(R.string.kcal));
        tvCalculateCarbohydrates.setText(String.valueOf(Math.round(portion * foodItem.getCarbohydrates())) + " " + getString(R.string.gramm));
        tvCalculateFat.setText(String.valueOf(Math.round(portion * foodItem.getFats())) + " " + getString(R.string.gramm));

    }


    @OnClick({R.id.btnSaveEating, R.id.ivBack, R.id.ibSheetClose, R.id.btnReg, R.id.btnPremCell, R.id.btnPremCholy,
            R.id.btnPremMonoUnSaturated, R.id.btnPremPolyUnSaturated, R.id.btnPremPot, R.id.btnPremSaturated, R.id.btnPremSod,
            R.id.btnPremSugar})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnSaveEating:
                if (edtWeight.getText().toString().equals("") || edtWeight.getText().toString().equals(" ")) {
                    Toast.makeText(ActivityDetailSavedFood.this, R.string.input_weight_of_eating, Toast.LENGTH_SHORT).show();
                } else {
                    savePortion(getIntent().getIntExtra(Config.TAG_CHOISE_EATING, 0));
                }
                break;
            case R.id.ivBack:
                onBackPressed();
                break;
        }
    }
}

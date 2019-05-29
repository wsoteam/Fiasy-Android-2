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

import com.adjust.sdk.Adjust;
import com.adjust.sdk.AdjustEvent;
import com.amplitude.api.Amplitude;
import com.github.lzyzsd.circleprogress.DonutProgress;
import com.wsoteam.diet.AmplitudaEvents;
import com.wsoteam.diet.BranchOfAnalyzer.Dialogs.AddFoodDialog;
import com.wsoteam.diet.BranchOfAnalyzer.POJOEating.Breakfast;
import com.wsoteam.diet.BranchOfAnalyzer.POJOEating.Dinner;
import com.wsoteam.diet.BranchOfAnalyzer.POJOEating.Lunch;
import com.wsoteam.diet.BranchOfAnalyzer.POJOEating.Snack;
import com.wsoteam.diet.BranchOfAnalyzer.POJOFoodSQL.CFood;
import com.wsoteam.diet.Config;
import com.wsoteam.diet.EventsAdjust;
import com.wsoteam.diet.EntryPoint.ActivitySplash;
import com.wsoteam.diet.POJOFoodItem.FoodItem;
import com.wsoteam.diet.R;
import com.wsoteam.diet.Sync.WorkWithFirebaseDB;
import com.yandex.metrica.YandexMetrica;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ActivityDetailOfFood extends AppCompatActivity {
    @BindView(R.id.tvActivityDetailOfFoodCollapsingTitle) TextView tvTitle;
    @BindView(R.id.pbActivityDetailOfFoodCarbo) DonutProgress pbCarbohydrates;
    @BindView(R.id.pbActivityDetailOfFoodFat) DonutProgress pbFat;
    @BindView(R.id.pbActivityDetailOfFoodProtein) DonutProgress pbProtein;
    @BindView(R.id.tvActivityDetailOfFoodCalculateKcal) TextView tvCalculateKcal;
    @BindView(R.id.tvActivityDetailOfFoodCalculateFat) TextView tvCalculateFat;
    @BindView(R.id.tvActivityDetailOfFoodCalculateCarbo) TextView tvCalculateCarbohydrates;
    @BindView(R.id.tvActivityDetailOfFoodCalculateProtein) TextView tvCalculateProtein;
    @BindView(R.id.edtActivityDetailOfFoodPortion) EditText edtWeight;


    @BindView(R.id.tvLabelCellulose) TextView tvLabelCellulose;
    @BindView(R.id.tvLabelSugar) TextView tvLabelSugar;
    @BindView(R.id.tvLabelFats) TextView tvLabelFats;
    @BindView(R.id.tvLabelSaturated) TextView tvLabelSaturated;
    @BindView(R.id.tvLabelMonoUnSaturated) TextView tvLabelMonoUnSaturated;
    @BindView(R.id.tvLabelPolyUnSaturated) TextView tvLabelPolyUnSaturated;
    @BindView(R.id.tvLabelСholesterol) TextView tvLabelСholesterol;
    @BindView(R.id.tvLabelSodium) TextView tvLabelSodium;
    @BindView(R.id.tvLabelPotassium) TextView tvLabelPotassium;

    @BindView(R.id.tvCarbohydrates) TextView tvCarbohydrates;
    @BindView(R.id.tvCellulose) TextView tvCellulose;
    @BindView(R.id.tvSugar) TextView tvSugar;
    @BindView(R.id.tvFats) TextView tvFats;
    @BindView(R.id.tvProteins) TextView tvProteins;
    @BindView(R.id.tvSaturated) TextView tvSaturated;
    @BindView(R.id.tvPolyUnSaturated) TextView tvPolyUnSaturated;
    @BindView(R.id.tvMonoUnSaturated) TextView tvMonoUnSaturated;
    @BindView(R.id.tvСholesterol) TextView tvСholesterol;
    @BindView(R.id.tvSodium) TextView tvSodium;
    @BindView(R.id.tvPotassium) TextView tvPotassium;

    @BindView(R.id.btnPremCell) TextView btnPremCell;
    @BindView(R.id.btnPremSugar) TextView btnPremSugar;
    @BindView(R.id.btnPremUnSaturated) TextView btnPremUnSaturated;
    @BindView(R.id.btnPremMonoUnSaturated) TextView btnPremMonoUnSaturated;
    @BindView(R.id.btnPremPolyUnSaturated) TextView btnPremPolyUnSaturated2;
    @BindView(R.id.btnPremCholy) TextView btnPremCholy;
    @BindView(R.id.btnPremSod) TextView btnPremSod;
    @BindView(R.id.btnPremPot) TextView btnPremPot;

    @BindView(R.id.bottom_sheet) LinearLayout bottomSheet;
    private BottomSheetBehavior bottomSheetBehavior;

    @BindViews({R.id.tvCellulose, R.id.tvSugar, R.id.tvSaturated, R.id.tvСholesterol, R.id.tvSodium,
            R.id.tvPotassium, R.id.tvMonoUnSaturated, R.id.tvPolyUnSaturated,
            R.id.tvLabelCellulose, R.id.tvLabelSugar, R.id.tvLabelSaturated, R.id.tvLabelMonoUnSaturated, R.id.tvLabelPolyUnSaturated,
            R.id.tvLabelСholesterol, R.id.tvLabelSodium, R.id.tvLabelPotassium, R.id.btnPremCell, R.id.btnPremSugar, R.id.btnPremUnSaturated,
            R.id.btnPremMonoUnSaturated, R.id.btnPremPolyUnSaturated, R.id.btnPremCholy, R.id.btnPremSod, R.id.btnPremPot})
    List<View> viewList;

    private final int BREAKFAST_POSITION = 0, LUNCH_POSITION = 1, DINNER_POSITION = 2, SNACK_POSITION = 3;
    private CFood foodItem;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_of_food);
        ButterKnife.bind(this);
        ButterKnife.apply(viewList, (view, value, index) -> view.setVisibility(value), View.GONE);

        foodItem = (CFood) getIntent().getSerializableExtra(Config.INTENT_DETAIL_FOOD);

        tvTitle.setText(foodItem.getName());
        tvFats.setText("0 г");
        tvCarbohydrates.setText("0 г");
        tvProteins.setText("0 г");

        calculateNumbersForProgressBars();

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
        AlertDialog alertDialog = AddFoodDialog.createChoiseEatingAlertDialog(ActivityDetailOfFood.this);
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

    private void calculateNumbersForProgressBars() {
        Double fat, carbohydrates, protein;
        String maxPercent = "100";
        int maxCountForProgressBar = 100;

        fat = Double.valueOf(foodItem.getPercentFats());
        carbohydrates = Double.valueOf(foodItem.getPercentCarbohydrates());
        protein = Double.valueOf(foodItem.getPercentProteins());


        if (fat > maxCountForProgressBar) {
            pbFat.setDonut_progress(maxPercent);
        } else {
            pbFat.setDonut_progress(String.valueOf(Math.round(fat)));
        }
        if (carbohydrates > maxCountForProgressBar) {
            pbProtein.setDonut_progress(maxPercent);
        } else {
            pbProtein.setDonut_progress(String.valueOf(Math.round(protein)));
        }
        if (protein > maxCountForProgressBar) {
            pbCarbohydrates.setDonut_progress(maxPercent);
        } else {
            pbCarbohydrates.setDonut_progress(String.valueOf(Math.round(carbohydrates)));
        }

    }

    @OnClick({R.id.btnSaveEating, R.id.ivBack, R.id.ibSheetClose, R.id.btnReg})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnSaveEating:
                if (edtWeight.getText().toString().equals("") || edtWeight.getText().toString().equals(" ")) {
                    Toast.makeText(ActivityDetailOfFood.this, R.string.input_weight_of_eating, Toast.LENGTH_SHORT).show();
                } else {
                    savePortion(getIntent().getIntExtra(Config.TAG_CHOISE_EATING, 0));
                }
                break;
            case R.id.ivBack:
                onBackPressed();
                break;
            case R.id.ibSheetClose:
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                break;
            case R.id.btnReg:
                AmplitudaEvents.logEventRegistration(AmplitudaEvents.reg_from_add_food);
                startActivity(new Intent(ActivityDetailOfFood.this, ActivitySplash.class)
                        .putExtra(Config.IS_NEED_REG, true)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));

                break;
        }
    }
}

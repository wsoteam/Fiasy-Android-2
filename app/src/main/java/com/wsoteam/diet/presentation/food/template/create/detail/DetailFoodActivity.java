package com.wsoteam.diet.presentation.food.template.create.detail;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.amplitude.api.Amplitude;
import com.bumptech.glide.Glide;
import com.wsoteam.diet.AmplitudaEvents;
import com.wsoteam.diet.Authenticate.POJO.Box;
import com.wsoteam.diet.BranchOfAnalyzer.Dialogs.ClaimForm;
import com.wsoteam.diet.BranchOfAnalyzer.POJOFoodSQL.Food;
import com.wsoteam.diet.BranchOfAnalyzer.templates.POJO.FoodTemplateHolder;
import com.wsoteam.diet.Config;
import com.wsoteam.diet.InApp.ActivitySubscription;
import com.wsoteam.diet.POJOProfile.FavoriteFood;
import com.wsoteam.diet.R;
import com.wsoteam.diet.Sync.UserDataHolder;
import com.wsoteam.diet.Sync.WorkWithFirebaseDB;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class DetailFoodActivity extends AppCompatActivity {
    private final int EMPTY_FIELD = -1;
    @BindView(R.id.btnAddFood) Button addButton;
    @BindView(R.id.tvActivityDetailOfFoodCollapsingTitle) TextView tvTitle;
    @BindView(R.id.tvActivityDetailOfFoodCalculateKcal) TextView tvCalculateKcal;
    @BindView(R.id.tvActivityDetailOfFoodCalculateFat) TextView tvCalculateFat;
    @BindView(R.id.tvActivityDetailOfFoodCalculateCarbo) TextView tvCalculateCarbohydrates;
    @BindView(R.id.tvActivityDetailOfFoodCalculateProtein) TextView tvCalculateProtein;
    @BindView(R.id.edtActivityDetailOfFoodPortion) EditText edtWeight;
    @BindView(R.id.ibAddFavorite) ImageButton ibAddFavorite;
    @BindView(R.id.tvLabelCellulose) TextView tvLabelCellulose;
    @BindView(R.id.tvLabelSugar) TextView tvLabelSugar;
    @BindView(R.id.tvLabelFats) TextView tvLabelFats;
    @BindView(R.id.tvLabelSaturated) TextView tvLabelSaturated;
    @BindView(R.id.tvLabelMonoUnSaturated) TextView tvLabelMonoUnSaturated;
    @BindView(R.id.tvLabelPolyUnSaturated) TextView tvLabelPolyUnSaturated;
    @BindView(R.id.tvLabelСholesterol) TextView tvLabelСholesterol;
    @BindView(R.id.tvLabelSodium) TextView tvLabelSodium;
    @BindView(R.id.tvLabelPotassium) TextView tvLabelPotassium;
    @BindView(R.id.tvBrand) TextView tvBrand;
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
    @BindView(R.id.tvDj) TextView tvDj;
    @BindView(R.id.tvKcal) TextView tvKcal;
    @BindView(R.id.btnPremCell) TextView btnPremCell;
    @BindView(R.id.btnPremSugar) TextView btnPremSugar;
    @BindView(R.id.btnPremSaturated) TextView btnPremSaturated;
    @BindView(R.id.btnPremMonoUnSaturated) TextView btnPremMonoUnSaturated;
    @BindView(R.id.btnPremPolyUnSaturated) TextView btnPremPolyUnSaturated;
    @BindView(R.id.btnPremCholy) TextView btnPremCholy;
    @BindView(R.id.btnPremSod) TextView btnPremSod;
    @BindView(R.id.btnPremPot) TextView btnPremPot;
    @BindView(R.id.cardView6) CardView cardView6;


    private Food foodItem;
    private boolean isFavorite = false;
    private FavoriteFood currentFavorite;

    private boolean isSendResult;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_food);
        ButterKnife.bind(this);

        int[] views = {R.id.tvCellulose, R.id.tvSugar, R.id.tvSaturated, R.id.tvСholesterol, R.id.tvSodium,
            R.id.tvPotassium, R.id.tvMonoUnSaturated, R.id.tvPolyUnSaturated,
            R.id.tvLabelCellulose, R.id.tvLabelSugar, R.id.tvLabelSaturated, R.id.tvLabelMonoUnSaturated, R.id.tvLabelPolyUnSaturated,
            R.id.tvLabelСholesterol, R.id.tvLabelSodium, R.id.tvLabelPotassium, R.id.btnPremCell, R.id.btnPremSugar, R.id.btnPremSaturated,
            R.id.btnPremMonoUnSaturated, R.id.btnPremPolyUnSaturated, R.id.btnPremCholy, R.id.btnPremSod, R.id.btnPremPot};

        for (int viewId : views) {
            View v = findViewById(viewId);

            if (v != null) {
                v.setVisibility(View.GONE);
            }
        }

        isSendResult = getIntent().getBooleanExtra(Config.SEND_RESULT_TO_BACK, false);
        int position = getIntent().getIntExtra(Config.INTENT_DETAIL_FOOD, 0);


        if (isSendResult) {
            foodItem = (Food) getIntent().getSerializableExtra(Config.INTENT_DETAIL_FOOD);
        } else {
            foodItem = FoodTemplateHolder.get().get(position);
        }

        bindFields();
        cardView6.setBackgroundResource(R.drawable.shape_calculate);
        getFavoriteFood();

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

        Amplitude.getInstance().logEvent(AmplitudaEvents.view_detail_food);

        String btnName = getIntent().getStringExtra(Config.DETAIL_FOOD_BTN_NAME);
        if (btnName != null){
            addButton.setText(btnName);
        }
    }

    private void getFavoriteFood() {
        if (UserDataHolder.getUserData() != null
                && UserDataHolder.getUserData().getFoodFavorites() != null
                && UserDataHolder.getUserData().getFoodFavorites().size() != 0) {
            Iterator iterator = UserDataHolder.getUserData().getFoodFavorites().entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry pair = (Map.Entry) iterator.next();
                if (((FavoriteFood) pair.getValue()).getFullInfo().equals(foodItem.getFullInfo())) {
                    currentFavorite = (FavoriteFood) pair.getValue();
                    isFavorite = true;
                    Glide.with(this).load(R.drawable.ic_fill_favorite).into(ibAddFavorite);
                }
            }

        }
    }


    private void bindFields() {
        tvTitle.setText(foodItem.getName().toUpperCase());
        tvFats.setText(String.valueOf(Math.round(foodItem.getFats() * 100)) + " г");
        tvCarbohydrates.setText(String.valueOf(Math.round(foodItem.getCarbohydrates() * 100)) + " г");
        tvProteins.setText(String.valueOf(Math.round(foodItem.getProteins() * 100)) + " г");
        tvKcal.setText(String.valueOf(Math.round(foodItem.getCalories() * 100)));
        tvDj.setText(String.valueOf(Math.round(foodItem.getKilojoules() * 100)));

        if (foodItem.getBrand() != null && !foodItem.getBrand().equals("")) {
            tvBrand.setText("(" + foodItem.getBrand() + ")");
        }

        if (foodItem.getSugar() != EMPTY_FIELD) {
            tvLabelSugar.setVisibility(View.VISIBLE);
            tvSugar.setVisibility(View.VISIBLE);
            tvSugar.setText(String.valueOf(Math.round(foodItem.getSugar() * 100)) + " г");
            if (!isPremiumUser()) {
                btnPremSugar.setVisibility(View.VISIBLE);
            }
        }
        if (foodItem.getSaturatedFats() != EMPTY_FIELD) {
            tvLabelSaturated.setVisibility(View.VISIBLE);
            tvSaturated.setVisibility(View.VISIBLE);
            tvSaturated.setText(String.valueOf(Math.round(foodItem.getSaturatedFats() * 100)) + " г");
            if (!isPremiumUser()) {
                btnPremSaturated.setVisibility(View.VISIBLE);
            }
        }
        if (foodItem.getMonoUnSaturatedFats() != EMPTY_FIELD) {
            tvLabelMonoUnSaturated.setVisibility(View.VISIBLE);
            tvMonoUnSaturated.setVisibility(View.VISIBLE);
            tvMonoUnSaturated.setText(String.valueOf(Math.round(foodItem.getMonoUnSaturatedFats() * 100)) + " г");
            if (!isPremiumUser()) {
                btnPremMonoUnSaturated.setVisibility(View.VISIBLE);
            }
        }
        if (foodItem.getPolyUnSaturatedFats() != EMPTY_FIELD) {
            tvLabelPolyUnSaturated.setVisibility(View.VISIBLE);
            tvPolyUnSaturated.setVisibility(View.VISIBLE);
            tvPolyUnSaturated.setText(String.valueOf(Math.round(foodItem.getPolyUnSaturatedFats() * 100)) + " г");
            if (!isPremiumUser()) {
                btnPremPolyUnSaturated.setVisibility(View.VISIBLE);
            }
        }
        if (foodItem.getCholesterol() != EMPTY_FIELD) {
            tvLabelСholesterol.setVisibility(View.VISIBLE);
            tvСholesterol.setVisibility(View.VISIBLE);
            tvСholesterol.setText(String.valueOf(Math.round(foodItem.getCholesterol() * 100)) + " мг");
            if (!isPremiumUser()) {
                btnPremCholy.setVisibility(View.VISIBLE);
            }
        }
        if (foodItem.getCellulose() != EMPTY_FIELD) {
            tvLabelCellulose.setVisibility(View.VISIBLE);
            tvCellulose.setVisibility(View.VISIBLE);
            tvCellulose.setText(String.valueOf(Math.round(foodItem.getCellulose() * 100)) + " г");
            if (!isPremiumUser()) {
                btnPremCell.setVisibility(View.VISIBLE);
            }
        }
        if (foodItem.getSodium() != EMPTY_FIELD) {
            tvLabelSodium.setVisibility(View.VISIBLE);
            tvSodium.setVisibility(View.VISIBLE);
            tvSodium.setText(String.valueOf(Math.round(foodItem.getSodium() * 100)) + " мг");
            if (!isPremiumUser()) {
                btnPremSod.setVisibility(View.VISIBLE);
            }
        }
        if (foodItem.getPottassium() != EMPTY_FIELD) {
            tvLabelPotassium.setVisibility(View.VISIBLE);
            tvPotassium.setVisibility(View.VISIBLE);
            tvPotassium.setText(String.valueOf(Math.round(foodItem.getPottassium() * 100)) + " мг");
            if (!isPremiumUser()) {
                btnPremPot.setVisibility(View.VISIBLE);
            }
        }

    }

    private boolean isPremiumUser() {
        SharedPreferences sharedPreferences = getSharedPreferences(Config.STATE_BILLING, MODE_PRIVATE);
        if (sharedPreferences.getBoolean(Config.STATE_BILLING, false)) {
            return true;
        } else {
            return false;
        }
    }


    private void calculateMainParameters(CharSequence stringPortion) {
        double portion = Double.parseDouble(stringPortion.toString());

        tvCalculateProtein.setText(String.valueOf(Math.round(portion * foodItem.getProteins())) + " " + getString(R.string.gramm));
        tvCalculateKcal.setText(String.valueOf(Math.round(portion * foodItem.getCalories())) + " " + getString(R.string.kcal));
        tvCalculateCarbohydrates.setText(String.valueOf(Math.round(portion * foodItem.getCarbohydrates())) + " " + getString(R.string.gramm));
        tvCalculateFat.setText(String.valueOf(Math.round(portion * foodItem.getFats())) + " " + getString(R.string.gramm));

    }

    @OnClick({R.id.btnAddFood, R.id.ivBack, R.id.btnPremCell, R.id.btnPremCholy,
            R.id.btnPremMonoUnSaturated, R.id.btnPremPolyUnSaturated, R.id.btnPremPot, R.id.btnPremSaturated, R.id.btnPremSod,
            R.id.btnPremSugar, R.id.tvSendClaim, R.id.ibShareFood, R.id.ibSendClaim, R.id.ibAddFavorite})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnAddFood:

                String weight = edtWeight.getText().toString();
                if (weight.equals("")
                        || weight.equals(" ")
                        || Integer.parseInt(weight) < 1) {

                    Toast.makeText(this, R.string.input_weight_of_eating, Toast.LENGTH_SHORT).show();

                } else {
                    Double portion = Double.parseDouble(edtWeight.getText().toString());
                    foodItem.setPortion(portion);

                        if (isSendResult){
                            Intent intent = new Intent();
                            intent.putExtra(Config.RECIPE_FOOD_INTENT, foodItem);
                            setResult(RESULT_OK, intent);
                        } else {

                        }
                    finish();
                }
                break;
            case R.id.ivBack:
                onBackPressed();
                break;
            case R.id.btnPremCell:
                showPremiumScreen();
                break;
            case R.id.btnPremCholy:
                showPremiumScreen();
                break;
            case R.id.btnPremMonoUnSaturated:
                showPremiumScreen();
                break;
            case R.id.btnPremPolyUnSaturated:
                showPremiumScreen();
                break;
            case R.id.btnPremPot:
                showPremiumScreen();
                break;
            case R.id.btnPremSaturated:
                showPremiumScreen();
                break;
            case R.id.btnPremSod:
                showPremiumScreen();
                break;
            case R.id.btnPremSugar:
                showPremiumScreen();
                break;
            case R.id.tvSendClaim:
                ClaimForm.createChoiseEatingAlertDialog(this, foodItem);
                break;
            case R.id.ibShareFood:
                shareFood(foodItem);
                break;
            case R.id.ibSendClaim:
                ClaimForm.createChoiseEatingAlertDialog(this, foodItem);
                break;
            case R.id.ibAddFavorite:
                if (isFavorite) {
                    isFavorite = false;
                    Glide.with(this).load(R.drawable.ic_empty_favorite).into(ibAddFavorite);
                    WorkWithFirebaseDB.deleteFavorite(currentFavorite.getKey());
                } else {
                    isFavorite = true;
                    Glide.with(this).load(R.drawable.ic_fill_favorite).into(ibAddFavorite);
                    currentFavorite = new FavoriteFood(foodItem.getId(), foodItem.getFullInfo(), addFavorite());
                }
                break;
        }
    }

    private void shareFood(Food foodItem) {
        String forSend;
        if (foodItem.getBrand() == null) {
            forSend = foodItem.getName()
                    + String.valueOf(foodItem.getCalories() * 100) + " Ккал. Узнайте % микроэлементов в продукте" +
                    " в дневнике питания Fiasy \n" + "https://play.google.com/store/apps/details?id=" + getPackageName();
        } else {
            forSend = foodItem.getName() + " (" + foodItem.getBrand() + ") - "
                    + String.valueOf(foodItem.getCalories() * 100) + " Ккал. Узнайте % микроэлементов в продукте" +
                    " в дневнике питания Fiasy \n" + "https://play.google.com/store/apps/details?id=" + getPackageName();
        }
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, forSend);
        startActivity(intent);
    }

    private void showPremiumScreen() {
        Intent intent = new Intent(this, ActivitySubscription.class);
        Box box = new Box(AmplitudaEvents.view_prem_elements, AmplitudaEvents.buy_prem_elements, false,
                true, null, false);
        intent.putExtra(Config.TAG_BOX, box);
        startActivity(intent);
    }

    private String addFavorite() {
        FavoriteFood favoriteFood = new FavoriteFood(foodItem.getId(), foodItem.getFullInfo(), "empty");
        String key = WorkWithFirebaseDB.addFoodFavorite(favoriteFood);
        return key;
    }

}

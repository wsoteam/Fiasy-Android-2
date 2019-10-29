package com.wsoteam.diet.Recipes.adding;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.wsoteam.diet.AmplitudaEvents;
import com.wsoteam.diet.Authenticate.POJO.Box;
import com.wsoteam.diet.BranchOfAnalyzer.Dialogs.ClaimForm;
import com.wsoteam.diet.BranchOfAnalyzer.POJOFoodSQL.Food;
import com.wsoteam.diet.Config;
import com.wsoteam.diet.InApp.ActivitySubscription;
import com.wsoteam.diet.POJOProfile.FavoriteFood;
import com.wsoteam.diet.R;
import com.wsoteam.diet.Sync.UserDataHolder;
import com.wsoteam.diet.Sync.WorkWithFirebaseDB;
import com.wsoteam.diet.common.Analytics.EventProperties;
import com.wsoteam.diet.common.Analytics.Events;

import com.wsoteam.diet.utils.DrawableUtilsKt;
import java.util.Iterator;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ActivityDetailFood extends AppCompatActivity {
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_food);
        ButterKnife.bind(this);

        final int[] viewList = new int[]{
            R.id.tvCellulose, R.id.tvSugar, R.id.tvSaturated, R.id.tvСholesterol, R.id.tvSodium,
            R.id.tvPotassium, R.id.tvMonoUnSaturated, R.id.tvPolyUnSaturated,
            R.id.tvLabelCellulose, R.id.tvLabelSugar, R.id.tvLabelSaturated, R.id.tvLabelMonoUnSaturated, R.id.tvLabelPolyUnSaturated,
            R.id.tvLabelСholesterol, R.id.tvLabelSodium, R.id.tvLabelPotassium, R.id.btnPremCell, R.id.btnPremSugar, R.id.btnPremSaturated,
            R.id.btnPremMonoUnSaturated, R.id.btnPremPolyUnSaturated, R.id.btnPremCholy, R.id.btnPremSod, R.id.btnPremPot
        };

        for (int viewId : viewList) {
            findViewById(viewId).setVisibility(View.GONE);
        }

        foodItem = (Food) getIntent().getSerializableExtra(Config.INTENT_DETAIL_FOOD);
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
                        tvCalculateProtein.setText("0 " + getString(R.string.g));
                        tvCalculateKcal.setText("0 " + getString(R.string.calories_unit));
                        tvCalculateCarbohydrates.setText("0 " + getString(R.string.g));
                        tvCalculateFat.setText("0 " + getString(R.string.g));
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


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
                final String fullInfo = ((FavoriteFood) pair.getValue()).getFullInfo();

                if (fullInfo != null && fullInfo.equals(foodItem.getFullInfo())) {
                    currentFavorite = (FavoriteFood) pair.getValue();
                    isFavorite = true;
                    Picasso.get().load(R.drawable.ic_fill_favorite).into(ibAddFavorite);
                }
            }

        }
    }


    private void bindFields() {
        tvTitle.setText(foodItem.getName().toUpperCase());
        tvFats.setText(String.format(getString(R.string.n_g), Math.round(foodItem.getFats() * 100)));
        tvCarbohydrates.setText(String.format(getString(R.string.n_g), Math.round(foodItem.getCarbohydrates() * 100)));
        tvProteins.setText(String.format(getString(R.string.n_g), Math.round(foodItem.getProteins() * 100)));
        tvKcal.setText(String.valueOf(Math.round(foodItem.getCalories() * 100)));
        tvDj.setText(String.valueOf(Math.round(foodItem.getKilojoules() * 100)));

        if (foodItem.getBrand() != null && !foodItem.getBrand().equals("")) {
            tvBrand.setText("(" + foodItem.getBrand() + ")");
        }

        if (foodItem.getSugar() != EMPTY_FIELD) {
            tvLabelSugar.setVisibility(View.VISIBLE);
            tvSugar.setVisibility(View.VISIBLE);
            tvSugar.setText(String.format(getString(R.string.n_g), Math.round(foodItem.getSugar() * 100)));
            if (!isPremiumUser()) {
                btnPremSugar.setVisibility(View.VISIBLE);
            }
        }
        if (foodItem.getSaturatedFats() != EMPTY_FIELD) {
            tvLabelSaturated.setVisibility(View.VISIBLE);
            tvSaturated.setVisibility(View.VISIBLE);
            tvSaturated.setText(String.format(getString(R.string.n_g), Math.round(foodItem.getSaturatedFats() * 100)));
            if (!isPremiumUser()) {
                btnPremSaturated.setVisibility(View.VISIBLE);
            }
        }
        if (foodItem.getMonoUnSaturatedFats() != EMPTY_FIELD) {
            tvLabelMonoUnSaturated.setVisibility(View.VISIBLE);
            tvMonoUnSaturated.setVisibility(View.VISIBLE);
            tvMonoUnSaturated.setText(String.format(getString(R.string.n_g), Math.round(foodItem.getMonoUnSaturatedFats() * 100)));
            if (!isPremiumUser()) {
                btnPremMonoUnSaturated.setVisibility(View.VISIBLE);
            }
        }
        if (foodItem.getPolyUnSaturatedFats() != EMPTY_FIELD) {
            tvLabelPolyUnSaturated.setVisibility(View.VISIBLE);
            tvPolyUnSaturated.setVisibility(View.VISIBLE);
            tvPolyUnSaturated.setText(String.format(getString(R.string.n_g), Math.round(foodItem.getPolyUnSaturatedFats() * 100)));
            if (!isPremiumUser()) {
                btnPremPolyUnSaturated.setVisibility(View.VISIBLE);
            }
        }
        if (foodItem.getCholesterol() != EMPTY_FIELD) {
            tvLabelСholesterol.setVisibility(View.VISIBLE);
            tvСholesterol.setVisibility(View.VISIBLE);
            tvСholesterol.setText(String.format(getString(R.string.n_mg), Math.round(foodItem.getCholesterol() * 100)));
            if (!isPremiumUser()) {
                btnPremCholy.setVisibility(View.VISIBLE);
            }
        }
        if (foodItem.getCellulose() != EMPTY_FIELD) {
            tvLabelCellulose.setVisibility(View.VISIBLE);
            tvCellulose.setVisibility(View.VISIBLE);
            tvCellulose.setText(String.format(getString(R.string.n_g), Math.round(foodItem.getCellulose() * 100)));
            if (!isPremiumUser()) {
                btnPremCell.setVisibility(View.VISIBLE);
            }
        }
        if (foodItem.getSodium() != EMPTY_FIELD) {
            tvLabelSodium.setVisibility(View.VISIBLE);
            tvSodium.setVisibility(View.VISIBLE);
            tvSodium.setText(String.format(getString(R.string.n_mg), Math.round(foodItem.getSodium() * 100)));
            if (!isPremiumUser()) {
                btnPremSod.setVisibility(View.VISIBLE);
            }
        }
        if (foodItem.getPottassium() != EMPTY_FIELD) {
            tvLabelPotassium.setVisibility(View.VISIBLE);
            tvPotassium.setVisibility(View.VISIBLE);
            tvPotassium.setText(String.format(getString(R.string.n_mg), Math.round(foodItem.getPottassium() * 100)));
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

        tvCalculateProtein.setText(String.valueOf(Math.round(portion * foodItem.getProteins())) + " " + getString(R.string.g));
        tvCalculateKcal.setText(String.valueOf(Math.round(portion * foodItem.getCalories())) + " " + getString(R.string.calories_unit));
        tvCalculateCarbohydrates.setText(String.valueOf(Math.round(portion * foodItem.getCarbohydrates())) + " " + getString(R.string.g));
        tvCalculateFat.setText(String.valueOf(Math.round(portion * foodItem.getFats())) + " " + getString(R.string.g));

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

                    foodItem.setKilojoules(foodItem.getKilojoules() * portion);
                    foodItem.setCalories(foodItem.getCalories() * portion);
                    foodItem.setProteins(foodItem.getProteins() * portion);
                    foodItem.setCarbohydrates(foodItem.getCarbohydrates() * portion);
                    foodItem.setSugar(foodItem.getSugar() * portion);
                    foodItem.setFats(foodItem.getFats() * portion);
                    foodItem.setSaturatedFats(foodItem.getSaturatedFats() * portion);
                    foodItem.setMonoUnSaturatedFats(foodItem.getMonoUnSaturatedFats() * portion);
                    foodItem.setPolyUnSaturatedFats(foodItem.getPolyUnSaturatedFats() * portion);
                    foodItem.setCholesterol(foodItem.getCholesterol() * portion);
                    foodItem.setCellulose(foodItem.getCellulose() * portion);
                    foodItem.setSodium(foodItem.getSodium() * portion);
                    foodItem.setPottassium(foodItem.getPottassium() * portion);

                    Intent intent = new Intent();
                    intent.putExtra(Config.RECIPE_FOOD_INTENT, foodItem);
                    setResult(RESULT_OK, intent);
                    finish();
                }
                break;
            case R.id.ivBack:
                onBackPressed();
                break;
            case R.id.btnPremCell:
            case R.id.btnPremPot:
            case R.id.btnPremSaturated:
            case R.id.btnPremPolyUnSaturated:
            case R.id.btnPremSod:
            case R.id.btnPremSugar:
            case R.id.btnPremMonoUnSaturated:
            case R.id.btnPremCholy:
                showPremiumScreen();
                break;
            case R.id.tvSendClaim:
            case R.id.ibSendClaim:
                ClaimForm.createChoiseEatingAlertDialog(this, foodItem);
                break;
            case R.id.ibShareFood:
                shareFood(foodItem);
                break;
            case R.id.ibAddFavorite:
                if (isFavorite) {
                    isFavorite = false;
                    ibAddFavorite.setImageDrawable(DrawableUtilsKt
                        .getVectorIcon(this, R.drawable.ic_empty_favorite));
                    WorkWithFirebaseDB.deleteFavorite(currentFavorite.getKey());
                } else {
                    isFavorite = true;
                    ibAddFavorite.setImageDrawable(DrawableUtilsKt
                        .getVectorIcon(this, R.drawable.ic_fill_favorite));
                    currentFavorite = new FavoriteFood(foodItem.getId(), foodItem.getFullInfo(), addFavorite());
                }
                break;
        }
    }

    private void shareFood(Food foodItem) {
        String forSend;
        if (foodItem.getBrand() == null) {
            forSend = foodItem.getName()
                    + (foodItem.getCalories() * 100)
                + getString(R.string.detail_food_activity_percent)
                + "https://play.google.com/store/apps/details?id=" + getPackageName();
        } else {
            forSend = foodItem.getName() + " (" + foodItem.getBrand() + ") - "
                    + (foodItem.getCalories() * 100)
                + getString(R.string.detail_food_activity_percent)
                + "https://play.google.com/store/apps/details?id=" + getPackageName();
        }
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, forSend);
        startActivity(intent);
    }

    private void showPremiumScreen() {
        Intent intent = new Intent(this, ActivitySubscription.class);
        Box box = new Box(AmplitudaEvents.view_prem_elements, EventProperties.trial_from_elements, false,
                true, null, false);
        intent.putExtra(Config.TAG_BOX, box);
        startActivity(intent);
    }

    private String addFavorite() {
      Events.logAddFavorite(foodItem.getName());
      FavoriteFood favoriteFood = new FavoriteFood(foodItem.getId(), foodItem.getFullInfo(), "empty", foodItem.getName(), foodItem.getBrand(),
          foodItem.getPortion(), foodItem.isLiquid(), foodItem.getKilojoules(), foodItem.getCalories(), foodItem.getProteins(), foodItem.getCarbohydrates(),
          foodItem.getSugar(), foodItem.getFats(), foodItem.getSaturatedFats(), foodItem.getMonoUnSaturatedFats(), foodItem.getPolyUnSaturatedFats(), foodItem.getCholesterol(),
          foodItem.getCellulose(), foodItem.getSodium(), foodItem.getPottassium(), foodItem.getPercentCarbohydrates(), foodItem.getPercentFats(), foodItem.getPercentProteins());
      String key = WorkWithFirebaseDB.addFoodFavorite(favoriteFood);
      return key;
    }

}

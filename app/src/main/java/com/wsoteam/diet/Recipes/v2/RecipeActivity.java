package com.wsoteam.diet.Recipes.v2;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amplitude.api.Amplitude;
import com.bumptech.glide.Glide;
import com.wsoteam.diet.AmplitudaEvents;
import com.wsoteam.diet.BranchOfAnalyzer.Dialogs.AddFoodDialog;
import com.wsoteam.diet.BranchOfAnalyzer.POJOEating.Breakfast;
import com.wsoteam.diet.BranchOfAnalyzer.POJOEating.Dinner;
import com.wsoteam.diet.BranchOfAnalyzer.POJOEating.Lunch;
import com.wsoteam.diet.BranchOfAnalyzer.POJOEating.Snack;
import com.wsoteam.diet.Config;
import com.wsoteam.diet.R;
import com.wsoteam.diet.Recipes.POJO.RecipeItem;
import com.wsoteam.diet.Sync.WorkWithFirebaseDB;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RecipeActivity extends AppCompatActivity {
    @BindView(R.id.ivHead) ImageView ivHead;
    @BindView(R.id.tvName) TextView tvName;
    @BindView(R.id.tvTime) TextView tvTime;
    @BindView(R.id.llIngedientsItem) LinearLayout llIngredients;
    @BindView(R.id.llInstructions) LinearLayout llInstructions;
    @BindView(R.id.tvCarbohydrates) TextView tvCarbohydrates;
    @BindView(R.id.tvCellulose) TextView tvCellulose;
    @BindView(R.id.tvSugar) TextView tvSugar;
    @BindView(R.id.tvFat) TextView tvFat;
    @BindView(R.id.tvSaturatedFats) TextView tvSaturatedFats;
    @BindView(R.id.tvUnSaturatedFats) TextView tvUnSaturatedFats;
    @BindView(R.id.tvProtein) TextView tvProtein;
    @BindView(R.id.tvCholesterol) TextView tvCholesterol;
    @BindView(R.id.tvSodium) TextView tvSodium;
    @BindView(R.id.tvPotassium) TextView tvPotassium;Window window;
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.tvRecipeKK) TextView tvKkal;

    private final int BREAKFAST_POSITION = 0, LUNCH_POSITION = 1, DINNER_POSITION = 2, SNACK_POSITION = 3, EMPTY_FIELD = -1;
    RecipeItem recipeItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_plans_v2);
        ButterKnife.bind(this);

        window =getWindow();
        window.getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        window.setStatusBarColor(Color.parseColor("#32000000"));
        recipeItem = (RecipeItem) getIntent().getSerializableExtra(Config.RECIPE_INTENT);

        tvKkal.setText(recipeItem.getCalories() + " ккал на порцию");
        tvName.setText(recipeItem.getName());
        tvTime.setText(String.valueOf(recipeItem.getTime()));
        tvCarbohydrates.setText(String.valueOf(recipeItem.getCarbohydrates()));
        tvCellulose.setText(String.valueOf(recipeItem.getCellulose()));
        tvSugar.setText(String.valueOf(recipeItem.getSugar()));
        tvFat.setText(String.valueOf(recipeItem.getFats()));
        tvSaturatedFats.setText(String.valueOf(recipeItem.getSaturatedFats()));
        tvUnSaturatedFats.setText(String.valueOf(recipeItem.getUnSaturatedFats()));
        tvProtein.setText(String.valueOf(recipeItem.getPortions()));
        tvCholesterol.setText(String.valueOf(recipeItem.getCholesterol()));
        tvSodium.setText(String.valueOf(recipeItem.getSodium()));
        tvPotassium.setText(String.valueOf(recipeItem.getPortions()));


//        mToolbar.inflateMenu(R.menu.toolbar_menu);
        mToolbar.setTitleTextColor(0xFFFFFFFF);
        mToolbar.setBackgroundColor(Color.parseColor("#00FFFFFF"));
        Menu menu = mToolbar.getMenu();

        mToolbar.setNavigationIcon(R.drawable.back_arrow_icon_white);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        for (String ingredient :
                recipeItem.getIngredients()) {
            View view = getLayoutInflater().inflate(R.layout.plan_recipes_ingredient, null);
            TextView textView = view.findViewById(R.id.tvIngredient);
            textView.setText(ingredient);
            llIngredients.addView(view);
        }

        for (String instruction :
                recipeItem.getInstruction()) {
            View view = getLayoutInflater().inflate(R.layout.plan_recipes_instruction, null);
            TextView textView = view.findViewById(R.id.tvInstruction);
            textView.setText(instruction);
            llInstructions.addView(view);
        }

        String url = recipeItem.getUrl();
        if (url == null || url.equals("link")) {
            url = "https://firebasestorage.googleapis.com/v0/b/diet-for-test.appspot.com/o/loading.jpg?alt=media&token=f1b6fe6d-57e3-4bca-8be3-9ebda9dc715e";
        }

        Glide.with(this).load(url).into(ivHead);

    }

    @OnClick(R.id.addDiary)
    public void onViewClicked(View view) {

        savePortion(DINNER_POSITION, recipeItem);
    }

    private void savePortion(int idOfEating, RecipeItem recipe) {

//        String wholeDate = getIntent().getStringExtra(Config.INTENT_DATE_FOR_SAVE);
//        String[] arrayOfNumbersForDate = wholeDate.split("\\.");

        int day = 10;
        int month = 06;
        int year = 2019;

        int kcal = recipe.getCalories();
        int carbo = (int) recipe.getCarbohydrates();
        int prot = recipe.getPortions();
        int fat = (int) recipe.getFats();

        int weight = -1;


        String name = recipe.getName();
        String urlOfImage = recipe.getUrl();

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
        AlertDialog alertDialog = AddFoodDialog.createChoiseEatingAlertDialog(this);
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

}

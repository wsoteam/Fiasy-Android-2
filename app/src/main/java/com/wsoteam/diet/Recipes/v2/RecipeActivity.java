package com.wsoteam.diet.Recipes.v2;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wsoteam.diet.Config;
import com.wsoteam.diet.R;
import com.wsoteam.diet.Recipes.POJO.RecipeItem;

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
        RecipeItem recipeItem = (RecipeItem) getIntent().getSerializableExtra(Config.RECIPE_INTENT);

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

}

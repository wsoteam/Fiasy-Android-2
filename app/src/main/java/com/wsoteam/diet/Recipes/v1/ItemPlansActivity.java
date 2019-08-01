package com.wsoteam.diet.Recipes.v1;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
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

public class ItemPlansActivity extends AppCompatActivity {
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
    @BindView(R.id.tvPotassium) TextView tvPotassium;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_plans);
        ButterKnife.bind(this);

        RecipeItem recipeItem = (RecipeItem) getIntent().getSerializableExtra(Config.RECIPE_INTENT);

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

    @OnClick({R.id.backButton})
    public void onViewClicked(View view) {
        onBackPressed();
    }
}

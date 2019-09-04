package com.wsoteam.diet.Recipes.v1;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.wsoteam.diet.Config;
import com.wsoteam.diet.InApp.ActivitySubscription;
import com.wsoteam.diet.R;
import com.wsoteam.diet.Recipes.POJO.RecipeItem;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RecipeWithoutPremActivity extends AppCompatActivity {


    @BindView(R.id.ivRecipe) ImageView imageView;
    RecipeItem recipeItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_without_prem);
        ButterKnife.bind(this);

        recipeItem = (RecipeItem) getIntent().getSerializableExtra(Config.RECIPE_INTENT);
        String url = recipeItem.getUrl();

        if (url == null || url.equals("link")) {
            url = "https://firebasestorage.googleapis.com/v0/b/diet-for-test.appspot.com/o/loading.jpg?alt=media&token=f1b6fe6d-57e3-4bca-8be3-9ebda9dc715e";
        }

        Glide.with(this).load(url).into(imageView);
    }

    @OnClick({R.id.xBackButtonItemRecipe, R.id.goPrem})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.xBackButtonItemRecipe:
                onBackPressed();
                break;
            case R.id.goPrem:
                startActivity(new Intent(this, ActivitySubscription.class));
                finish();
                break;
        }}
}

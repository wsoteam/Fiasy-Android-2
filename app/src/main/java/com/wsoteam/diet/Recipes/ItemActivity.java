package com.wsoteam.diet.Recipes;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Layout;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wsoteam.diet.Config;
import com.wsoteam.diet.POJOS.ItemRecipes;
import com.wsoteam.diet.R;

public class ItemActivity extends AppCompatActivity {

    private ItemRecipes itemRecipes;
    private ImageView imageView;
    private TextView bodyTextView;
    private TextView nameTextView;
    private TextView premTextView;
    private Button arrowBackButton;
    private Button xBackButton;
    private Button premButton;
    private View.OnClickListener listener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_recipe);

        listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        };

        bodyTextView = findViewById(R.id.tvRecipe);
        nameTextView = findViewById(R.id.tvRecipeNmae);
        imageView = findViewById(R.id.ivRecipe);
        arrowBackButton = findViewById(R.id.backButtonItemRecipe);
        xBackButton = findViewById(R.id.xBackButtonItemRecipe);
        premButton = findViewById(R.id.goPrem);
        premTextView = findViewById(R.id.tvPremRecipe);

        itemRecipes = (ItemRecipes)getIntent().getSerializableExtra(Config.RECIPE_INTENT);

        if (checkSubscribe()){
            //subscription purchased
            xBackButton.setVisibility(View.INVISIBLE);
            premButton.setVisibility(View.INVISIBLE);
            premTextView.setVisibility(View.INVISIBLE);
            bodyTextView.setText(Html.fromHtml(itemRecipes.getBody()));
            nameTextView.setText(Html.fromHtml(itemRecipes.getName()));
            arrowBackButton.setOnClickListener(listener);
        } else {
            //subscription not purchased
            arrowBackButton.setVisibility(View.INVISIBLE);
            nameTextView.setVisibility(View.INVISIBLE);
            xBackButton.setOnClickListener(listener);
        }

        Glide
                .with(this)
                .load(itemRecipes.getUrl())
                .into(imageView);

    }

    private boolean checkSubscribe() {
        SharedPreferences sharedPreferences = getSharedPreferences(Config.STATE_BILLING, MODE_PRIVATE);
        if (sharedPreferences.getBoolean(Config.STATE_BILLING, false)) {
            return true;
        } else {
            return false;
        }
    }
}

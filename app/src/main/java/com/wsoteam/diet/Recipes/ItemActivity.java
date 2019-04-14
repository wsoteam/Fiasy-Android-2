package com.wsoteam.diet.Recipes;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
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
    private CollapsingToolbarLayout collapsingToolbarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_recipe);

        bodyTextView = findViewById(R.id.tvRecipe);
//        nameTextView = findViewById(R.id.tvRecipeNmae);
        imageView = findViewById(R.id.ivRecipe);
        collapsingToolbarLayout = findViewById(R.id.collapsingToolbar);
        itemRecipes = (ItemRecipes)getIntent().getSerializableExtra(Config.RECIPE_INTENT);


//        nameTextView.setText(Html.fromHtml(itemRecipes.getName()));
        collapsingToolbarLayout.setTitle(itemRecipes.getName());
        bodyTextView.setText(Html.fromHtml(itemRecipes.getBody()));
        Glide
                .with(this)
                .load(itemRecipes.getUrl())
                .into(imageView);

    }
}

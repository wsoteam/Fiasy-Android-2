package com.wsoteam.diet.Recipes.v2;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wsoteam.diet.AmplitudaEvents;
import com.wsoteam.diet.Authenticate.POJO.Box;
import com.wsoteam.diet.Config;
import com.wsoteam.diet.InApp.ActivitySubscription;
import com.wsoteam.diet.R;
import com.wsoteam.diet.Recipes.POJO.RecipeItem;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class BlockedRecipeActivity extends AppCompatActivity  implements Toolbar.OnMenuItemClickListener{

    @BindView(R.id.ivHead) ImageView ivHead;
    @BindView(R.id.tvName) TextView tvName;
    @BindView(R.id.tvRecipeKK) TextView tvKkal;
    @BindView(R.id.tvTime) TextView tvTime;
    @BindView(R.id.toolbar) Toolbar mToolbar;
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
    RecipeItem recipeItem;
    Window window;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blocked_recipe);
        ButterKnife.bind(this);
        window = getWindow();
        window.getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        window.setStatusBarColor(Color.parseColor("#66000000"));
        recipeItem = (RecipeItem) getIntent().getSerializableExtra(Config.RECIPE_INTENT);

        mToolbar.setTitleTextColor(0xFFFFFFFF);
        mToolbar.setPadding(0, dpToPx(24), 0, 0);
        mToolbar.setBackgroundColor(Color.parseColor("#32000000"));
        Menu menu = mToolbar.getMenu();

//        mToolbar.inflateMenu(R.menu.recipe_menu);
        mToolbar.setOnMenuItemClickListener(this);
        mToolbar.setNavigationIcon(R.drawable.back_arrow_icon_white);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


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

        String url = recipeItem.getUrl();
        if (url == null || url.equals("link")) {
            url = "https://firebasestorage.googleapis.com/v0/b/diet-for-test.appspot.com/o/loading.jpg?alt=media&token=f1b6fe6d-57e3-4bca-8be3-9ebda9dc715e";
        }

        Glide.with(this).load(url).into(ivHead);

        int indexInstruction = 0;
        int borderInstruction = recipeItem.getInstruction().size();
        for (String instruction :
                recipeItem.getInstruction()) {
            indexInstruction++;
            View view = getLayoutInflater().inflate(R.layout.plan_recipes_instruction, null);
            View line = getLayoutInflater().inflate(R.layout.line_horizontal, null);
            line.setPadding(dpToPx(70), 0, 0, 0);
            TextView textView = view.findViewById(R.id.tvInstruction);
            textView.setText(instruction);
            llInstructions.addView(view);
            if (indexInstruction < borderInstruction){ llInstructions.addView(line);}
        }

    }

    @OnClick(R.id.goPrem)
    public void onViewClicked(View view) {
        Box box = new Box();
        box.setSubscribe(false);
        box.setOpenFromPremPart(true);
        box.setOpenFromIntrodaction(false);
        box.setComeFrom(AmplitudaEvents.view_prem_recipe);
        box.setComeFrom(AmplitudaEvents.buy_prem_recipe);
        Intent intent = new Intent(this, ActivitySubscription.class).putExtra(Config.TAG_BOX, box);
        startActivity(intent);
    }

    public int dpToPx(int dp) {
        float density = getResources()
                .getDisplayMetrics()
                .density;
        return Math.round((float) dp * density);
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.mShare:

                return true;
            case R.id.mFavorites:

                return true;
        }
        return false;

    }

}

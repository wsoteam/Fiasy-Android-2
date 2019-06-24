package com.wsoteam.diet.Recipes.adding;


import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator;
import com.wsoteam.diet.BranchOfAnalyzer.POJOFoodSQL.Food;
import com.wsoteam.diet.R;
import com.wsoteam.diet.Recipes.POJO.RecipeItem;
import com.wsoteam.diet.Recipes.adding.pages.IngredientsFragment;
import com.wsoteam.diet.Recipes.adding.pages.InstructionsFragment;
import com.wsoteam.diet.Recipes.adding.pages.MainFragment;
import com.wsoteam.diet.Recipes.adding.pages.ResultFragment;
import com.wsoteam.diet.Sync.WorkWithFirebaseDB;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddingRecipeActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.btnLeft) Button btnBack;
    @BindView(R.id.btnRight) Button btnNext;
    @BindView(R.id.btnOk) Button btnOk;
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.worm_dots_indicator) WormDotsIndicator wormDotsIndicator;
    @BindView(R.id.vpContainer) ViewPager vpPager;
    private FragmentPagerAdapter adapterViewPager;

    private List<Fragment> fragmentList;

    private RecipeItem recipeItem;
    private List<Food> foods;

    private Window window;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding_recipe);
        ButterKnife.bind(this);

        List<String> instruction = new ArrayList<>();

        recipeItem = new RecipeItem();
        recipeItem.setInstruction(instruction);
        foods = new ArrayList<>();

        fragmentList = new LinkedList<>();
        fragmentList.add(new MainFragment());
        fragmentList.add(new IngredientsFragment());
        fragmentList.add(new InstructionsFragment());
        fragmentList.add(new ResultFragment());


        adapterViewPager = new MyPagerAdapter(getSupportFragmentManager(), fragmentList);
        vpPager.setAdapter(adapterViewPager);
        vpPager.setOffscreenPageLimit(4);

        vpPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

                switch (i){
                    case 0:
                        btnBack.setVisibility(View.INVISIBLE);
                        btnNext.setVisibility(View.VISIBLE);
                        btnOk.setVisibility(View.GONE);
                        mToolbar.setTitle("Создать рецепт");
                        break;
                    case 1:
                        btnBack.setVisibility(View.VISIBLE);
                        btnNext.setVisibility(View.VISIBLE);
                        btnOk.setVisibility(View.GONE);
                        mToolbar.setTitle("Ингридиенты для 1 порции");
                        break;
                    case 2:
                        btnBack.setVisibility(View.VISIBLE);
                        btnNext.setVisibility(View.VISIBLE);
                        btnOk.setVisibility(View.GONE);
                        mToolbar.setTitle("Инструкция по приготовлению");
                        break;
                    case 3:
                        btnBack.setVisibility(View.VISIBLE);
                        btnNext.setVisibility(View.INVISIBLE);
                        btnOk.setVisibility(View.VISIBLE);
                        mToolbar.setTitle("Сохранить рецепт");
                        break;
                }

            }

            @Override
            public void onPageSelected(int i) {
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });

        wormDotsIndicator.setDotsClickable(false);
        wormDotsIndicator.setViewPager(vpPager);

        window = getWindow();
        window.getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        window.setStatusBarColor(Color.parseColor("#32000000"));

        mToolbar.setTitle(R.string.recipeToolBarTitle);
        mToolbar.inflateMenu(R.menu.adding_recipe_menu);
        mToolbar.setTitleTextColor(0xFFFFFFFF);

        Menu menu = mToolbar.getMenu();
        MenuItem btnClose = menu.findItem(R.id.close);
        btnClose.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                onBackPressed();
                return false;
            }
        });
    }

    public RecipeItem getRecipeItem(){
        return recipeItem;
    }

    public List<Food> getFoods(){
        return foods;
    }

    @OnClick({R.id.btnLeft, R.id.btnRight, R.id.btnOk})
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.btnLeft:
                vpPager.setCurrentItem(vpPager.getCurrentItem() - 1);
                break;
            case R.id.btnRight:
                vpPager.setCurrentItem(vpPager.getCurrentItem() + 1);
                break;
            case R.id.btnOk:
                saveRecipe(recipeItem, foods);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.mainLayout:
                vpPager.setCurrentItem(0);
                break;
            case R.id.ingredientsLayoutIn:
                vpPager.setCurrentItem(1);
                break;
            case R.id.instructionsLayoutIn:
                vpPager.setCurrentItem(2);
                break;
        }
    }

    public static class MyPagerAdapter extends FragmentPagerAdapter {
        List<Fragment> fragmentList;

        public MyPagerAdapter(FragmentManager fragmentManager, List<Fragment> fragmentList) {
            super(fragmentManager);
            this.fragmentList = fragmentList;
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "";
        }

    }

    public void saveRecipe(RecipeItem recipeItem, List<Food> foods){

        double prot = 0.0, fats = 0.0, carbo = 0.0, cal = 0.0, portion = 0.0;
        List<String> ingredients = new ArrayList<>();
        for (Food food:
             foods) {
            ingredients.add(food.getName());
            prot = prot + food.getProteins();
            fats = fats + food.getFats();
            carbo = carbo + food.getCarbohydrates();
            cal = cal + food.getCalories();
            portion = portion + food.getPortion();
        }

        recipeItem.setIngredients(ingredients);
        recipeItem.setProteins((int) prot);
        recipeItem.setFats((int) fats);
        recipeItem.setCarbohydrates((int) carbo);
        recipeItem.setCalories((int) cal);
        recipeItem.setPortions((int) portion);
        WorkWithFirebaseDB.addUserRecipe(recipeItem);

        AlertDialog alertDialog = AddRecipeAlertDialog.createChoiceEatingAlertDialog(this);
        alertDialog.show();
        new CountDownTimer(800, 100) {
            @Override
            public void onTick(long millisUntilFinished) { }

            @Override
            public void onFinish() {
                alertDialog.dismiss();
                onBackPressed();
            }
        }.start();
    }

    public void updateUI(){
        ((ResultFragment)fragmentList.get(3)).updateUI();
    }
}

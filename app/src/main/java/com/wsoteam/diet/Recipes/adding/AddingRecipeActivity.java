package com.wsoteam.diet.Recipes.adding;


import android.graphics.Color;
import android.os.CountDownTimer;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator;
import com.wsoteam.diet.BranchOfAnalyzer.POJOFoodSQL.Food;
import com.wsoteam.diet.R;
import com.wsoteam.diet.Recipes.POJO.RecipeItem;
import com.wsoteam.diet.Recipes.adding.pages.IngredientsFragment;
import com.wsoteam.diet.Recipes.adding.pages.InstructionsFragment;
import com.wsoteam.diet.Recipes.adding.pages.MainFragment;
import com.wsoteam.diet.Recipes.adding.pages.ResultFragment;
import com.wsoteam.diet.Sync.WorkWithFirebaseDB;
import com.wsoteam.diet.common.Analytics.EventProperties;
import com.wsoteam.diet.common.Analytics.Events;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddingRecipeActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

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
    private boolean isShare;

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

//        vpPager.onInterceptTouchEvent(null);
//        vpPager.onTouchEvent(null);

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

        mToolbar.setTitle(R.string.createRecipe);
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

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        isShare = isChecked;
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

        int portion = recipeItem.getPortions();

        if (portion < 1){
            recipeItem.setPortions(1);
            portion = 1;
        }


        if (!check()){
            return;
        }

        double prot = 0.0,
                fats = 0.0,
                carbo = 0.0,
                cal = 0.0,
                weight = 0.0,

                cellulose =0.0,
                sugar = 0.0,
                saturFats = 0.0,
                unsaturFats = 0.0,
                cholesterol = 0.0,
                sodium = 0.0,
                potassium = 0.0;


        List<String> ingredients = new ArrayList<>();
        for (Food food:
             foods) {
            ingredients.add(food.getName() + " (" + ((int)food.getPortion() / portion) + "г)");
            prot = prot + food.getProteins();
            fats = fats + checkValue(food.getFats());
            carbo = carbo + checkValue(food.getCarbohydrates());
            cal = cal + checkValue(food.getCalories());
            weight = weight + checkValue(food.getPortion());

            cellulose = cellulose + checkValue(food.getCellulose());
            sugar = sugar + checkValue(food.getSugar());
            saturFats = saturFats + checkValue(food.getSaturatedFats());
            unsaturFats = unsaturFats + checkValue(food.getMonoUnSaturatedFats());
            cholesterol = cholesterol + checkValue(food.getCholesterol());
            sodium = sodium + checkValue(food.getSodium());
            potassium = potassium + checkValue(food.getPottassium());
        }

        recipeItem.setIngredients(ingredients);
        recipeItem.setProteins((int) prot);
        recipeItem.setFats((int) fats);
        recipeItem.setCarbohydrates((int) carbo);
        recipeItem.setCalories((int) cal);


        recipeItem.setCellulose((int) cellulose);
        recipeItem.setSugar((int) sugar);
        recipeItem.setSaturatedFats((int) saturFats);
        recipeItem.setUnSaturatedFats((int) unsaturFats);
        recipeItem.setCholesterol((int) cholesterol);
        recipeItem.setSodium((int) sodium);
        recipeItem.setPotassium((int) potassium);

        recipeItem.setName(recipeItem.getName().trim());

        recipeItem.setUrl("https://firebasestorage.googleapis.com/v0/b/diet-for-test.appspot.com/o/default_recipe.png?alt=media&token=1fcf855f-fa9d-4831-9ff2-af204a612707");

        WorkWithFirebaseDB.addUserRecipe(recipeItem);
        Events.logCreateRecipe(getIntent().getStringExtra(EventProperties.recipe_from));
        if (isShare) {
            WorkWithFirebaseDB.addUsersSharedRecipe(recipeItem);
        }

        Toast.makeText(getApplicationContext(),
                "Рецепт сохранен в разделе Рецепты/Свои рецепты", Toast.LENGTH_SHORT).show();

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

    private double checkValue(Double value){
        if (value > 0){
            return value;
        } else{
            return 0.0;
        }
    }

    private boolean check(){
        if (recipeItem.getName() == null || recipeItem.getName().trim().length() < 1){
            Toast.makeText(getApplicationContext(),
                    "Введите название рецепта!", Toast.LENGTH_SHORT).show();
            vpPager.setCurrentItem(0);
            return false;
        } else if (recipeItem.getTime() < 1 ){
            Toast.makeText(getApplicationContext(),
                    "Введите время нужное для готовки!", Toast.LENGTH_SHORT).show();
            vpPager.setCurrentItem(0);
            return false;

        }else if (foods == null || foods.size() < 1 ){
            Toast.makeText(getApplicationContext(),
                    "Введите ингридиенты", Toast.LENGTH_SHORT).show();
            vpPager.setCurrentItem(1);
            return false;
        } else if (recipeItem.getInstruction() == null || recipeItem.getInstruction().size() < 1 ){
            Toast.makeText(getApplicationContext(),
                    "Введите шаги приготовления", Toast.LENGTH_SHORT).show();
            vpPager.setCurrentItem(2);
            return false;
        } else {
            return true;
        }
    }

    public void updateUI(){
        ((ResultFragment)fragmentList.get(3)).updateUI();
    }
}

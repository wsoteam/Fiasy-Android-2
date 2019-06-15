package com.wsoteam.diet.Recipes.adding;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;


import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator;
import com.wsoteam.diet.R;
import com.wsoteam.diet.Recipes.POJO.RecipeItem;

import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddingRecipeActivity extends AppCompatActivity {

    @BindView(R.id.btnLeft) Button btnBack;
    @BindView(R.id.btnRight) Button btnNext;
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.worm_dots_indicator) WormDotsIndicator wormDotsIndicator;
    @BindView(R.id.vpContainer) ViewPager vpPager;
    private FragmentPagerAdapter adapterViewPager;

    private List<Fragment> fragmentList;

    private RecipeItem recipeItem;

    private Window window;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding_recipe);
        ButterKnife.bind(this);

        recipeItem = new RecipeItem();

        fragmentList = new LinkedList<>();
        fragmentList.add(new MainFragment());
        fragmentList.add(new InstructionsFragment());
        fragmentList.add(new IngredientsFragment());
        fragmentList.add(new ResultFragment());


        adapterViewPager = new MyPagerAdapter(getSupportFragmentManager(), fragmentList);
        vpPager.setAdapter(adapterViewPager);

        vpPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });


        vpPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

                switch (i){
                    case 0:
                        if(recipeItem.getName() != null && recipeItem.getName().trim().length() == 0){
                            vpPager.setCurrentItem(0);
                        }
                        mToolbar.setTitle("123");
                        break;
                    case 1:
                        mToolbar.setTitle("456");
                        break;
                }


                if (fragmentList.size() == 1){
                    btnBack.setVisibility(View.INVISIBLE);
                    btnNext.setVisibility(View.INVISIBLE);
                } else if (i == 0) {
                    btnBack.setVisibility(View.INVISIBLE);
                    btnNext.setVisibility(View.VISIBLE);
                } else if (i == fragmentList.size() - 1){
                    btnNext.setVisibility(View.INVISIBLE);
                    btnBack.setVisibility(View.VISIBLE);

                } else {
                    btnBack.setVisibility(View.VISIBLE);
                    btnNext.setVisibility(View.VISIBLE);
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

    @OnClick({R.id.btnLeft, R.id.btnRight})
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.btnLeft:
                vpPager.setCurrentItem(vpPager.getCurrentItem() - 1);
                break;
            case R.id.btnRight:
                vpPager.setCurrentItem(vpPager.getCurrentItem() + 1);
                break;
        }
    }

    public static class MyPagerAdapter extends FragmentPagerAdapter {
        List<Fragment> fragmentList;

        public MyPagerAdapter(FragmentManager fragmentManager, List<Fragment> fragmentList) {
            super(fragmentManager);
            this.fragmentList =fragmentList;
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

}

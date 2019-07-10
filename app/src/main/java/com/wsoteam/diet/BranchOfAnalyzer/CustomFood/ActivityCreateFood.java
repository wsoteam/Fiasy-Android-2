package com.wsoteam.diet.BranchOfAnalyzer.CustomFood;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.wsoteam.diet.BarcodeScanner.BaseScanner;
import com.wsoteam.diet.BranchOfAnalyzer.Controller.CustomFoodViewPagerAdapter;
import com.wsoteam.diet.BranchOfAnalyzer.CustomFood.Fragments.FragmentMainInfo;
import com.wsoteam.diet.BranchOfAnalyzer.CustomFood.Fragments.FragmentOutlay;
import com.wsoteam.diet.BranchOfAnalyzer.CustomFood.Fragments.FragmentResult;
import com.wsoteam.diet.R;
import com.wsoteam.diet.Sync.WorkWithFirebaseDB;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ActivityCreateFood extends AppCompatActivity {

    public CustomFood customFood;
    public boolean isPublicFood;
    @BindView(R.id.vpFragmentContainer) ViewPager vpFragmentContainer;
    @BindView(R.id.tvTitle) TextView tvTitle;
    @BindView(R.id.btnForward) Button btnForward;
    private CustomFoodViewPagerAdapter vpAdapter;
    private final int FRAGMENT_RESULT = 2, FRAGMENT_OUTLAY = 1;
    private final int COUNT_GRAMM = 100;
    private final double EMPTY_PARAM = -1.0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_food);
        ButterKnife.bind(this);
        customFood = new CustomFood();
        updateUI();
        vpFragmentContainer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
    }

    private void updateUI() {
        vpAdapter = new CustomFoodViewPagerAdapter(getSupportFragmentManager(), createFragmentList());
        vpFragmentContainer.setAdapter(vpAdapter);
        vpFragmentContainer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        vpFragmentContainer.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                updateUIAfterScrolled(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    private void updateUIAfterScrolled(int i) {
        tvTitle.setText(getResources().getStringArray(R.array.fragment_names)[i]);
        if (i == FRAGMENT_RESULT){
            btnForward.setText(getString(R.string.ok_forward));
        }
        if (i == FRAGMENT_OUTLAY && btnForward.getText().toString().equals(getString(R.string.ok_forward))){
            btnForward.setText(getString(R.string.forward_btn));
        }
    }


    private List<Fragment> createFragmentList() {
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new FragmentMainInfo());
        fragments.add(new FragmentOutlay());
        fragments.add(new FragmentResult());
        return fragments;
    }

    private boolean isCanMoveForward() {
        return ((SayForward) vpAdapter.getItem(vpFragmentContainer.getCurrentItem())).forward();
    }

    @OnClick({R.id.ibClose, R.id.btnBack, R.id.btnForward})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ibClose:
                finish();
                break;
            case R.id.btnBack:
                back();
                break;
            case R.id.btnForward:
                if (isCanMoveForward() && vpFragmentContainer.getCurrentItem() < vpAdapter.getCount() - 1) {
                    vpFragmentContainer.setCurrentItem(vpFragmentContainer.getCurrentItem() + 1);
                    Log.e("LOL", customFood.toString());
                } else if (vpFragmentContainer.getCurrentItem() == vpAdapter.getCount() - 1) {
                    saveFood();
                    Toast.makeText(this, getString(R.string.food_saved), Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        back();
    }

    private void back() {
        if (vpFragmentContainer.getCurrentItem() > 0) {
            vpFragmentContainer.setCurrentItem(vpFragmentContainer.getCurrentItem() - 1);
        } else {
            finish();
        }
    }

    private void saveFood() {
        customFood.setCalories(customFood.getCalories() / COUNT_GRAMM);
        customFood.setFats(customFood.getFats() / COUNT_GRAMM);
        customFood.setCarbohydrates(customFood.getCarbohydrates() / COUNT_GRAMM);
        customFood.setProteins(customFood.getProteins() / COUNT_GRAMM);
        Log.e("LOL", String.valueOf(customFood.getSugar()));

        if (customFood.getCellulose() != EMPTY_PARAM){
            customFood.setCellulose(customFood.getCellulose() / COUNT_GRAMM);
        }

        if (customFood.getSugar() != EMPTY_PARAM){
            customFood.setSugar(customFood.getSugar() / COUNT_GRAMM);
        }

        if (customFood.getCholesterol() != EMPTY_PARAM){
            customFood.setCholesterol(customFood.getCholesterol() / COUNT_GRAMM);
        }

        if (customFood.getSodium() != EMPTY_PARAM){
            customFood.setSodium(customFood.getSodium() / COUNT_GRAMM);
        }

        if (customFood.getPottassium() != EMPTY_PARAM){
            customFood.setPottassium(customFood.getPottassium() / COUNT_GRAMM);
        }

        if (customFood.getSaturatedFats() != EMPTY_PARAM){
            customFood.setSaturatedFats(customFood.getSaturatedFats() / COUNT_GRAMM);
        }

        if (customFood.getMonoUnSaturatedFats() != EMPTY_PARAM){
            customFood.setMonoUnSaturatedFats(customFood.getMonoUnSaturatedFats() / COUNT_GRAMM);
        }

        if (customFood.getPolyUnSaturatedFats() != EMPTY_PARAM){
            customFood.setPolyUnSaturatedFats(customFood.getPolyUnSaturatedFats() / COUNT_GRAMM);
        }

        WorkWithFirebaseDB.addCustomFood(customFood);

        if (isPublicFood){
            WorkWithFirebaseDB.shareCustomFood(customFood);
        }
    }
}

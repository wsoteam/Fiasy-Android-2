package com.wsoteam.diet.Onboarding;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.wsoteam.diet.MainScreen.MainActivity;
import com.wsoteam.diet.OtherActivity.ActivityPrivacyPolicy;
import com.wsoteam.diet.OtherActivity.ActivitySplash;
import com.wsoteam.diet.R;

import java.util.LinkedList;
import java.util.List;

public class OnboardingActivity extends AppCompatActivity {


    private String TAG = "onboarding";

    Button finishButton;
    Button ppButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_onboarding);
        finishButton = findViewById(R.id.onboarding_close);
        ppButton = findViewById(R.id.onboarding_pp_button);
        ppButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(OnboardingActivity.this, ActivityPrivacyPolicy.class));
            }
        });
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(OnboardingActivity.this, ActivitySplash.class));
                finish();
            }
        });




        List<View> viewList = new LinkedList<>();
        viewList.add( new OnboardingItem(this, R.string.onboarding_item_title1, R.string.onboarding_item_description1, R.drawable.onboard_img1).getView());
        viewList.add( new OnboardingItem(this, R.string.onboarding_item_title2, R.string.onboarding_item_description2, R.drawable.onboard_img2).getView());
        viewList.add( new OnboardingItem(this, R.string.onboarding_item_title3, R.string.onboarding_item_description3, R.drawable.onboard_img3).getView());
        viewList.add( new OnboardingItem(this, R.string.onboarding_item_title4, R.string.onboarding_item_description4, R.drawable.onboard_img4).getView());
        viewList.add( new OnboardingItem(this, R.string.onboarding_item_title5, R.string.onboarding_item_description5, R.drawable.onboard_img5).getView());

        ViewPager viewPager = findViewById(R.id.view_pager_onboarding);
        OnboardinAdapter adapter = new OnboardinAdapter(viewList);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabDots);
        tabLayout.setupWithViewPager(viewPager, true);
        viewPager.setAdapter(adapter);
        viewPager.setPageTransformer(true, new ZoomOutPageTransformer());

    }
}

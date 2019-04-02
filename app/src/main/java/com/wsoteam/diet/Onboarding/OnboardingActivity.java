package com.wsoteam.diet.Onboarding;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.adjust.sdk.Adjust;
import com.adjust.sdk.AdjustEvent;
import com.wsoteam.diet.Config;
import com.wsoteam.diet.EventsAdjust;
import com.wsoteam.diet.InApp.ActivitySubscription;
import com.wsoteam.diet.MainScreen.MainActivity;
import com.wsoteam.diet.OtherActivity.ActivityPrivacyPolicy;
import com.wsoteam.diet.OtherActivity.ActivitySplash;
import com.wsoteam.diet.R;

import java.util.LinkedList;
import java.util.List;

public class OnboardingActivity extends AppCompatActivity {

    private String TAG = "onboarding";
    private PageListener pageListener;
    private Button backButton;
    List<View> viewList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_onboarding);
        backButton = findViewById(R.id.onboarding_btn_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        viewList = new LinkedList<>();
        viewList.add( new OnboardingItem(this, R.string.onboarding_item_title1, R.string.onboarding_item_description1, R.drawable.onboard_img1).getView());
        viewList.add( new OnboardingItem(this, R.string.onboarding_item_title2, R.string.onboarding_item_description2, R.drawable.onboard_img2).getView());
        viewList.add( new OnboardingItem(this, R.string.onboarding_item_title3, R.string.onboarding_item_description3, R.drawable.onboard_img3).getView());
        viewList.add( new OnboardingItem(this, R.string.onboarding_item_title4, R.string.onboarding_item_description4, R.drawable.onboard_img4).getView());
        viewList.add( new OnboardingItem(this, R.string.onboarding_item_title5, R.string.onboarding_item_description5, R.drawable.onboard_img5).getView());
        viewList.add(LayoutInflater.from(this).inflate(R.layout.empty_layout, null));

        ViewPager viewPager = findViewById(R.id.view_pager_onboarding);
        OnboardinAdapter adapter = new OnboardinAdapter(viewList);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabDots);
        tabLayout.setupWithViewPager(viewPager, true);
        viewPager.setAdapter(adapter);
        viewPager.setPageTransformer(true, new ZoomOutPageTransformer());

        pageListener = new PageListener();
        viewPager.setOnPageChangeListener(pageListener);

    }


    private class PageListener extends ViewPager.SimpleOnPageChangeListener {

        public void onPageSelected(int position) {
            Log.d(TAG, "page selected " + position);
            if (position == viewList.size() - 1){
                Adjust.trackEvent(new AdjustEvent(EventsAdjust.view_onboarding));
                startActivity(new Intent(OnboardingActivity.this, ActivitySubscription.class).putExtra(Config.FROM_ONBOARDING, Config.FROM_ONBOARDING));
                finish();
            }

        }
    }
}

package com.wsoteam.diet.MainScreen;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.wsoteam.diet.AmplitudaEvents;
import com.wsoteam.diet.BranchProfile.Fragments.FragmentProfile;
import com.wsoteam.diet.Config;
import com.wsoteam.diet.EventsAdjust;
import com.wsoteam.diet.InApp.Fragments.FragmentSubscription;
import com.wsoteam.diet.MainScreen.Fragments.FragmentDiary;
import com.wsoteam.diet.MainScreen.Fragments.FragmentEmpty;
import com.wsoteam.diet.R;
import com.wsoteam.diet.Recipes.GroupsFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.flFragmentContainer) FrameLayout flFragmentContainer;
    @BindView(R.id.bnv_main) BottomNavigationView bnvMain;
    private FragmentTransaction transaction;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            transaction = getSupportFragmentManager().beginTransaction();
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            switch (item.getItemId()) {
                case R.id.bnv_main_diary:
                    transaction.replace(R.id.flFragmentContainer, new FragmentDiary()).commit();
                    window.setStatusBarColor(Color.parseColor("#AE6A23"));
                    return true;
                case R.id.bnv_main_articles:
                    if (checkSubscribe()) {
                        transaction.replace(R.id.flFragmentContainer, new FragmentEmpty()).commit();
                    } else {
                        transaction.replace(R.id.flFragmentContainer, FragmentSubscription.newInstance(true,
                                AmplitudaEvents.view_prem_content, EventsAdjust.view_prem_content,
                                AmplitudaEvents.buy_prem_content, EventsAdjust.buy_prem_content)).commit();
                        window.setStatusBarColor(Color.parseColor("#374557"));
                    }
                    return true;
                case R.id.bnv_main_trainer:
                    if (checkSubscribe()) {
                        transaction.replace(R.id.flFragmentContainer, new FragmentEmpty()).commit();
                    } else {
                        transaction.replace(R.id.flFragmentContainer, FragmentSubscription.newInstance(true,
                                AmplitudaEvents.view_prem_training, EventsAdjust.view_prem_training,
                                AmplitudaEvents.buy_prem_training, EventsAdjust.buy_prem_training)).commit();
                        window.setStatusBarColor(Color.parseColor("#374557"));
                    }
                    return true;
                case R.id.bnv_main_recipes:
                    if (true) {
                        transaction.replace(R.id.flFragmentContainer, new GroupsFragment()).commit();

                    } else {
                        transaction.replace(R.id.flFragmentContainer, FragmentSubscription.newInstance(true,
                                AmplitudaEvents.view_prem_recipe, EventsAdjust.view_prem_recipe,
                                AmplitudaEvents.buy_prem_recipe, EventsAdjust.buy_prem_recipe)).commit();
                        window.setStatusBarColor(Color.parseColor("#374557"));
                    }
                    return true;
                case R.id.bnv_main_profile:
                    transaction.replace(R.id.flFragmentContainer, new FragmentProfile()).commit();
                    window.setStatusBarColor(Color.parseColor("#2E4E4E"));
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        ButterKnife.bind(this);
        bnvMain.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        getSupportFragmentManager().beginTransaction().add(R.id.flFragmentContainer, new FragmentDiary()).commit();
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

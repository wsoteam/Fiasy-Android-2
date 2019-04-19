package com.wsoteam.diet.MainScreen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.amplitude.api.Amplitude;
import com.wsoteam.diet.AmplitudaEvents;
import com.wsoteam.diet.BranchProfile.Fragments.FragmentProfile;
import com.wsoteam.diet.Config;
import com.wsoteam.diet.EventsAdjust;
import com.wsoteam.diet.InApp.ActivitySubscription;
import com.wsoteam.diet.InApp.Fragments.FragmentSubscription;
import com.wsoteam.diet.MainScreen.Fragments.FragmentDiary;
import com.wsoteam.diet.MainScreen.Fragments.FragmentEmpty;
import com.wsoteam.diet.EntryPoint.ActivitySplash;
import com.wsoteam.diet.R;
import com.wsoteam.diet.Recipes.GroupsFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.flFragmentContainer) FrameLayout flFragmentContainer;
    @BindView(R.id.bnv_main) BottomNavigationView bnvMain;
    @BindView(R.id.bottom_sheet) LinearLayout bottomSheet;
    @BindView(R.id.ivTop) ImageView ivTop;
    private FragmentTransaction transaction;
    private SharedPreferences freeUser, firstRun;
    private BottomSheetBehavior bottomSheetBehavior;


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
                    if (!isFreeUser()) {
                        transaction.replace(R.id.flFragmentContainer, new FragmentProfile()).commit();
                        window.setStatusBarColor(Color.parseColor("#2E4E4E"));
                        return true;
                    } else {
                        Amplitude.getInstance().logEvent(AmplitudaEvents.reg_offer);
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                        return false;
                    }

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
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        if (isFreeUser() && !checkSubscribe() && getPreferences(MODE_PRIVATE).getBoolean(Config.FIRST_SPAM, true)) {
            setInterceptor();
            deleteSpamPremium();
        }

        Amplitude.getInstance().logEvent(AmplitudaEvents.view_diary);
    }

    private void deleteSpamPremium() {
        firstRun = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = firstRun.edit();
        editor.putBoolean(Config.FIRST_SPAM, false);
        editor.commit();
    }


    private void setInterceptor() {
        ivTop.setVisibility(View.VISIBLE);
        ivTop.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                ivTop.setVisibility(View.GONE);
                startActivity(new Intent(MainActivity.this, ActivitySubscription.class)
                        .putExtra(Config.AMPLITUDE_COME_FROM, AmplitudaEvents.view_prem_free_onboard)
                        .putExtra(Config.AMPLITUDE_BUY_FROM, AmplitudaEvents.buy_prem_free_onboard));
                return false;
            }
        });
    }

    private boolean checkSubscribe() {
        SharedPreferences sharedPreferences = getSharedPreferences(Config.STATE_BILLING, MODE_PRIVATE);
        if (sharedPreferences.getBoolean(Config.STATE_BILLING, false)) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isFreeUser() {
        freeUser = getSharedPreferences(Config.FREE_USER, MODE_PRIVATE);
        return freeUser.getBoolean(Config.FREE_USER, true);
    }

    @OnClick({R.id.ibSheetClose, R.id.btnReg})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ibSheetClose:
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                break;
            case R.id.btnReg:
                AmplitudaEvents.logEventRegistration(AmplitudaEvents.reg_from_diary);
                startActivity(new Intent(MainActivity.this, ActivitySplash.class)
                        .putExtra(Config.IS_NEED_REG, true)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                break;
        }
    }


}

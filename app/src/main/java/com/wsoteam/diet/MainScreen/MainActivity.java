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
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.wsoteam.diet.AmplitudaEvents;
import com.wsoteam.diet.BranchProfile.Fragments.FragmentProfile;
import com.wsoteam.diet.Config;
import com.wsoteam.diet.EventsAdjust;
import com.wsoteam.diet.InApp.Fragments.FragmentSubscriptionGreen;
import com.wsoteam.diet.MainScreen.Dialogs.RateDialogs;
import com.wsoteam.diet.MainScreen.Fragments.FragmentDiary;
import com.wsoteam.diet.MainScreen.Fragments.FragmentEmpty;
import com.wsoteam.diet.EntryPoint.ActivitySplash;
import com.wsoteam.diet.R;
import com.wsoteam.diet.Recipes.GroupsFragment;
import com.wsoteam.diet.Sync.UserDataHolder;

import java.util.Calendar;

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
    private final int NONE_RUN = -1;


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
                        transaction.replace(R.id.flFragmentContainer, FragmentSubscriptionGreen.newInstance(true,
                                AmplitudaEvents.view_prem_content, EventsAdjust.view_prem_content,
                                AmplitudaEvents.buy_prem_content, EventsAdjust.buy_prem_content, false)).commit();
                        window.setStatusBarColor(Color.parseColor("#747d3b"));
                    }
                    return true;
                case R.id.bnv_main_trainer:
                    if (checkSubscribe()) {
                        transaction.replace(R.id.flFragmentContainer, new FragmentEmpty()).commit();
                    } else {
                        transaction.replace(R.id.flFragmentContainer, FragmentSubscriptionGreen.newInstance(true,
                                AmplitudaEvents.view_prem_content, EventsAdjust.view_prem_content,
                                AmplitudaEvents.buy_prem_content, EventsAdjust.buy_prem_content, false)).commit();
                        window.setStatusBarColor(Color.parseColor("#747d3b"));
                    }
                    return true;
                case R.id.bnv_main_recipes:
                    transaction.replace(R.id.flFragmentContainer, new GroupsFragment()).commit();
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
    protected void onResume() {
        super.onResume();
        handlGrade(Calendar.getInstance().getTimeInMillis());
    }

    private void handlGrade(long currentTime) {
        long timeStartingPoint = getSharedPreferences(Config.STARTING_POINT, MODE_PRIVATE).getLong(Config.STARTING_POINT, 0);
        boolean isAddedFoodEarly = getSharedPreferences(Config.IS_ADDED_FOOD, MODE_PRIVATE).getBoolean(Config.IS_ADDED_FOOD, false);
        int gradeStatus = getSharedPreferences(Config.IS_ADDED_FOOD, MODE_PRIVATE).
                getInt(Config.IS_ADDED_FOOD, Config.NOT_VIEW_GRADE_DIALOG);
        if ((currentTime - timeStartingPoint) >= Config.ONE_DAY && gradeStatus != Config.GRADED) {
            if (isAddedFoodEarly) {
                if (gradeStatus == Config.NOT_VIEW_GRADE_DIALOG) {
                    RateDialogs.showGradeDialog(this, false);
                }
            } else if ((currentTime - timeStartingPoint) >= Config.ONE_DAY * 2) {
                RateDialogs.showGradeDialog(this, false);
            }
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        ButterKnife.bind(this);
        bnvMain.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        getSupportFragmentManager().beginTransaction().add(R.id.flFragmentContainer, new FragmentDiary()).commit();
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        checkForcedGrade();
    }

    private void checkForcedGrade() {
        if (getSharedPreferences(Config.IS_NEED_SHOW_GRADE_DIALOG, MODE_PRIVATE).getBoolean(Config.IS_NEED_SHOW_GRADE_DIALOG, false)){
            RateDialogs.showGradeDialog(this, true);
            getSharedPreferences(Config.IS_NEED_SHOW_GRADE_DIALOG, MODE_PRIVATE).
                    edit().putBoolean(Config.IS_NEED_SHOW_GRADE_DIALOG, false).commit();
        }
    }

    private boolean checkSubscribe() {
        SharedPreferences sharedPreferences = getSharedPreferences(Config.STATE_BILLING, MODE_PRIVATE);
        if (sharedPreferences.getBoolean(Config.STATE_BILLING, false)) {
            return true;
        } else {
            return false;
        }
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

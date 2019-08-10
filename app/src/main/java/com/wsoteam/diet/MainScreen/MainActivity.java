package com.wsoteam.diet.MainScreen;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.amplitude.api.Amplitude;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wsoteam.diet.ABConfig;
import com.wsoteam.diet.AmplitudaEvents;
import com.wsoteam.diet.Articles.ListArticlesAdapter;
import com.wsoteam.diet.Articles.ListArticlesFragment;
import com.wsoteam.diet.Articles.POJO.ArticlesHolder;
import com.wsoteam.diet.Articles.POJO.ListArticles;
import com.wsoteam.diet.Authenticate.POJO.Box;
import com.wsoteam.diet.BarcodeScanner.BaseScanner;
import com.wsoteam.diet.BranchOfAnalyzer.CustomFood.ActivityCreateFood;
import com.wsoteam.diet.BranchProfile.Fragments.FragmentProfile;
import com.wsoteam.diet.Config;
import com.wsoteam.diet.EntryPoint.ActivitySplash;
import com.wsoteam.diet.DietPlans.POJO.ForUploadModel;
import com.wsoteam.diet.InApp.Fragments.FragmentSubscriptionGreen;
import com.wsoteam.diet.InApp.Fragments.FragmentSubscriptionGreenOneButton;
import com.wsoteam.diet.InApp.Fragments.FragmentSubscriptionGreenUA;
import com.wsoteam.diet.MainScreen.Dialogs.RateDialogs;
import com.wsoteam.diet.MainScreen.Fragments.FragmentDiary;
import com.wsoteam.diet.MainScreen.Fragments.FragmentEmpty;
import com.wsoteam.diet.MainScreen.Support.AsyncWriteFoodDB;
import com.wsoteam.diet.MainScreen.intercom.IntercomFactory;
import com.wsoteam.diet.R;
import com.wsoteam.diet.Recipes.POJO.EatingGroupsRecipes;
import com.wsoteam.diet.Recipes.POJO.GroupsHolder;
import com.wsoteam.diet.Recipes.POJO.ListRecipes;
import com.wsoteam.diet.Recipes.v2.GroupsFragment;
import com.wsoteam.diet.common.Analytics.Events;
import com.wsoteam.diet.presentation.food.template.browse.BrowseFoodTemplateFragment;
import com.wsoteam.diet.presentation.food.template.create.CreateFoodTemplateActivity;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.intercom.android.sdk.Intercom;


public class MainActivity extends AppCompatActivity {
    @BindView(R.id.flFragmentContainer) FrameLayout flFragmentContainer;
    @BindView(R.id.bnv_main) BottomNavigationView bnvMain;
    @BindView(R.id.bottom_sheet) LinearLayout bottomSheet;
    private FragmentTransaction transaction;
    private BottomSheetBehavior bottomSheetBehavior;
    private boolean isMainFragment = true;
    private Window window;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            transaction = getSupportFragmentManager().beginTransaction();
            window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            Box box = new Box();
            box.setSubscribe(false);
            box.setOpenFromPremPart(true);
            box.setOpenFromIntrodaction(false);

            getSupportFragmentManager().popBackStack(Config.RECIPE_BACK_STACK, FragmentManager.POP_BACK_STACK_INCLUSIVE);

            switch (item.getItemId()) {
                case R.id.bnv_main_diary:
                    isMainFragment = true;
                    transaction.replace(R.id.flFragmentContainer, new FragmentDiary()).commit();
                    window.setStatusBarColor(Color.parseColor("#AE6A23"));
                    return true;
                case R.id.bnv_main_articles:
                    Amplitude.getInstance().logEvent(Events.CHOOSE_ARTICLES);
                    Intercom.client().logEvent(Events.CHOOSE_ARTICLES);
                    box.setComeFrom(AmplitudaEvents.view_prem_content);
                    box.setBuyFrom(AmplitudaEvents.buy_prem_content);
                    isMainFragment = false;

                    if (Config.RELEASE) {
                        if (checkSubscribe()) {
                            transaction.replace(R.id.flFragmentContainer, new FragmentEmpty()).commit();
                        } else {
                            if (getABVersion().equals(ABConfig.C_VERSION)) {
                                transaction.replace(R.id.flFragmentContainer, FragmentSubscriptionGreenUA.
                                        newInstance(box)).commit();
                            } else {
                                if (getABVersion().equals(ABConfig.A_VERSION)) {
                                    transaction.replace(R.id.flFragmentContainer, FragmentSubscriptionGreen.
                                            newInstance(box)).commit();
                                } else {
                                    transaction.replace(R.id.flFragmentContainer, FragmentSubscriptionGreenOneButton.
                                            newInstance(box)).commit();
                                }
                            }
                            window.setStatusBarColor(Color.parseColor("#747d3b"));
                        }
                    } else {
                        transaction.replace(R.id.flFragmentContainer, new ListArticlesFragment()).commit();
                    }
                    return true;
                case R.id.bnv_main_recipes:
                    isMainFragment = false;
                    transaction.replace(R.id.flFragmentContainer, new GroupsFragment()).commit();
                    return true;
                case R.id.bnv_main_profile:
                    Amplitude.getInstance().logEvent(Events.VIEW_PROFILE);
                    Intercom.client().logEvent(Events.VIEW_PROFILE);
                    isMainFragment = false;
                    transaction.replace(R.id.flFragmentContainer, new FragmentProfile()).commit();
                    window.setStatusBarColor(Color.parseColor("#2E4E4E"));
                    return true;
            }
            return false;
        }
    };

    private String getABVersion() {
        return getSharedPreferences(ABConfig.KEY_FOR_SAVE_STATE, MODE_PRIVATE).getString(ABConfig.KEY_FOR_SAVE_STATE, "default");
    }

    @Override
    protected void onResume() {
        super.onResume();
        handlGrade(Calendar.getInstance().getTimeInMillis());
        Log.e("LOL", FirebaseAuth.getInstance().getCurrentUser().getUid());
    }

    private void handlGrade(long currentTime) {
        long timeStartingPoint = getSharedPreferences(Config.STARTING_POINT, MODE_PRIVATE).getLong(Config.STARTING_POINT, 0);
        boolean isAddedFoodEarly = getSharedPreferences(Config.IS_ADDED_FOOD, MODE_PRIVATE).getBoolean(Config.IS_ADDED_FOOD, false);
        int gradeStatus = getSharedPreferences(Config.IS_GRADE_APP, MODE_PRIVATE).
                getInt(Config.IS_GRADE_APP, Config.NOT_VIEW_GRADE_DIALOG);
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
        //checkForcedGrade();
        IntercomFactory.login(FirebaseAuth.getInstance().getCurrentUser().getUid());
        new AsyncWriteFoodDB().execute(MainActivity.this);
        if (GroupsHolder.getGroupsRecipes() == null) {
            loadRecipes();
        }
        if (ArticlesHolder.getListArticles() == null) {
            loadArticles();
        }
    }

    private void checkForcedGrade() {
        if (getSharedPreferences(Config.IS_NEED_SHOW_GRADE_DIALOG, MODE_PRIVATE).getBoolean(Config.IS_NEED_SHOW_GRADE_DIALOG, false)) {
            RateDialogs.showGradeDialog(this, true);
            getSharedPreferences(Config.IS_NEED_SHOW_GRADE_DIALOG, MODE_PRIVATE).
                    edit().putBoolean(Config.IS_NEED_SHOW_GRADE_DIALOG, false).apply();
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

    private void loadRecipes() {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("RECIPES_PLANS_NEW");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                ListRecipes groupsRecipes = dataSnapshot.getValue(ListRecipes.class);

                EatingGroupsRecipes eatingGroupsRecipes = new EatingGroupsRecipes(groupsRecipes);
                GroupsHolder groupsHolder = new GroupsHolder();
                groupsHolder.bind(eatingGroupsRecipes);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void loadArticles() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("ARTICLES");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                ListArticles listArticles = dataSnapshot.getValue(ListArticles.class);
                ArticlesHolder articlesHolder = new ArticlesHolder();
                articlesHolder.bind(listArticles);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        if (isMainFragment) {
            super.onBackPressed();
        }else {
            isMainFragment = true;
            getSupportFragmentManager().beginTransaction().replace(R.id.flFragmentContainer, new FragmentDiary()).commit();
            window.setStatusBarColor(Color.parseColor("#AE6A23"));
            bnvMain.setSelectedItemId(R.id.bnv_main_diary);
        }

    }
}

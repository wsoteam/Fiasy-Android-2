package com.wsoteam.diet.BranchEatingDiary;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.wsoteam.diet.BranchEatingDiary.Fragments.FragmentBreakfast;
import com.wsoteam.diet.BranchEatingDiary.Fragments.FragmentDinner;
import com.wsoteam.diet.BranchEatingDiary.Fragments.FragmentLunch;
import com.wsoteam.diet.BranchEatingDiary.Fragments.FragmentSnack;
import com.wsoteam.diet.BranchOfAnalyzer.ActivityListAndSearch;
import com.wsoteam.diet.BranchOfAnalyzer.POJOEating.Breakfast;
import com.wsoteam.diet.BranchOfAnalyzer.POJOEating.Dinner;
import com.wsoteam.diet.BranchOfAnalyzer.POJOEating.Lunch;
import com.wsoteam.diet.BranchOfAnalyzer.POJOEating.Snack;
import com.wsoteam.diet.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ActivityEatingDiary extends AppCompatActivity {
    private ArrayList<Fragment> listOfFragments;
    private FloatingActionButton fabEatingDiaryAddNewEating;
    private BottomNavigationView bottomNavigationView;
    private FrameLayout flEatingDiary;
    private FragmentTransaction transaction;
    private TextView tvEatingDiaryCollapsingFat, tvEatingDiaryCollapsingCarbo, tvEatingDiaryCollapsingProt, tvEatingDiaryCollapsingKcal;

    int breakfastKcal = 0, lunchKcal = 0, dinnerKcal = 0, snackKcal = 0,
            breakfastProt = 0, lunchProt = 0, dinnerProt = 0, snackProt = 0,
            breakfastCarbo = 0, lunchCarbo = 0, dinnerCarbo = 0, snackCarbo = 0,
            breakfastFat = 0, lunchFat = 0, dinnerFat = 0, snackFat = 0;

    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            transaction = getSupportFragmentManager().beginTransaction();
            switch (item.getItemId()) {
                case R.id.btn_nav_eating_breakfast:
                    //fillMainIndicators(0);
                    transaction.replace(R.id.flEatingDiary, listOfFragments.get(0)).commit();
                    return true;
                case R.id.btn_nav_eating_lunch:
                    //fillMainIndicators(1);
                    transaction.replace(R.id.flEatingDiary, listOfFragments.get(1)).commit();
                    return true;
                case R.id.btn_nav_eating_dinner:
                    //fillMainIndicators(2);
                    transaction.replace(R.id.flEatingDiary, listOfFragments.get(2)).commit();
                    return true;
                case R.id.btn_nav_eating_snack:
                    //fillMainIndicators(3);
                    transaction.replace(R.id.flEatingDiary, listOfFragments.get(3)).commit();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eating_diary);

        bottomNavigationView = findViewById(R.id.bottNavEatingDinner);

        tvEatingDiaryCollapsingFat = findViewById(R.id.tvEatingDiaryCollapsingFat);
        tvEatingDiaryCollapsingCarbo = findViewById(R.id.tvEatingDiaryCollapsingCarbo);
        tvEatingDiaryCollapsingProt = findViewById(R.id.tvEatingDiaryCollapsingProt);
        tvEatingDiaryCollapsingKcal = findViewById(R.id.tvEatingDiaryCollapsingKcal);
        flEatingDiary = findViewById(R.id.flEatingDiary);
        fabEatingDiaryAddNewEating = findViewById(R.id.fabEatingDiaryAddNewEating);

        compareDate();
        fillMainIndicators(0);

        listOfFragments = new ArrayList<>();
        listOfFragments.add(new FragmentBreakfast());
        listOfFragments.add(new FragmentLunch());
        listOfFragments.add(new FragmentDinner());
        listOfFragments.add(new FragmentSnack());

        getSupportFragmentManager().beginTransaction().add(R.id.flEatingDiary, listOfFragments.get(0)).commit();

        fabEatingDiaryAddNewEating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityEatingDiary.this, ActivityListAndSearch.class);
                startActivity(intent);
            }
        });



        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);


    }

    private void fillMainIndicators(int position) {
        switch (position) {
            case 0:
                setUpNumbers(breakfastFat, breakfastCarbo, breakfastProt, breakfastKcal);
                break;
            case 1:
                setUpNumbers(lunchFat, lunchCarbo, lunchProt, lunchKcal);
                break;
            case 2:
                setUpNumbers(dinnerFat, dinnerCarbo, dinnerProt, dinnerKcal);
                break;
            case 3:
                setUpNumbers(snackFat, snackCarbo, snackProt, snackKcal);
                break;
        }
    }

    private void setUpNumbers(int fat, int carbo, int prot, int kcal) {
        tvEatingDiaryCollapsingFat.setText(String.valueOf(fat) + " г");
        tvEatingDiaryCollapsingCarbo.setText(String.valueOf(carbo) + " г");
        tvEatingDiaryCollapsingProt.setText(String.valueOf(prot) + " г");
        tvEatingDiaryCollapsingKcal.setText(String.valueOf(kcal) + " ккал");
    }

    private void compareDate() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);


        List<Breakfast> breakfasts = Breakfast.listAll(Breakfast.class);
        List<Lunch> lunches = Lunch.listAll(Lunch.class);
        List<Dinner> dinners = Dinner.listAll(Dinner.class);
        List<Snack> snacks = Snack.listAll(Snack.class);

        Breakfast.deleteAll(Breakfast.class);
        Lunch.deleteAll(Lunch.class);
        Dinner.deleteAll(Dinner.class);
        Snack.deleteAll(Snack.class);

        for (int i = 0; i < breakfasts.size(); i++) {
            if (breakfasts.get(i).getDay() < day || breakfasts.get(i).getMonth() < month || breakfasts.get(i).getYear() < year) {

            } else {
                breakfasts.get(i).save();
                breakfastKcal += breakfasts.get(i).getCalories();
                breakfastProt += breakfasts.get(i).getProtein();
                breakfastCarbo += breakfasts.get(i).getCarbohydrates();
                breakfastFat += breakfasts.get(i).getFat();
            }
        }
        for (int i = 0; i < lunches.size(); i++) {
            if (lunches.get(i).getDay() < day || lunches.get(i).getMonth() < month || lunches.get(i).getYear() < year) {

            } else {
                lunches.get(i).save();
                lunchKcal += lunches.get(i).getCalories();
                lunchProt += lunches.get(i).getProtein();
                lunchCarbo += lunches.get(i).getCarbohydrates();
                lunchFat += lunches.get(i).getFat();
            }
        }
        for (int i = 0; i < dinners.size(); i++) {
            if (dinners.get(i).getDay() < day || dinners.get(i).getMonth() < month || dinners.get(i).getYear() < year) {

            } else {
                dinners.get(i).save();
                dinnerKcal += dinners.get(i).getCalories();
                dinnerProt += dinners.get(i).getProtein();
                dinnerCarbo += dinners.get(i).getCarbohydrates();
                dinnerFat += dinners.get(i).getFat();
            }
        }
        for (int i = 0; i < snacks.size(); i++) {
            if (snacks.get(i).getDay() < day || snacks.get(i).getMonth() < month || snacks.get(i).getYear() < year) {

            } else {
                snacks.get(i).save();
                snackKcal += snacks.get(i).getCalories();
                snackProt += snacks.get(i).getProtein();
                snackCarbo += snacks.get(i).getCarbohydrates();
                snackFat += snacks.get(i).getFat();
            }
        }
    }
}

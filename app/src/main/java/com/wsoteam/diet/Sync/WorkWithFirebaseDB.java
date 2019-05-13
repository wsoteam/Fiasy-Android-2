package com.wsoteam.diet.Sync;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wsoteam.diet.BranchOfAnalyzer.POJOEating.Breakfast;
import com.wsoteam.diet.BranchOfAnalyzer.POJOEating.Dinner;
import com.wsoteam.diet.BranchOfAnalyzer.POJOEating.Eating;
import com.wsoteam.diet.BranchOfAnalyzer.POJOEating.Lunch;
import com.wsoteam.diet.BranchOfAnalyzer.POJOEating.Snack;
import com.wsoteam.diet.Config;
import com.wsoteam.diet.POJOForDB.DiaryData;
import com.wsoteam.diet.POJOProfile.Profile;
import com.wsoteam.diet.R;
import com.wsoteam.diet.Recipes.POJO.ListRecipes;
import com.wsoteam.diet.Sync.POJO.UserData;
import com.wsoteam.diet.Sync.POJO.WeightDiaryObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class WorkWithFirebaseDB {

    public static void setStartEmptyObject(Context context) {
        int currentDay, currentMonth, currentYear;
        Calendar calendar = Calendar.getInstance();
        currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        currentMonth = calendar.get(Calendar.MONTH);
        currentYear = calendar.get(Calendar.YEAR);

        Profile profile = new Profile("Ivan", "Ivanov", true, 24, 180, 55, 0, context.getResources().getString(R.string.level_easy),
                "default", 3000, 2500, 165, 50, 290, context.getResources().getString(R.string.dif_level_normal),
                currentDay, currentMonth, currentYear);

        Breakfast currentBrekfastDrink = new Breakfast(context.getResources().getString(R.string.currentBreakfastDrink), "none", 140,
                4, 20, 5, 100, currentDay, currentMonth, currentYear);
        Breakfast yesterdayBreakfastDrink = new Breakfast(context.getResources().getString(R.string.yesterdayBreakfastDrink), "none",
                100, 3, 22, 3, 100, currentDay - 1, currentMonth, currentYear);
        Breakfast beforeYesterdayBreakfastDrink = new Breakfast(context.getResources().getString(R.string.beforeYesterdayBreakfastDrink), "none",
                130, 5, 18, 3, 100, currentDay - 2, currentMonth, currentYear);
        Breakfast currentBreakfastEat = new Breakfast(context.getResources().getString(R.string.currentBreakfastEat), "none", 215,
                0, 12, 17, 100, currentDay, currentMonth, currentYear);
        Breakfast yesterdayBreakfastEat = new Breakfast(context.getResources().getString(R.string.yesterdayBreakfastEat), "none",
                228, 7, 19, 14, 100, currentDay - 1, currentMonth, currentYear);
        Breakfast beforeYesterdayBreakfastEat = new Breakfast(context.getResources().getString(R.string.beforeYesterdayBreakfastEat), "none",
                215, 0, 12, 17, 100, currentDay - 2, currentMonth, currentYear);



        Lunch currentLunchEat = new Lunch(context.getResources().getString(R.string.currentLunchEat), "none",
                266, 44, 21, 2, 100, currentDay, currentMonth, currentYear);
        Lunch yesterdayLunchEat = new Lunch(context.getResources().getString(R.string.yesterdayLunchEat), "none",
                56, 11, 3, 1, 100, currentDay - 1, currentMonth, currentYear);
        Lunch beforeYesterdayLunchEat = new Lunch(context.getResources().getString(R.string.beforeYesterdayLunchEat), "none",
                58, 9, 2, 1, 100, currentDay - 2, currentMonth, currentYear);



        Dinner currentDinnerEat = new Dinner(context.getResources().getString(R.string.currentDinnerEat), "none",
                259, 4, 11, 22, 100, currentDay, currentMonth, currentYear);
        Dinner yesterdayDinnerEat = new Dinner(context.getResources().getString(R.string.yesterdayDinnerEat), "none",
                204, 0, 15, 16, 100, currentDay - 1, currentMonth, currentYear);
        Dinner beforeYesterdayDinnerEat = new Dinner(context.getResources().getString(R.string.beforeYesterdayDinnerEat), "none",
                198, 7, 5, 17, 100, currentDay - 2, currentMonth, currentYear);



        Snack currentSnackEat = new Snack(context.getResources().getString(R.string.currentSnackEat), "none",
                289, 46, 3, 11, 100, currentDay, currentMonth, currentYear);
        Snack yesterdaySnackEat = new Snack(context.getResources().getString(R.string.yesterdaySnackEat), "none",
                379, 73, 3, 9, 100, currentDay - 1, currentMonth, currentYear);
        Snack beforeYesterdaySnackEat = new Snack(context.getResources().getString(R.string.beforeYesterdaySnackEat), "none",
                115, 19, 3, 3, 100, currentDay - 2, currentMonth, currentYear);

        HashMap<String, Breakfast> breakfastHashMap = new HashMap<>();
        breakfastHashMap.put("currentBrekfastDrink", currentBrekfastDrink);
        breakfastHashMap.put("yesterdayBreakfastDrink", yesterdayBreakfastDrink);
        breakfastHashMap.put("beforeYesterdayBreakfastDrink", beforeYesterdayBreakfastDrink);
        breakfastHashMap.put("currentBreakfastEat", currentBreakfastEat);
        breakfastHashMap.put("yesterdayBreakfastEat", yesterdayBreakfastEat);
        breakfastHashMap.put("beforeYesterdayBreakfastEat", beforeYesterdayBreakfastEat);

        HashMap<String, Lunch> lunchHashMap = new HashMap<>();
        lunchHashMap.put("currentLunchEat", currentLunchEat);
        lunchHashMap.put("yesterdayLunchEat", yesterdayLunchEat);
        lunchHashMap.put("beforeYesterdayLunchEat", beforeYesterdayLunchEat);

        HashMap<String, Dinner> dinnerHashMap = new HashMap<>();
        dinnerHashMap.put("currentDinnerEat", currentDinnerEat);
        dinnerHashMap.put("yesterdayDinnerEat", yesterdayDinnerEat);
        dinnerHashMap.put("beforeYesterdayDinnerEat", beforeYesterdayDinnerEat);

        HashMap<String, Snack> snackHashMap = new HashMap<>();
        snackHashMap.put("currentSnackEat", currentSnackEat);
        snackHashMap.put("yesterdaySnackEat", yesterdaySnackEat);
        snackHashMap.put("beforeYesterdaySnackEat", beforeYesterdaySnackEat);

        UserData userData = new UserData("free", profile, breakfastHashMap, lunchHashMap, dinnerHashMap, snackHashMap, new HashMap<>());

        new UserDataHolder().bindObjectWithHolder(userData);



    }


    public static void setWholeTestObject() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(Config.NAME_OF_USER_DATA_LIST_ENTITY).
                child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserData userData = dataSnapshot.getValue(UserData.class);
                Log.e("LOL", "get user data");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static void setFirebaseStateListener() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(Config.NAME_OF_USER_DATA_LIST_ENTITY).
                child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                new UserDataHolder().bindObjectWithHolder(dataSnapshot.getValue(UserData.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public static void addBreakfast(Breakfast breakfast) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(Config.NAME_OF_USER_DATA_LIST_ENTITY).
                child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("breakfasts");
        myRef.push().setValue(breakfast);
    }

    public static void addLunch(Lunch lunch) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(Config.NAME_OF_USER_DATA_LIST_ENTITY).
                child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("lunches");
        myRef.push().setValue(lunch);
    }

    public static void addDinner(Dinner dinner) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(Config.NAME_OF_USER_DATA_LIST_ENTITY).
                child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("dinners");
        myRef.push().setValue(dinner);
    }

    public static void addSnack(Snack snack) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(Config.NAME_OF_USER_DATA_LIST_ENTITY).
                child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("snacks");
        myRef.push().setValue(snack);
    }

    public static void putProfileValue(Profile profile) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(Config.NAME_OF_USER_DATA_LIST_ENTITY).
                child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("profile");
        myRef.setValue(profile);
    }

    public static void addWeightDiaryItem(WeightDiaryObject data) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(Config.NAME_OF_USER_DATA_LIST_ENTITY).
                child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("diaryDataList");
        myRef.push().setValue(data);
    }

    public static void replaceWeightDiaryItem(WeightDiaryObject data, String key) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(Config.NAME_OF_USER_DATA_LIST_ENTITY).
                child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("diaryDataList").child(key);
        myRef.setValue(data);
    }

    public static void saveListRecipes(ListRecipes data){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("RECIPES_PLANS");
        myRef.setValue(data);
    }
}

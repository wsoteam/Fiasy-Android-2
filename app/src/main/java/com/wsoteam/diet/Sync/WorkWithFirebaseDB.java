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
import com.wsoteam.diet.Sync.POJO.UserData;

import java.util.ArrayList;
import java.util.List;

public class WorkWithFirebaseDB {

    public static void setStartEmptyObject(){
        UserData userData = new UserData();
        userData.setName("name");
        userData.setProfile(new Profile());

        List<Breakfast> breakfasts = new ArrayList<>();
        breakfasts.add(new Breakfast());
        //userData.setBreakfasts(breakfasts);

        List<Lunch> lunches = new ArrayList<>();
        lunches.add(new Lunch());
        userData.setLunches(lunches);

        List<Dinner> dinners = new ArrayList<>();
        dinners.add(new Dinner());
        userData.setDinners(dinners);

        List<Snack> snacks = new ArrayList<>();
        snacks.add(new Snack());
        userData.setSnacks(snacks);

        List<DiaryData> diaryData = new ArrayList<>();
        diaryData.add(new DiaryData());
        userData.setDiaryDataList(diaryData);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(Config.NAME_OF_USER_DATA_LIST_ENTITY).
                child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        myRef.setValue(userData);

    }


    public static void setWholeTestObject(){
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
    public static void setFirebaseStateListener(){
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

    public static void addBreakfast(Breakfast breakfast){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(Config.NAME_OF_USER_DATA_LIST_ENTITY).
                child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("breakfasts");
        myRef.push().setValue(breakfast);
    }

    public static void addLunch(Lunch lunch){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(Config.NAME_OF_USER_DATA_LIST_ENTITY).
                child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("lunches");
        myRef.push().setValue(lunch);
    }

    public static void addDinner(Dinner dinner){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(Config.NAME_OF_USER_DATA_LIST_ENTITY).
                child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("dinners");
        myRef.push().setValue(dinner);
    }

    public static void addSnack(Snack snack){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(Config.NAME_OF_USER_DATA_LIST_ENTITY).
                child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("snacks");
        myRef.push().setValue(snack);
    }
}

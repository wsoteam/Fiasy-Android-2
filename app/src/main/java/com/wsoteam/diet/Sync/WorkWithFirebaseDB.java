package com.wsoteam.diet.Sync;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.adjust.sdk.Adjust;
import com.adjust.sdk.AdjustAttribution;
import com.adjust.sdk.AdjustEvent;
import com.amplitude.api.Amplitude;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wsoteam.diet.Activism.POJO.ActivismFirebaseObject;
import com.wsoteam.diet.Articles.POJO.ListArticles;
import com.wsoteam.diet.BranchOfAnalyzer.Const;
import com.wsoteam.diet.BranchOfAnalyzer.POJOClaim.Claim;
import com.wsoteam.diet.BranchOfAnalyzer.POJOEating.Breakfast;
import com.wsoteam.diet.BranchOfAnalyzer.POJOEating.Dinner;
import com.wsoteam.diet.BranchOfAnalyzer.POJOEating.Eating;
import com.wsoteam.diet.BranchOfAnalyzer.POJOEating.Lunch;
import com.wsoteam.diet.BranchOfAnalyzer.POJOEating.Snack;
import com.wsoteam.diet.Config;
import com.wsoteam.diet.POJOForDB.DiaryData;
import com.wsoteam.diet.POJOProfile.FavoriteFood;
import com.wsoteam.diet.POJOProfile.Profile;
import com.wsoteam.diet.POJOProfile.SubInfo;
import com.wsoteam.diet.POJOProfile.TrackInfo;
import com.wsoteam.diet.R;
import com.wsoteam.diet.Recipes.POJO.ListRecipes;
import com.wsoteam.diet.Recipes.POJO.RecipeItem;
import com.wsoteam.diet.Sync.POJO.UserData;
import com.wsoteam.diet.Sync.POJO.WeightDiaryObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WorkWithFirebaseDB {

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


    public static void setSubInfo(SubInfo subInfo){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(Config.NAME_OF_USER_DATA_LIST_ENTITY).
                child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("subInfo");
        myRef.setValue(subInfo);
    }

    public static void setTrackInfo(TrackInfo trackInfo){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(Config.NAME_OF_USER_DATA_LIST_ENTITY).
                child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("trackInfo");
        myRef.setValue(trackInfo);
    }

    public static void check(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(Config.NAME_OF_USER_DATA_LIST_ENTITY);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HashMap<String, UserData> stringUserDataHashMap = (HashMap<String, UserData>) dataSnapshot.getValue();
                Log.e("LOL", String.valueOf(stringUserDataHashMap.size()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static void addUserRecipe(RecipeItem recipeItem) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(Config.NAME_OF_USER_DATA_LIST_ENTITY).
                child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("recipes");
        myRef.push().setValue(recipeItem);
    }


    public static void saveListRecipesNew(ListRecipes data){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("RECIPES_PLANS_NEW");
        myRef.setValue(data);
    }

    public static void saveListArticles(ListArticles data) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("ARTICLES");
        myRef.setValue(data);
    }

    public static void saveActivism(ActivismFirebaseObject data) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("ACTIVISM");
        myRef.setValue(data);
    }

    public static void sendClaim(Claim claim) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(Const.CLAIM_PATH);
        myRef.push().setValue(claim);
    }

    public static void addFoodFavorite(FavoriteFood food) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(Config.NAME_OF_USER_DATA_LIST_ENTITY).
                child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("foodFavorites");
        myRef.push().setValue(food);
    }

}

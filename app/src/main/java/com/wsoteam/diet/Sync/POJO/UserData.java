package com.wsoteam.diet.Sync.POJO;

import com.wsoteam.diet.BranchOfAnalyzer.POJOEating.Breakfast;
import com.wsoteam.diet.BranchOfAnalyzer.POJOEating.Dinner;
import com.wsoteam.diet.BranchOfAnalyzer.POJOEating.Lunch;
import com.wsoteam.diet.BranchOfAnalyzer.POJOEating.Snack;
import com.wsoteam.diet.POJOForDB.DiaryData;
import com.wsoteam.diet.POJOProfile.Profile;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public class UserData implements Serializable {
    private String name;
    private Profile profile;

    private HashMap<String, Breakfast> breakfasts;
    private HashMap<String, Lunch> lunches;
    private HashMap<String, Dinner> dinners;
    private HashMap<String, Snack> snacks;
    private HashMap<String, WeightDiaryObject> diaryDataList;

    public UserData() {
    }

    public UserData(String name, Profile profile, HashMap<String, Breakfast> breakfasts, HashMap<String, Lunch> lunches, HashMap<String, Dinner> dinners, HashMap<String, Snack> snacks, HashMap<String, WeightDiaryObject> diaryDataList) {
        this.name = name;
        this.profile = profile;
        this.breakfasts = breakfasts;
        this.lunches = lunches;
        this.dinners = dinners;
        this.snacks = snacks;
        this.diaryDataList = diaryDataList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public HashMap<String, Breakfast> getBreakfasts() {
        return breakfasts;
    }

    public void setBreakfasts(HashMap<String, Breakfast> breakfasts) {
        this.breakfasts = breakfasts;
    }

    public HashMap<String, Lunch> getLunches() {
        return lunches;
    }

    public void setLunches(HashMap<String, Lunch> lunches) {
        this.lunches = lunches;
    }

    public HashMap<String, Dinner> getDinners() {
        return dinners;
    }

    public void setDinners(HashMap<String, Dinner> dinners) {
        this.dinners = dinners;
    }

    public HashMap<String, Snack> getSnacks() {
        return snacks;
    }

    public void setSnacks(HashMap<String, Snack> snacks) {
        this.snacks = snacks;
    }

    public HashMap<String, WeightDiaryObject> getDiaryDataList() {
        return diaryDataList;
    }

    public void setDiaryDataList(HashMap<String, WeightDiaryObject> diaryDataList) {
        this.diaryDataList = diaryDataList;
    }
}

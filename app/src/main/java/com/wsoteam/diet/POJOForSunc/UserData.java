package com.wsoteam.diet.POJOForSunc;

import com.wsoteam.diet.BranchOfAnalyzer.POJOEating.Breakfast;
import com.wsoteam.diet.BranchOfAnalyzer.POJOEating.Dinner;
import com.wsoteam.diet.BranchOfAnalyzer.POJOEating.Lunch;
import com.wsoteam.diet.BranchOfAnalyzer.POJOEating.Snack;
import com.wsoteam.diet.POJOForDB.DiaryData;
import com.wsoteam.diet.POJOProfile.Profile;

import java.io.Serializable;
import java.util.List;

public class UserData implements Serializable {
    private String uId;
    private Profile profile;

    private List<Breakfast> breakfasts;
    private List<Lunch> lunches;
    private List<Dinner> dinners;
    private List<Snack> snacks;
    private List<DiaryData> diaryDataList;

    public UserData() {
    }

    public UserData(String uId, Profile profile, List<Breakfast> breakfasts, List<Lunch> lunches, List<Dinner> dinners, List<Snack> snacks, List<DiaryData> diaryDataList) {
        this.uId = uId;
        this.profile = profile;
        this.breakfasts = breakfasts;
        this.lunches = lunches;
        this.dinners = dinners;
        this.snacks = snacks;
        this.diaryDataList = diaryDataList;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public List<Breakfast> getBreakfasts() {
        return breakfasts;
    }

    public void setBreakfasts(List<Breakfast> breakfasts) {
        this.breakfasts = breakfasts;
    }

    public List<Lunch> getLunches() {
        return lunches;
    }

    public void setLunches(List<Lunch> lunches) {
        this.lunches = lunches;
    }

    public List<Dinner> getDinners() {
        return dinners;
    }

    public void setDinners(List<Dinner> dinners) {
        this.dinners = dinners;
    }

    public List<Snack> getSnacks() {
        return snacks;
    }

    public void setSnacks(List<Snack> snacks) {
        this.snacks = snacks;
    }

    public List<DiaryData> getDiaryDataList() {
        return diaryDataList;
    }

    public void setDiaryDataList(List<DiaryData> diaryDataList) {
        this.diaryDataList = diaryDataList;
    }
}

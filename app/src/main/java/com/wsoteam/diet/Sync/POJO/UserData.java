package com.wsoteam.diet.Sync.POJO;

import com.wsoteam.diet.POJOProfile.CheckInfo.CheckHistory;
import com.wsoteam.diet.POJOProfile.Profile;
import com.wsoteam.diet.POJOProfile.SubInfo;
import com.wsoteam.diet.POJOProfile.TrackInfo;
import com.wsoteam.diet.model.Breakfast;
import com.wsoteam.diet.model.Dinner;
import com.wsoteam.diet.model.Lunch;
import com.wsoteam.diet.model.Snack;
import com.wsoteam.diet.model.Water;
import com.wsoteam.diet.model.main.Day;

import java.io.Serializable;
import java.util.HashMap;

public class UserData implements Serializable {
    private String name;
    private Profile profile;
    private SubInfo subInfo;
    private TrackInfo trackInfo;
    private CheckHistory checkHistory;

    private HashMap<String, Day> dayList;

    private HashMap<String, Breakfast> breakfasts;
    private HashMap<String, Lunch> lunches;
    private HashMap<String, Dinner> dinners;
    private HashMap<String, Snack> snacks;
    private HashMap<String, Water> water;
    private HashMap<String, WeightDiaryObject> diaryDataList;

    public UserData() {
    }

    public UserData(String name, Profile profile, SubInfo subInfo, TrackInfo trackInfo, CheckHistory checkHistory, HashMap<String, Breakfast> breakfasts, HashMap<String, Lunch> lunches, HashMap<String, Dinner> dinners, HashMap<String, Snack> snacks, HashMap<String, Water> water, HashMap<String, WeightDiaryObject> diaryDataList) {
        this.name = name;
        this.profile = profile;
        this.subInfo = subInfo;
        this.trackInfo = trackInfo;
        this.checkHistory = checkHistory;
        this.breakfasts = breakfasts;
        this.lunches = lunches;
        this.dinners = dinners;
        this.snacks = snacks;
        this.water = water;
        this.diaryDataList = diaryDataList;
    }

    public SubInfo getSubInfo() {
        return subInfo;
    }

    public void setSubInfo(SubInfo subInfo) {
        this.subInfo = subInfo;
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

    public HashMap<String, Day> getDayList() {
        return dayList;
    }

    public void setDayList(HashMap<String, Day> dayList) {
        this.dayList = dayList;
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

    public HashMap<String, Water> getWater() {
        return water;
    }

    public void setWater(HashMap<String, Water> water) {
        this.water = water;
    }

    public HashMap<String, WeightDiaryObject> getDiaryDataList() {
        return diaryDataList;
    }

    public void setDiaryDataList(HashMap<String, WeightDiaryObject> diaryDataList) {
        this.diaryDataList = diaryDataList;
    }

    public TrackInfo getTrackInfo() {
        return trackInfo;
    }

    public void setTrackInfo(TrackInfo trackInfo) {
        this.trackInfo = trackInfo;
    }

    public CheckHistory getCheckHistory() {
        return checkHistory;
    }

    public void setCheckHistory(CheckHistory checkHistory) {
        this.checkHistory = checkHistory;
    }
}

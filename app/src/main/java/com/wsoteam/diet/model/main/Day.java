package com.wsoteam.diet.model.main;

import com.wsoteam.diet.Sync.POJO.WeightDiaryObject;
import com.wsoteam.diet.model.Breakfast;
import com.wsoteam.diet.model.Dinner;
import com.wsoteam.diet.model.Lunch;
import com.wsoteam.diet.model.Snack;
import com.wsoteam.diet.model.Water;

import java.io.Serializable;

public class Day implements Serializable {

    private Breakfast breakfast;
    private Lunch lunch;
    private Dinner dinner;
    private Snack snack;
    private Water water;
    private WeightDiaryObject diaryDataList;

    public Breakfast getBreakfast() {
        return breakfast;
    }

    public void setBreakfast(Breakfast breakfast) {
        this.breakfast = breakfast;
    }

    public Lunch getLunch() {
        return lunch;
    }

    public void setLunch(Lunch lunch) {
        this.lunch = lunch;
    }

    public Dinner getDinner() {
        return dinner;
    }

    public void setDinner(Dinner dinner) {
        this.dinner = dinner;
    }

    public Snack getSnack() {
        return snack;
    }

    public void setSnack(Snack snack) {
        this.snack = snack;
    }

    public Water getWater() {
        return water;
    }

    public void setWater(Water water) {
        this.water = water;
    }

    public WeightDiaryObject getDiaryDataList() {
        return diaryDataList;
    }

    public void setDiaryDataList(WeightDiaryObject diaryDataList) {
        this.diaryDataList = diaryDataList;
    }

    public boolean isTrackDay() {
        return breakfast != null || lunch != null || dinner != null || snack != null || water != null || diaryDataList != null;
    }
}

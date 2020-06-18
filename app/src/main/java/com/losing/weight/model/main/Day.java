package com.losing.weight.model.main;

import com.losing.weight.Sync.POJO.WeightDiaryObject;
import com.losing.weight.model.Breakfast;
import com.losing.weight.model.Dinner;
import com.losing.weight.model.Lunch;
import com.losing.weight.model.Snack;
import com.losing.weight.model.Water;

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

package com.wsoteam.diet.model;

import java.io.Serializable;

public class Water extends Eating implements Serializable {

    private int day;
    private int month;
    private int year;
    private int waterCount;
    private String key;

    //private int count;
    //private int maxCount;
    //private boolean waterPack;

    public Water() {
    }

    public Water(int day, int month, int year, int waterCount) {
        this.day = day;
        this.month = month;
        this.year = year;
        this.waterCount = waterCount;
    }

    public Water(String name, String urlOfImages, int calories, int carbohydrates, int protein, int fat, int weight, int day, int month, int year) {
        super(name, urlOfImages, calories, carbohydrates, protein, fat, weight, day, month, year);
    }

    @Override public int getDay() {
        return day;
    }

    @Override public void setDay(int day) {
        this.day = day;
    }

    @Override public int getMonth() {
        return month;
    }

    @Override public void setMonth(int month) {
        this.month = month;
    }

    @Override public int getYear() {
        return year;
    }

    @Override public void setYear(int year) {
        this.year = year;
    }

    public int getWaterCount() {
        return waterCount;
    }

    public void setWaterCount(int waterCount) {
        this.waterCount = waterCount;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}

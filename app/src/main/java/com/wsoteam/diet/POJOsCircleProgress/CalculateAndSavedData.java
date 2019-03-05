package com.wsoteam.diet.POJOsCircleProgress;

import com.orm.SugarRecord;

public class CalculateAndSavedData extends SugarRecord {
    private String userName;

    private int dayOfMonth;
    private int month;
    private int year;

    private int protein;
    private int calories;
    private int fat;
    private int carbohydrates;

    public CalculateAndSavedData() {
    }

    public CalculateAndSavedData(String userName, int dayOfMonth, int month, int year, int protein, int calories, int fat, int carbohydrates) {
        this.userName = userName;
        this.dayOfMonth = dayOfMonth;
        this.month = month;
        this.year = year;
        this.protein = protein;
        this.calories = calories;
        this.fat = fat;
        this.carbohydrates = carbohydrates;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getDayOfMonth() {
        return dayOfMonth;
    }

    public void setDayOfMonth(int dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getProtein() {
        return protein;
    }

    public void setProtein(int protein) {
        this.protein = protein;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public int getFat() {
        return fat;
    }

    public void setFat(int fat) {
        this.fat = fat;
    }

    public int getCarbohydrates() {
        return carbohydrates;
    }

    public void setCarbohydrates(int carbohydrates) {
        this.carbohydrates = carbohydrates;
    }
}

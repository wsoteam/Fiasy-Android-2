package com.losing.weight.BranchOfAnalyzer.POJOClaim;

import java.io.Serializable;

public class Claim implements Serializable {
    private int day;
    private int month;
    private int year;
    private int minutes;
    private int hours;

    private String userId;
    private String textClaim;
    private String foodInfo;
    private long foodId;

    public Claim() {
    }

    public Claim(int day, int month, int year, int minutes, int hours, String userId, String textClaim, String foodInfo, long foodId) {
        this.day = day;
        this.month = month;
        this.year = year;
        this.minutes = minutes;
        this.hours = hours;
        this.userId = userId;
        this.textClaim = textClaim;
        this.foodInfo = foodInfo;
        this.foodId = foodId;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
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

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTextClaim() {
        return textClaim;
    }

    public void setTextClaim(String textClaim) {
        this.textClaim = textClaim;
    }

    public String getFoodInfo() {
        return foodInfo;
    }

    public void setFoodInfo(String foodInfo) {
        this.foodInfo = foodInfo;
    }

    public long getFoodId() {
        return foodId;
    }

    public void setFoodId(long foodId) {
        this.foodId = foodId;
    }
}

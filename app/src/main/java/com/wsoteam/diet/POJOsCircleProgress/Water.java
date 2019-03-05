package com.wsoteam.diet.POJOsCircleProgress;

import com.orm.SugarRecord;

public class Water extends SugarRecord {
    private int day;
    private int month;
    private int year;

    private int step;
    private int currentNumber;

    public Water() {
    }

    public Water(int day, int month, int year, int step, int currentNumber) {
        this.day = day;
        this.month = month;
        this.year = year;
        this.step = step;
        this.currentNumber = currentNumber;
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

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public int getCurrentNumber() {
        return currentNumber;
    }

    public void setCurrentNumber(int currentNumber) {
        this.currentNumber = currentNumber;
    }
}

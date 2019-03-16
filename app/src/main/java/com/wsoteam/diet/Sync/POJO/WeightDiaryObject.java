package com.wsoteam.diet.Sync.POJO;

import java.io.Serializable;

public class WeightDiaryObject implements Serializable {
    private int ownId;
    private int numberOfDay;
    private int month;
    private int year;
    private double weight;
    private int chest;
    private int waist;
    private int hips;
    private String note;
    private String nameOfMonth;

    public WeightDiaryObject() {
    }

    public WeightDiaryObject(int ownId, int numberOfDay, int month, int year, double weight, int chest, int waist, int hips, String note, String nameOfMonth) {
        this.ownId = ownId;
        this.numberOfDay = numberOfDay;
        this.month = month;
        this.year = year;
        this.weight = weight;
        this.chest = chest;
        this.waist = waist;
        this.hips = hips;
        this.note = note;
        this.nameOfMonth = nameOfMonth;
    }

    public int getOwnId() {
        return ownId;
    }

    public void setOwnId(int ownId) {
        this.ownId = ownId;
    }

    public int getNumberOfDay() {
        return numberOfDay;
    }

    public void setNumberOfDay(int numberOfDay) {
        this.numberOfDay = numberOfDay;
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

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public int getChest() {
        return chest;
    }

    public void setChest(int chest) {
        this.chest = chest;
    }

    public int getWaist() {
        return waist;
    }

    public void setWaist(int waist) {
        this.waist = waist;
    }

    public int getHips() {
        return hips;
    }

    public void setHips(int hips) {
        this.hips = hips;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getNameOfMonth() {
        return nameOfMonth;
    }

    public void setNameOfMonth(String nameOfMonth) {
        this.nameOfMonth = nameOfMonth;
    }
}

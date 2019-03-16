package com.wsoteam.diet.POJOForDB;

import com.orm.SugarRecord;

import java.io.Serializable;

public class DiaryData extends SugarRecord implements Serializable {
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

    public DiaryData() {
    }

    public DiaryData(int ownId, int numberOfDay, int month, int year, double weight, int chest, int waist, int hips, String note, String nameOfMonth) {
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

    public void setNameOfMonth(int nameOfMonth) {
        switch (nameOfMonth) {
            case 0:
                this.nameOfMonth = "январь";
                break;
            case 1:
                this.nameOfMonth = "февраль";
                break;
            case 2:
                this.nameOfMonth = "март";
                break;
            case 3:
                this.nameOfMonth = "апрель";
                break;
            case 4:
                this.nameOfMonth = "май";
                break;
            case 5:
                this.nameOfMonth = "июнь";
                break;
            case 6:
                this.nameOfMonth = "июль";
                break;
            case 7:
                this.nameOfMonth = "август";
                break;
            case 8:
                this.nameOfMonth = "сентябрь";
                break;
            case 9:
                this.nameOfMonth = "октябрь";
                break;
            case 10:
                this.nameOfMonth = "ноябрь";
                break;
            case 11:
                this.nameOfMonth = "декабрь";
                break;
        }
    }

    @Override
    public String toString() {
        return "DiaryData{" +
                "ownId=" + ownId +
                ", numberOfDay=" + numberOfDay +
                ", month=" + month +
                ", year=" + year +
                ", weight=" + weight +
                ", chest=" + chest +
                ", waist=" + waist +
                ", hips=" + hips +
                ", note='" + note + '\'' +
                '}';
    }
}

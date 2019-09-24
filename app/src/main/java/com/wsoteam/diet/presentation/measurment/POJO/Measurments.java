package com.wsoteam.diet.presentation.measurment.POJO;

import java.io.Serializable;

public class Measurments implements Serializable {

    private String key;
    private long timeInMillis;
    private double chest;
    private double waist;
    private double hips;

    public Measurments() {
    }

    public Measurments(String key, long timeInMillis, double chest, double waist, double hips) {
        this.key = key;
        this.timeInMillis = timeInMillis;
        this.chest = chest;
        this.waist = waist;
        this.hips = hips;
    }

    public long getTimeInMillis() {
        return timeInMillis;
    }

    public void setTimeInMillis(long timeInMillis) {
        this.timeInMillis = timeInMillis;
    }

    public double getChest() {
        return chest;
    }

    public void setChest(double chest) {
        this.chest = chest;
    }

    public double getWaist() {
        return waist;
    }

    public void setWaist(double waist) {
        this.waist = waist;
    }

    public double getHips() {
        return hips;
    }

    public void setHips(double hips) {
        this.hips = hips;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return "Measurments{" +
                "key='" + key + '\'' +
                ", timeInMillis=" + timeInMillis +
                ", chest=" + chest +
                ", waist=" + waist +
                ", hips=" + hips +
                '}';
    }
}

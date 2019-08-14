package com.wsoteam.diet.presentation.measurment.POJO;

import java.io.Serializable;

public class Measurment implements Serializable {

    private long timeInMillis;
    private double weight;
    private double chest;
    private double waist;
    private double hips;

    public Measurment() {
    }

    public Measurment(long timeInMillis, double weight, double chest, double waist, double hips) {
        this.timeInMillis = timeInMillis;
        this.weight = weight;
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

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
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

    @Override
    public String toString() {
        return "Measurment{" +
                "timeInMillis=" + timeInMillis +
                ", weight=" + weight +
                ", chest=" + chest +
                ", waist=" + waist +
                ", hips=" + hips +
                '}';
    }
}

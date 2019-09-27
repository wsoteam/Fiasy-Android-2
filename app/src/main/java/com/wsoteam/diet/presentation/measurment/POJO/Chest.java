package com.wsoteam.diet.presentation.measurment.POJO;

import java.io.Serializable;

public class Chest implements Serializable {
    private String key;
    private long timeInMillis;
    private double weight;

    public Chest() {
    }

    public Chest(String key, long timeInMillis, double weight) {
        this.key = key;
        this.timeInMillis = timeInMillis;
        this.weight = weight;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
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
}

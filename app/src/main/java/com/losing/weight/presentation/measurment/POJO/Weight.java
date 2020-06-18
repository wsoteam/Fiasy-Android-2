package com.losing.weight.presentation.measurment.POJO;

import java.io.Serializable;

public class Weight implements Serializable {

    private String key;
    private long timeInMillis;
    private double weight;

    public Weight() {
    }

    public Weight(String key, long timeInMillis, double weight) {
        this.key = key;
        this.timeInMillis = timeInMillis;
        this.weight = weight;
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

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return "Weight{" +
                "key='" + key + '\'' +
                ", timeInMillis=" + timeInMillis +
                ", weight=" + weight +
                '}';
    }
}

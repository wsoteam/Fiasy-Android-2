package com.losing.weight.presentation.measurment.POJO;

import java.io.Serializable;

public class Meas implements Serializable {
    private String key;
    private long timeInMillis;
    private int meas;

    public Meas() {
    }

    public Meas(String key, long timeInMillis, int meas) {
        this.key = key;
        this.timeInMillis = timeInMillis;
        this.meas = meas;
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

    public int getMeas() {
        return meas;
    }

    public void setMeas(int meas) {
        this.meas = meas;
    }
}

package com.losing.weight.presentation.measurment.POJO;

import java.io.Serializable;

public class Hips extends Meas implements Serializable {
    public Hips() {
    }

    public Hips(String key, long timeInMillis, int meas) {
        super(key, timeInMillis, meas);
    }
}

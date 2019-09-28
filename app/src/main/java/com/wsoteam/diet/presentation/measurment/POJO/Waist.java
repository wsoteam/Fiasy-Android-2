package com.wsoteam.diet.presentation.measurment.POJO;

import java.io.Serializable;

public class Waist extends Meas implements Serializable {
    public Waist() {
    }

    public Waist(String key, long timeInMillis, int meas) {
        super(key, timeInMillis, meas);
    }
}

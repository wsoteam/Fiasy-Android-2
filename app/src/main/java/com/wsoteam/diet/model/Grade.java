package com.wsoteam.diet.model;

import java.io.Serializable;

public class Grade implements Serializable {

    private String userId;
    private double rating;
    private String msg;
    private long dateMillis;

    public Grade() {
    }

    public Grade(long dateMillis) {
        this.dateMillis = dateMillis;
    }

    public long getDateMillis() {
        return dateMillis;
    }

    public void setDateMillis(long dateMillis) {
        this.dateMillis = dateMillis;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}

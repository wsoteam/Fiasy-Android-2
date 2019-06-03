package com.wsoteam.diet.Activism.POJO;

import java.io.Serializable;

public class Activism implements Serializable {

    String imageURL;
    String name;
    int calories;
    int duration;

    public Activism() {
    }

    public Activism(String imageURL, String name, int calories, int duration) {
        this.imageURL = imageURL;
        this.name = name;
        this.calories = calories;
        this.duration = duration;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}

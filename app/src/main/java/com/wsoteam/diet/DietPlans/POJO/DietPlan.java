package com.wsoteam.diet.DietPlans.POJO;

import java.io.Serializable;

public class DietPlan implements Serializable {
    private String name;
    private int countDays;
    private String text;
    private String urlImage;
    private String flag;

    public DietPlan() {
    }

    public DietPlan(String name, int countDays, String text, String urlImage, String flag) {
        this.name = name;
        this.countDays = countDays;
        this.text = text;
        this.urlImage = urlImage;
        this.flag = flag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCountDays() {
        return countDays;
    }

    public void setCountDays(int countDays) {
        this.countDays = countDays;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }
}

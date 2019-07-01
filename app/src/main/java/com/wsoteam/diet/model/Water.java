package com.wsoteam.diet.model;

import java.io.Serializable;

public class Water extends Eating implements Serializable {

    private int count;
    private int maxCount;
    private boolean waterPack;

    public Water(int count, int maxCount, boolean waterPack) {
        this.count = count;
        this.maxCount = maxCount;
        this.waterPack = waterPack;
    }

    public Water(String name, String urlOfImages, int calories, int carbohydrates, int protein, int fat, int weight, int day, int month, int year) {
        super(name, urlOfImages, calories, carbohydrates, protein, fat, weight, day, month, year);
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getMaxCount() {
        return maxCount;
    }

    public void setMaxCount(int maxCount) {
        this.maxCount = maxCount;
    }

    public boolean isWaterPack() {
        return waterPack;
    }

    public void setWaterPack(boolean waterPack) {
        this.waterPack = waterPack;
    }
}

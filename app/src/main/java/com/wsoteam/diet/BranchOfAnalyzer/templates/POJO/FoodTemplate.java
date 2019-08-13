package com.wsoteam.diet.BranchOfAnalyzer.templates.POJO;

import com.wsoteam.diet.BranchOfAnalyzer.POJOFoodSQL.Food;

import java.io.Serializable;
import java.util.List;

public class FoodTemplate implements Serializable {

    private String name;
    private String eating;
    private String key;
    private boolean isShowFoods;


    private List<Food> foodList;

    public FoodTemplate() {
    }

    public FoodTemplate(String name, String eating, String key, boolean isShowFoods, List<Food> foodList) {
        this.name = name;
        this.eating = eating;
        this.key = key;
        this.isShowFoods = isShowFoods;
        this.foodList = foodList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEating() {
        return eating;
    }

    public void setEating(String eating) {
        this.eating = eating;
    }

    public List<Food> getFoodList() {
        return foodList;
    }

    public void setFoodList(List<Food> foodList) {
        this.foodList = foodList;
    }

    public boolean isShowFoods() {
        return isShowFoods;
    }

    public void setShowFoods(boolean showFoods) {
        isShowFoods = showFoods;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}

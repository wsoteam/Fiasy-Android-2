package com.wsoteam.diet.BranchOfAnalyzer.templates.POJO;

import com.wsoteam.diet.BranchOfAnalyzer.POJOFoodSQL.Food;

import java.io.Serializable;
import java.util.List;

public class FoodTemplete implements Serializable {

    private String name;
    private List<Food> foodList;

    public FoodTemplete() {
    }

    public FoodTemplete(String name, List<Food> foodList) {
        this.name = name;
        this.foodList = foodList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Food> getFoodList() {
        return foodList;
    }

    public void setFoodList(List<Food> foodList) {
        this.foodList = foodList;
    }
}

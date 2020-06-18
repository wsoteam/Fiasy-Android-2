package com.losing.weight.BranchOfAnalyzer.templates.POJO;

import com.losing.weight.BranchOfAnalyzer.POJOFoodSQL.Food;

import java.util.List;

public class FoodTemplateHolder {

    private static List<Food> foods;

    public static void bind(List<Food> foods){
        FoodTemplateHolder.foods = foods;
    }

    public static List<Food> get(){
        return foods;
    }
}

package com.wsoteam.diet.common.backward;

import com.wsoteam.diet.BranchOfAnalyzer.POJOFoodSQL.Food;
import com.wsoteam.diet.POJOProfile.FavoriteFood;
import com.wsoteam.diet.common.networking.food.POJO.Result;

public class FoodConverter {

    public static Food convertResultToFood(Result result) {
        Food food = new Food();
        food.setId(result.getId());
        food.setName(result.getName());
        food.setBrand(result.getBrand().getName());
        food.setPortion(result.getPortion());
        food.setLiquid(result.isLiquid());
        food.setKilojoules(result.getKilojoules());
        food.setCalories(result.getCalories());
        food.setProteins(result.getProteins());
        food.setCarbohydrates(result.getCarbohydrates());
        food.setSugar(result.getSugar());
        food.setFats(result.getFats());
        food.setSaturatedFats(result.getSaturatedFats());
        food.setMonoUnSaturatedFats(result.getMonounsaturatedFats());
        food.setPolyUnSaturatedFats(result.getPolyunsaturatedFats());
        food.setCholesterol(result.getCholesterol());
        food.setCellulose(result.getCellulose());
        food.setPottassium(result.getPottasium());
        food.setSodium(result.getSodium());
        return food;
    }


    public static Food convertFavoriteToFood(FavoriteFood favoriteFood) {
        Food food = new Food();
        food.setId(favoriteFood.getId());
        food.setName(favoriteFood.getName());
        food.setBrand(favoriteFood.getBrand());
        food.setPortion(favoriteFood.getPortion());
        food.setLiquid(favoriteFood.isLiquid());
        food.setKilojoules(favoriteFood.getKilojoules());
        food.setCalories(favoriteFood.getCalories());
        food.setProteins(favoriteFood.getProteins());
        food.setCarbohydrates(favoriteFood.getCarbohydrates());
        food.setSugar(favoriteFood.getSugar());
        food.setFats(favoriteFood.getFats());
        food.setSaturatedFats(favoriteFood.getSaturatedFats());
        food.setMonoUnSaturatedFats(favoriteFood.getMonoUnSaturatedFats());
        food.setPolyUnSaturatedFats(favoriteFood.getPolyUnSaturatedFats());
        food.setCholesterol(favoriteFood.getCholesterol());
        food.setCellulose(favoriteFood.getCellulose());
        food.setPottassium(favoriteFood.getPottassium());
        food.setSodium(favoriteFood.getSodium());
        return food;
    }

}

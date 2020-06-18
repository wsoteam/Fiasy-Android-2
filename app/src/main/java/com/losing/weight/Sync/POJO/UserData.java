package com.losing.weight.Sync.POJO;

import com.losing.weight.BranchOfAnalyzer.CustomFood.CustomFood;
import com.losing.weight.BranchOfAnalyzer.templates.POJO.FoodTemplate;
import com.losing.weight.DietPlans.POJO.DietPlan;
import com.losing.weight.POJOProfile.CheckInfo.CheckHistory;
import com.losing.weight.POJOProfile.FavoriteFood;
import com.losing.weight.POJOProfile.Profile;
import com.losing.weight.POJOProfile.SubInfo;
import com.losing.weight.POJOProfile.TrackInfo;
import com.losing.weight.Recipes.POJO.RecipeItem;
import com.losing.weight.common.promo.POJO.UserPromo;
import com.losing.weight.model.Breakfast;
import com.losing.weight.model.Dinner;
import com.losing.weight.model.Lunch;
import com.losing.weight.model.OpenArticles;
import com.losing.weight.model.Snack;
import com.losing.weight.model.Water;
import com.losing.weight.presentation.measurment.POJO.Chest;
import com.losing.weight.presentation.measurment.POJO.Hips;
import com.losing.weight.presentation.measurment.POJO.Waist;
import com.losing.weight.presentation.measurment.POJO.Weight;

import java.io.Serializable;
import java.util.HashMap;

public class UserData implements Serializable {
    private String name;
    private Profile profile;
    private SubInfo subInfo;
    private TrackInfo trackInfo;
    private CheckHistory checkHistory;

    private HashMap<String, Breakfast> breakfasts;
    private HashMap<String, Lunch> lunches;
    private HashMap<String, Dinner> dinners;
    private HashMap<String, Snack> snacks;
    private HashMap<String, Water> waters;
    private HashMap<String, WeightDiaryObject> diaryDataList;
    private HashMap<String, RecipeItem> recipes;
    private HashMap<String, FavoriteFood> foodFavorites;
    private HashMap<String, RecipeItem> favoriteRecipes;
    private HashMap<String, CustomFood> customFoods;
    private HashMap<String, FoodTemplate> foodTemplates;
    private HashMap<String, OpenArticles> articleSeries;
    private DietPlan plan;
    private UserPromo userPromo;
    private HashMap<String, Weight> weights;
    private HashMap<String, Chest> chest;
    private HashMap<String, Waist> waist;
    private HashMap<String, Hips> hips;

    public UserData() {
    }

    public UserData(String name, Profile profile, SubInfo subInfo, TrackInfo trackInfo, CheckHistory checkHistory,
                    HashMap<String, Breakfast> breakfasts, HashMap<String, Lunch> lunches, HashMap<String, Dinner> dinners,
                    HashMap<String, Snack> snacks, HashMap<String, Water> waters, HashMap<String, WeightDiaryObject> diaryDataList,
                    HashMap<String, RecipeItem> recipes, HashMap<String, FavoriteFood> foodFavorites, HashMap<String, RecipeItem> favoriteRecipes,
                    HashMap<String, CustomFood> customFoods, HashMap<String, FoodTemplate> foodTemplates, DietPlan plan, UserPromo userPromo, HashMap<String, Weight> weights,
                    HashMap<String, Chest> chest, HashMap<String, Waist> waist, HashMap<String, Hips> hips) {
        this.name = name;
        this.profile = profile;
        this.subInfo = subInfo;
        this.trackInfo = trackInfo;
        this.checkHistory = checkHistory;
        this.breakfasts = breakfasts;
        this.lunches = lunches;
        this.dinners = dinners;
        this.snacks = snacks;
        this.waters = waters;
        this.diaryDataList = diaryDataList;
        this.recipes = recipes;
        this.foodFavorites = foodFavorites;
        this.favoriteRecipes = favoriteRecipes;
        this.customFoods = customFoods;
        this.foodTemplates = foodTemplates;
        this.plan = plan;
        this.userPromo = userPromo;
        this.weights = weights;
        this.chest = chest;
        this.waist = waist;
        this.hips = hips;
    }

    public HashMap<String, OpenArticles> getArticleSeries() {
        return articleSeries;
    }

    public void setArticleSeries(HashMap<String, OpenArticles> articleSeries) {
        this.articleSeries = articleSeries;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public SubInfo getSubInfo() {
        return subInfo;
    }

    public void setSubInfo(SubInfo subInfo) {
        this.subInfo = subInfo;
    }

    public TrackInfo getTrackInfo() {
        return trackInfo;
    }

    public void setTrackInfo(TrackInfo trackInfo) {
        this.trackInfo = trackInfo;
    }

    public CheckHistory getCheckHistory() {
        return checkHistory;
    }

    public void setCheckHistory(CheckHistory checkHistory) {
        this.checkHistory = checkHistory;
    }

    public HashMap<String, Breakfast> getBreakfasts() {
        return breakfasts;
    }

    public void setBreakfasts(HashMap<String, Breakfast> breakfasts) {
        this.breakfasts = breakfasts;
    }

    public HashMap<String, Lunch> getLunches() {
        return lunches;
    }

    public void setLunches(HashMap<String, Lunch> lunches) {
        this.lunches = lunches;
    }

    public HashMap<String, Dinner> getDinners() {
        return dinners;
    }

    public void setDinners(HashMap<String, Dinner> dinners) {
        this.dinners = dinners;
    }

    public HashMap<String, Snack> getSnacks() {
        return snacks;
    }

    public void setSnacks(HashMap<String, Snack> snacks) {
        this.snacks = snacks;
    }

    public HashMap<String, Water> getWaters() {
        return waters;
    }

    public void setWaters(HashMap<String, Water> waters) {
        this.waters = waters;
    }

    public HashMap<String, WeightDiaryObject> getDiaryDataList() {
        return diaryDataList;
    }

    public void setDiaryDataList(HashMap<String, WeightDiaryObject> diaryDataList) {
        this.diaryDataList = diaryDataList;
    }

    public HashMap<String, RecipeItem> getRecipes() {
        return recipes;
    }

    public void setRecipes(HashMap<String, RecipeItem> recipes) {
        this.recipes = recipes;
    }

    public HashMap<String, FavoriteFood> getFoodFavorites() {
        return foodFavorites;
    }

    public void setFoodFavorites(HashMap<String, FavoriteFood> foodFavorites) {
        this.foodFavorites = foodFavorites;
    }

    public HashMap<String, RecipeItem> getFavoriteRecipes() {
        return favoriteRecipes;
    }

    public void setFavoriteRecipes(HashMap<String, RecipeItem> favoriteRecipes) {
        this.favoriteRecipes = favoriteRecipes;
    }

    public HashMap<String, CustomFood> getCustomFoods() {
        return customFoods;
    }

    public void setCustomFoods(HashMap<String, CustomFood> customFoods) {
        this.customFoods = customFoods;
    }

    public HashMap<String, FoodTemplate> getFoodTemplates() {
        return foodTemplates;
    }

    public void setFoodTemplates(HashMap<String, FoodTemplate> foodTemplates) {
        this.foodTemplates = foodTemplates;
    }

    public DietPlan getPlan() {
        return plan;
    }

    public void setPlan(DietPlan plan) {
        this.plan = plan;
    }

    public UserPromo getUserPromo() {
        return userPromo;
    }

    public void setUserPromo(UserPromo userPromo) {
        this.userPromo = userPromo;
    }

    public HashMap<String, Weight> getWeights() {
        return weights;
    }

    public void setWeights(HashMap<String, Weight> weights) {
        this.weights = weights;
    }

    public HashMap<String, Chest> getChest() {
        return chest;
    }

    public void setChest(HashMap<String, Chest> chest) {
        this.chest = chest;
    }

    public HashMap<String, Waist> getWaist() {
        return waist;
    }

    public void setWaist(HashMap<String, Waist> waist) {
        this.waist = waist;
    }

    public HashMap<String, Hips> getHips() {
        return hips;
    }

    public void setHips(HashMap<String, Hips> hips) {
        this.hips = hips;
    }
}

package com.wsoteam.diet.Sync.POJO;

import com.wsoteam.diet.BranchOfAnalyzer.CustomFood.CustomFood;
import com.wsoteam.diet.BranchOfAnalyzer.templates.POJO.FoodTemplate;
import com.wsoteam.diet.DietPlans.POJO.DietPlan;
import com.wsoteam.diet.POJOProfile.CheckInfo.CheckHistory;
import com.wsoteam.diet.POJOProfile.FavoriteFood;
import com.wsoteam.diet.POJOProfile.Profile;
import com.wsoteam.diet.POJOProfile.SubInfo;
import com.wsoteam.diet.POJOProfile.TrackInfo;
import com.wsoteam.diet.Recipes.POJO.RecipeItem;
import com.wsoteam.diet.common.promo.POJO.UserPromo;
import com.wsoteam.diet.model.Breakfast;
import com.wsoteam.diet.model.Dinner;
import com.wsoteam.diet.model.Lunch;
import com.wsoteam.diet.model.Snack;
import com.wsoteam.diet.model.Water;
import com.wsoteam.diet.presentation.measurment.POJO.Chest;
import com.wsoteam.diet.presentation.measurment.POJO.Hips;
import com.wsoteam.diet.presentation.measurment.POJO.Waist;
import com.wsoteam.diet.presentation.measurment.POJO.Weight;

import com.wsoteam.diet.presentation.activity.UserActivityExercise;
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

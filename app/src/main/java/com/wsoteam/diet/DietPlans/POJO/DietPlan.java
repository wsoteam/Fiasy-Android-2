package com.wsoteam.diet.DietPlans.POJO;

import com.wsoteam.diet.Recipes.POJO.plan.RecipeForDay;
import java.io.Serializable;
import java.util.List;

public class DietPlan implements Serializable {
    private String name;
    private int countDays;
    private String text;
    private String urlImage;
    private String flag;
    private boolean premium;

    private int recipeCount;
    private List<RecipeForDay> recipeForDays;

    public DietPlan() {
    }

    public DietPlan(String name, int countDays, String text, String urlImage, String flag, boolean premium) {
        this.name = name;
        this.countDays = countDays;
        this.text = text;
        this.urlImage = urlImage;
        this.flag = flag;
        this.premium = premium;
    }

    public void setRecipes(List<RecipeForDay> recipeForDays, int recipeCount){
        this.recipeForDays = recipeForDays;
        this.recipeCount = recipeCount;
    }

    public int getRecipeCount() {
        return recipeCount;
    }

    public List<RecipeForDay> getRecipeForDays() {
        return recipeForDays;
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

    public boolean isPremium() {
        return premium;
    }

    public void setPremium(boolean premium) {
        this.premium = premium;
    }
}

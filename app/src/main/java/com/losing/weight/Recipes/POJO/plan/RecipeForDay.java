package com.losing.weight.Recipes.POJO.plan;

import com.losing.weight.Recipes.POJO.RecipeItem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RecipeForDay implements Serializable {

    List<RecipeItem> breakfast = new ArrayList<>();
    List<RecipeItem> lunch = new ArrayList<>();
    List<RecipeItem> dinner = new ArrayList<>();
    List<RecipeItem> snack = new ArrayList<>();

    public RecipeForDay() {
    }

    public RecipeForDay(List<RecipeItem> breakfast,
        List<RecipeItem> lunch, List<RecipeItem> dinner,
        List<RecipeItem> snack) {
        this.breakfast = breakfast;
        this.lunch = lunch;
        this.dinner = dinner;
        this.snack = snack;
    }

    public List<RecipeItem> getBreakfast() {
        return breakfast;
    }

    public void setBreakfast(List<RecipeItem> breakfast) {
        this.breakfast = breakfast;
    }

    public List<RecipeItem> getLunch() {
        return lunch;
    }

    public void setLunch(List<RecipeItem> lunch) {
        this.lunch = lunch;
    }

    public List<RecipeItem> getDinner() {
        return dinner;
    }

    public void setDinner(List<RecipeItem> dinner) {
        this.dinner = dinner;
    }

    public List<RecipeItem> getSnack() {
        return snack;
    }

    public void setSnack(List<RecipeItem> snack) {
        this.snack = snack;
    }
}

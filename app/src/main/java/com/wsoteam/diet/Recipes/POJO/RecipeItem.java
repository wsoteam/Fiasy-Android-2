package com.wsoteam.diet.Recipes.POJO;

import java.io.Serializable;
import java.util.List;

public class RecipeItem implements Serializable {
    private String name;
    private String url;
    private int time;
    private int calories;
    private String description;

    private double percentCarbohydrates;
    private double percentProteins;
    private double percentFats;

    private double carbohydrates;
    private double proteins;
    private double fats;
    private double cellulose;
    private double sugar;

    private double saturatedFats;
    private double unSaturatedFats;

    private double cholesterol;
    private double sodium;
    private double potassium;

    private int portions;


    private List<String> ingredients;
    private List<String> instruction;
    private List<String> eating;
    private List<String> diet;

    public RecipeItem() {
    }

    public RecipeItem(String url, String name, int time, int calories, String description,
                      double percentCarbohydrates, double percentProteins,
                      double percentFats, double carbohydrates, double proteins,
                      double fats, double cellulose, double sugar, double saturatedFats,
                      double unSaturatedFats, double cholesterol, double sodium, double potassium,
                      int portions, List<String> ingredients, List<String> instruction, List<String> eating,
                      List<String> diet) {
        this.url = url;
        this.name = name;
        this.time = time;
        this.calories = calories;
        this.description = description;
        this.percentCarbohydrates = percentCarbohydrates;
        this.percentProteins = percentProteins;
        this.percentFats = percentFats;
        this.carbohydrates = carbohydrates;
        this.proteins = proteins;
        this.fats = fats;
        this.cellulose = cellulose;
        this.sugar = sugar;
        this.saturatedFats = saturatedFats;
        this.unSaturatedFats = unSaturatedFats;
        this.cholesterol = cholesterol;
        this.sodium = sodium;
        this.potassium = potassium;
        this.portions = portions;
        this.ingredients = ingredients;
        this.instruction = instruction;
        this.eating = eating;
        this.diet = diet;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPercentCarbohydrates() {
        return percentCarbohydrates;
    }

    public void setPercentCarbohydrates(double percentCarbohydrates) {
        this.percentCarbohydrates = percentCarbohydrates;
    }

    public double getPercentProteins() {
        return percentProteins;
    }

    public void setPercentProteins(double percentProteins) {
        this.percentProteins = percentProteins;
    }

    public double getPercentFats() {
        return percentFats;
    }

    public void setPercentFats(double percentFats) {
        this.percentFats = percentFats;
    }

    public double getCarbohydrates() {
        return carbohydrates;
    }

    public void setCarbohydrates(double carbohydrates) {
        this.carbohydrates = carbohydrates;
    }

    public double getProteins() {
        return proteins;
    }

    public void setProteins(double proteins) {
        this.proteins = proteins;
    }

    public double getFats() {
        return fats;
    }

    public void setFats(double fats) {
        this.fats = fats;
    }

    public double getCellulose() {
        return cellulose;
    }

    public void setCellulose(double cellulose) {
        this.cellulose = cellulose;
    }

    public double getSugar() {
        return sugar;
    }

    public void setSugar(double sugar) {
        this.sugar = sugar;
    }

    public double getSaturatedFats() {
        return saturatedFats;
    }

    public void setSaturatedFats(double saturatedFats) {
        this.saturatedFats = saturatedFats;
    }

    public double getUnSaturatedFats() {
        return unSaturatedFats;
    }

    public void setUnSaturatedFats(double unSaturatedFats) {
        this.unSaturatedFats = unSaturatedFats;
    }

    public double getCholesterol() {
        return cholesterol;
    }

    public void setCholesterol(double cholesterol) {
        this.cholesterol = cholesterol;
    }

    public double getSodium() {
        return sodium;
    }

    public void setSodium(double sodium) {
        this.sodium = sodium;
    }

    public double getPotassium() {
        return potassium;
    }

    public void setPotassium(double potassium) {
        this.potassium = potassium;
    }

    public int getPortions() {
        return portions;
    }

    public void setPortions(int portions) {
        this.portions = portions;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public List<String> getInstruction() {
        return instruction;
    }

    public void setInstruction(List<String> instruction) {
        this.instruction = instruction;
    }

    public List<String> getEating() {
        return eating;
    }

    public void setEating(List<String> eating) {
        this.eating = eating;
    }

    public List<String> getDiet() {
        return diet;
    }

    public void setDiet(List<String> diet) {
        this.diet = diet;
    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}

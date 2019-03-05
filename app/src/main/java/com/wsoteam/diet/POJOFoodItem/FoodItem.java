package com.wsoteam.diet.POJOFoodItem;

import com.orm.SugarRecord;
import com.squareup.moshi.Json;

import java.io.Serializable;

public class FoodItem implements Serializable {

    private String calories;
    private String carbohydrates;
    private String composition;
    private String description;
    private String fat;
    private String name;
    private String properties;
    private String protein;
    private String urlOfImages;
    private String nameOfGroup;
    private int countOfItemsInGroup;

    public FoodItem() {
    }

    public FoodItem(String calories, String carbohydrates, String composition, String description, String fat, String name, String properties, String protein, String urlOfImages, String nameOfGroup, int countOfItemsInGroup) {
        this.calories = calories;
        this.carbohydrates = carbohydrates;
        this.composition = composition;
        this.description = description;
        this.fat = fat;
        this.name = name;
        this.properties = properties;
        this.protein = protein;
        this.urlOfImages = urlOfImages;
        this.nameOfGroup = nameOfGroup;
        this.countOfItemsInGroup = countOfItemsInGroup;
    }

    public int getCountOfItemsInGroup() {
        return countOfItemsInGroup;
    }

    public void setCountOfItemsInGroup(int countOfItemsInGroup) {
        this.countOfItemsInGroup = countOfItemsInGroup;
    }

    public String getCalories() {
        return calories;
    }

    public void setCalories(String calories) {
        this.calories = calories;
    }

    public String getCarbohydrates() {
        return carbohydrates;
    }

    public void setCarbohydrates(String carbohydrates) {
        this.carbohydrates = carbohydrates;
    }

    public String getComposition() {
        return composition;
    }

    public void setComposition(String composition) {
        this.composition = composition;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFat() {
        return fat;
    }

    public void setFat(String fat) {
        this.fat = fat;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProperties() {
        return properties;
    }

    public void setProperties(String properties) {
        this.properties = properties;
    }

    public String getProtein() {
        return protein;
    }

    public void setProtein(String protein) {
        this.protein = protein;
    }

    public String getUrlOfImages() {
        return urlOfImages;
    }

    public void setUrlOfImages(String urlOfImages) {
        this.urlOfImages = urlOfImages;
    }

    public String getNameOfGroup() {
        return nameOfGroup;
    }

    public void setNameOfGroup(String nameOfGroup) {
        this.nameOfGroup = nameOfGroup;
    }
}

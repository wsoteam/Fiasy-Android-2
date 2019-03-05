
package com.wsoteam.diet.POJOFoodItem;

import com.orm.SugarRecord;
import com.squareup.moshi.Json;

import java.io.Serializable;

public class ListOfFoodItem extends SugarRecord implements Serializable {

    @Json(name = "calories")
    private String calories;
    @Json(name = "carbohydrates")
    private String carbohydrates;
    @Json(name = "composition")
    private String composition;
    @Json(name = "description")
    private String description;
    @Json(name = "fat")
    private String fat;
    @Json(name = "name")
    private String name;
    @Json(name = "properties")
    private String properties;
    @Json(name = "protein")
    private String protein;
    @Json(name = "url_of_images")
    private String urlOfImages;

    /**
     * No args constructor for use in serialization
     * 
     */
    public ListOfFoodItem() {
    }

    /**
     * 
     * @param protein
     * @param description
     * @param name
     * @param fat
     * @param composition
     * @param carbohydrates
     * @param calories
     * @param properties
     * @param urlOfImages
     */
    public ListOfFoodItem(String calories, String carbohydrates, String composition, String description, String fat, String name, String properties, String protein, String urlOfImages) {
        super();
        this.calories = calories;
        this.carbohydrates = carbohydrates;
        this.composition = composition;
        this.description = description;
        this.fat = fat;
        this.name = name;
        this.properties = properties;
        this.protein = protein;
        this.urlOfImages = urlOfImages;
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

}

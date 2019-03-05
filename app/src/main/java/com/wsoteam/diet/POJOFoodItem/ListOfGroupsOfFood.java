
package com.wsoteam.diet.POJOFoodItem;

import java.util.List;
import com.squareup.moshi.Json;

public class ListOfGroupsOfFood {

    @Json(name = "listOfFoodItems")
    private List<ListOfFoodItem> listOfFoodItems = null;
    @Json(name = "name")
    private String name;
    @Json(name = "url_of_image")
    private String urlOfImage;

    /**
     * No args constructor for use in serialization
     * 
     */
    public ListOfGroupsOfFood() {
    }

    /**
     * 
     * @param urlOfImage
     * @param name
     * @param listOfFoodItems
     */
    public ListOfGroupsOfFood(List<ListOfFoodItem> listOfFoodItems, String name, String urlOfImage) {
        super();
        this.listOfFoodItems = listOfFoodItems;
        this.name = name;
        this.urlOfImage = urlOfImage;
    }

    public List<ListOfFoodItem> getListOfFoodItems() {
        return listOfFoodItems;
    }

    public void setListOfFoodItems(List<ListOfFoodItem> listOfFoodItems) {
        this.listOfFoodItems = listOfFoodItems;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrlOfImage() {
        return urlOfImage;
    }

    public void setUrlOfImage(String urlOfImage) {
        this.urlOfImage = urlOfImage;
    }

}

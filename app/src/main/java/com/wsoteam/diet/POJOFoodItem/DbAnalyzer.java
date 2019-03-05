
package com.wsoteam.diet.POJOFoodItem;

import java.util.List;
import com.squareup.moshi.Json;

public class DbAnalyzer {

    @Json(name = "listOfGroupsOfFood")
    private List<ListOfGroupsOfFood> listOfGroupsOfFood = null;
    @Json(name = "name")
    private String name;

    /**
     * No args constructor for use in serialization
     * 
     */
    public DbAnalyzer() {
    }

    /**
     * 
     * @param listOfGroupsOfFood
     * @param name
     */
    public DbAnalyzer(List<ListOfGroupsOfFood> listOfGroupsOfFood, String name) {
        super();
        this.listOfGroupsOfFood = listOfGroupsOfFood;
        this.name = name;
    }

    public List<ListOfGroupsOfFood> getListOfGroupsOfFood() {
        return listOfGroupsOfFood;
    }

    public void setListOfGroupsOfFood(List<ListOfGroupsOfFood> listOfGroupsOfFood) {
        this.listOfGroupsOfFood = listOfGroupsOfFood;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}

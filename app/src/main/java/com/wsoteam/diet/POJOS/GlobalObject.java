package com.wsoteam.diet.POJOS;

import java.io.Serializable;

public class GlobalObject implements Serializable {
    private String name;
    private ListOfGroupsFood listOfGroupsFood;
    private ListOfPOJO listOfPOJO;
    private DescriptionOfDiet descriptionOfDiet;
    private ListOfGroupsRecipes listOfGroupsRecipes;

    public GlobalObject() {
    }

    public GlobalObject(String name, ListOfGroupsFood listOfGroupsFood, ListOfPOJO listOfPOJO, DescriptionOfDiet descriptionOfDiet, ListOfGroupsRecipes listOfGroupsRecipes) {
        this.name = name;
        this.listOfGroupsFood = listOfGroupsFood;
        this.listOfPOJO = listOfPOJO;
        this.descriptionOfDiet = descriptionOfDiet;
        this.listOfGroupsRecipes = listOfGroupsRecipes;
    }

    public ListOfGroupsRecipes getListOfGroupsRecipes() {
        return listOfGroupsRecipes;
    }

    public void setListOfGroupsRecipes(ListOfGroupsRecipes listOfGroupsRecipes) {
        this.listOfGroupsRecipes = listOfGroupsRecipes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ListOfGroupsFood getListOfGroupsFood() {
        return listOfGroupsFood;
    }

    public void setListOfGroupsFood(ListOfGroupsFood listOfGroupsFood) {
        this.listOfGroupsFood = listOfGroupsFood;
    }

    public ListOfPOJO getListOfPOJO() {
        return listOfPOJO;
    }

    public void setListOfPOJO(ListOfPOJO listOfPOJO) {
        this.listOfPOJO = listOfPOJO;
    }

    public DescriptionOfDiet getDescriptionOfDiet() {
        return descriptionOfDiet;
    }

    public void setDescriptionOfDiet(DescriptionOfDiet descriptionOfDiet) {
        this.descriptionOfDiet = descriptionOfDiet;
    }
}

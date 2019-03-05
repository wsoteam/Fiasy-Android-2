package com.wsoteam.diet.POJOS;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ListOfGroupsRecipes implements Serializable {

    private String name;
    private List<ListOfRecipes> listOfGroupsRecipes;

    public ListOfGroupsRecipes() {
    }

    public ListOfGroupsRecipes(String name, List<ListOfRecipes> listOfGroupsRecipes) {
        this.name = name;
        this.listOfGroupsRecipes = listOfGroupsRecipes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ListOfRecipes> getListOfGroupsRecipes() {
        return listOfGroupsRecipes;
    }

    public void setListOfGroupsRecipes(List<ListOfRecipes> listOfGroupsRecipes) {
        this.listOfGroupsRecipes = listOfGroupsRecipes;
    }
}

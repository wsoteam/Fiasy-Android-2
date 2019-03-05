package com.wsoteam.diet.POJOS;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ListOfRecipes implements Serializable {

    private String name;
    private String urlGroup;
    private List<ItemRecipes> listRecipes;

    public ListOfRecipes() {
    }

    public ListOfRecipes(String name, String urlGroup, List<ItemRecipes> listRecipes) {
        this.name = name;
        this.urlGroup = urlGroup;
        this.listRecipes = listRecipes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrlGroup() {
        return urlGroup;
    }

    public void setUrlGroup(String urlGroup) {
        this.urlGroup = urlGroup;
    }

    public List<ItemRecipes> getListRecipes() {
        return listRecipes;
    }

    public void setListRecipes(List<ItemRecipes> listRecipes) {
        this.listRecipes = listRecipes;
    }
}

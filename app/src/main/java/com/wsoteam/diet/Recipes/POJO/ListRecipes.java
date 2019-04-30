package com.wsoteam.diet.Recipes.POJO;

import java.io.Serializable;
import java.util.List;

public class ListRecipes implements Serializable {
    private String name;
    private List<RecipeItem> listrecipes;

    public ListRecipes() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<RecipeItem> getListrecipes() {
        return listrecipes;
    }

    public void setListrecipes(List<RecipeItem> listrecipes) {
        this.listrecipes = listrecipes;
    }
}

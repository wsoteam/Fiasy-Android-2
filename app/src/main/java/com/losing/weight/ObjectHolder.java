package com.losing.weight;

import com.losing.weight.POJOS.ListOfGroupsRecipes;

public class ObjectHolder {
    private static ListOfGroupsRecipes listOfGroupsRecipes;

    public void bindObjectWithHolder(ListOfGroupsRecipes listOfGroupsRecipes){
        this.listOfGroupsRecipes = listOfGroupsRecipes;
    }

    public static ListOfGroupsRecipes getListOfGroupsRecipes(){
        return listOfGroupsRecipes;
    }
}

package com.wsoteam.diet;

import com.wsoteam.diet.POJOS.GlobalObject;
import com.wsoteam.diet.POJOS.ListOfGroupsRecipes;

public class ObjectHolder {
    private static ListOfGroupsRecipes listOfGroupsRecipes;

    public void bindObjectWithHolder(ListOfGroupsRecipes listOfGroupsRecipes){
        this.listOfGroupsRecipes = listOfGroupsRecipes;
    }

    public static ListOfGroupsRecipes getListOfGroupsRecipes(){
        return listOfGroupsRecipes;
    }
}

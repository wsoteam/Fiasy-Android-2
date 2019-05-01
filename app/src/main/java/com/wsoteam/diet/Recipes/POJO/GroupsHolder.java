package com.wsoteam.diet.Recipes.POJO;

public class GroupsHolder {
    private static GroupsRecipes groupsRecipes;

    public void bind(GroupsRecipes groupsRecipes){
        this.groupsRecipes = groupsRecipes;
    }

    public static GroupsRecipes getGroupsRecipes(){
        return groupsRecipes;
    }
}

package com.wsoteam.diet.Recipes.POJO;

import java.util.LinkedHashSet;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;


public class GroupsHolder {

    private static GroupsRecipes groupsRecipes;
    private static Set<Observer> observersList = new LinkedHashSet<>();

    public GroupsHolder() {

    }

    public static void subscribe(Observer observer) {
        observersList.add(observer);
    }

    public static void unsubscribe(Observer observer) {
        observersList.remove(observer);
    }

    public static GroupsRecipes getGroupsRecipes() {
        return groupsRecipes;
    }

    private void update() {

        for (Observer observer :
                observersList) {
            observer.update(new Observable(), null);
        }
    }

    public void bind(GroupsRecipes groupsRecipes) {
        this.groupsRecipes = groupsRecipes;
        update();

    }
}

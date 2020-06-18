package com.losing.weight.Recipes.POJO;

import java.util.LinkedHashSet;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

public class RecipesHolder {

    private static ListRecipes listRecipes;
    private static Set<Observer> observersList = new LinkedHashSet<>();

    public static void bind(ListRecipes listRecipes){
        RecipesHolder.listRecipes = listRecipes;
        update();
    }

    public static ListRecipes get(){
        return listRecipes;
    }

    public static void subscribe(Observer observer) {
        observersList.add(observer);
    }

    public static void unsubscribe(Observer observer) {
        observersList.remove(observer);
    }

    private static void update() {

        for (Observer observer :
                observersList) {
            observer.update(new Observable(), null);
        }
    }
}

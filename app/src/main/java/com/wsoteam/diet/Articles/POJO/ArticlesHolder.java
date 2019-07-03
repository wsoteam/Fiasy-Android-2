package com.wsoteam.diet.Articles.POJO;

import java.util.LinkedHashSet;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

public class ArticlesHolder {
    private static ListArticles listArticles;
    private static Set<Observer> observersList = new LinkedHashSet<>();

    public void bind (ListArticles listArticles){
        this.listArticles = listArticles;
        update();
    }

    public static ListArticles getListArticles(){
        return listArticles;
    }

    public static void subscribe(Observer observer) {
        observersList.add(observer);
    }

    public static void unsubscribe(Observer observer) {
        observersList.remove(observer);
    }

    private void update() {

        for (Observer observer :
                observersList) {
            observer.update(new Observable(), null);
        }
    }
}

package com.wsoteam.diet.POJOSExercises;

import java.io.Serializable;
import java.util.List;

public class GlobalObject implements Serializable {
    private String name;
    private List<Programm> programmList;
    private AllPartOfBody exercises;
    private AllWholeArticles wholeArticles;

    public GlobalObject() {
    }


    public GlobalObject(String name, List<Programm> programmList, AllPartOfBody exercises, AllWholeArticles wholeArticles) {
        this.name = name;
        this.programmList = programmList;
        this.exercises = exercises;
        this.wholeArticles = wholeArticles;
    }

    public AllWholeArticles getWholeArticles() {
        return wholeArticles;
    }

    public void setWholeArticles(AllWholeArticles wholeArticles) {
        this.wholeArticles = wholeArticles;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Programm> getProgrammList() {
        return programmList;
    }

    public void setProgrammList(List<Programm> programmList) {
        this.programmList = programmList;
    }

    public AllPartOfBody getExercises() {
        return exercises;
    }

    public void setExercises(AllPartOfBody exercises) {
        this.exercises = exercises;
    }


}

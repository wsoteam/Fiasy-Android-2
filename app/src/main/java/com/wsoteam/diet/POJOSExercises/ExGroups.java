package com.wsoteam.diet.POJOSExercises;

import java.io.Serializable;
import java.util.List;

public class ExGroups implements Serializable {
    private String name;
    private List<Ex> exList;

    public ExGroups() {
    }

    public ExGroups(String name, List<Ex> exList) {
        this.name = name;
        this.exList = exList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Ex> getExList() {
        return exList;
    }

    public void setExList(List<Ex> exList) {
        this.exList = exList;
    }
}

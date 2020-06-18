package com.losing.weight.DietPlans.POJO;

import java.io.Serializable;
import java.util.List;

public class DietModule implements Serializable {
    private String name;
    private List<DietsList> listGroups;

    public DietModule() {
    }

    public DietModule(String name, List<DietsList> listGroups) {
        this.name = name;
        this.listGroups = listGroups;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<DietsList> getListGroups() {
        return listGroups;
    }

    public void setListGroups(List<DietsList> listGroups) {
        this.listGroups = listGroups;
    }
}

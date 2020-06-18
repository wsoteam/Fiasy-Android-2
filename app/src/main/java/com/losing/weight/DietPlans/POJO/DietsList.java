package com.losing.weight.DietPlans.POJO;

import java.io.Serializable;
import java.util.List;

public class DietsList implements Serializable {
    private String name;
    private String properties;
    private List<DietPlan> dietPlans;

    public DietsList() {
    }

    public DietsList(String name, String properties, List<DietPlan> dietPlans) {
        this.name = name;
        this.properties = properties;
        this.dietPlans = dietPlans;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<DietPlan> getDietPlans() {
        return dietPlans;
    }

    public void setDietPlans(List<DietPlan> dietPlans) {
        this.dietPlans = dietPlans;
    }

    public String getProperties() {
        return properties;
    }

    public void setProperties(String properties) {
        this.properties = properties;
    }
}

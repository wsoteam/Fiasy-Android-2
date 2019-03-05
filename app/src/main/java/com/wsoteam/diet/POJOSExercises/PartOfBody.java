package com.wsoteam.diet.POJOSExercises;

import java.io.Serializable;
import java.util.List;

public class PartOfBody implements Serializable {
    private String name;
    private String url_of_image;
    private List<ExGroups> exGroupsList;

    public PartOfBody() {
    }

    public PartOfBody(String name, String url_of_image, List<ExGroups> exGroupsList) {
        this.name = name;
        this.url_of_image = url_of_image;
        this.exGroupsList = exGroupsList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl_of_image() {
        return url_of_image;
    }

    public void setUrl_of_image(String url_of_image) {
        this.url_of_image = url_of_image;
    }

    public List<ExGroups> getExGroupsList() {
        return exGroupsList;
    }

    public void setExGroupsList(List<ExGroups> exGroupsList) {
        this.exGroupsList = exGroupsList;
    }
}

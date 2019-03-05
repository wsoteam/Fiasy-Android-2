package com.wsoteam.diet.POJOSExercises;

import java.io.Serializable;
import java.util.List;

public class AllPartOfBody implements Serializable {
    private String name;
    private String url_of_image;
    private List<PartOfBody> partOfBodyList;

    public AllPartOfBody() {
    }

    public AllPartOfBody(String name, String url_of_image, List<PartOfBody> partOfBodyList) {
        this.name = name;
        this.url_of_image = url_of_image;
        this.partOfBodyList = partOfBodyList;
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

    public List<PartOfBody> getPartOfBodyList() {
        return partOfBodyList;
    }

    public void setPartOfBodyList(List<PartOfBody> partOfBodyList) {
        this.partOfBodyList = partOfBodyList;
    }
}

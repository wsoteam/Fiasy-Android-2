package com.wsoteam.diet.POJOSExercises;

import java.io.Serializable;
import java.util.List;

public class Programm implements Serializable {
    private String title;
    private List<Training> trainingList;
    private String img_url;

    public Programm() {
    }

    public Programm(String title, List<Training> trainingList, String img_url) {
        this.title = title;
        this.trainingList = trainingList;
        this.img_url = img_url;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Training> getTrainingList() {
        return trainingList;
    }

    public void setTrainingList(List<Training> trainingList) {
        this.trainingList = trainingList;
    }
}

package com.wsoteam.diet.POJOSExercises;

import java.io.Serializable;
import java.util.List;

public class Training implements Serializable {
    private String title;
    private String text;
    private String url_of_image;
    private List<ArrayOfTiles> tilesList;

    public Training() {
    }

    public Training(String title, String text, String url_of_image, List<ArrayOfTiles> tilesList) {
        this.title = title;
        this.text = text;
        this.url_of_image = url_of_image;
        this.tilesList = tilesList;
    }

    public String getUrl_of_image() {
        return url_of_image;
    }

    public void setUrl_of_image(String url_of_image) {
        this.url_of_image = url_of_image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<ArrayOfTiles> getTilesList() {
        return tilesList;
    }

    public void setTilesList(List<ArrayOfTiles> tilesList) {
        this.tilesList = tilesList;
    }
}

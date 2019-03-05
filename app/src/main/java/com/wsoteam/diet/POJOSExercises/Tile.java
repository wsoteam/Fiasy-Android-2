package com.wsoteam.diet.POJOSExercises;

import java.io.Serializable;

public class Tile implements Serializable {
    private String title;
    private String url_of_image;

    public Tile() {
    }

    public Tile(String title, String url_of_image) {
        this.title = title;
        this.url_of_image = url_of_image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl_of_image() {
        return url_of_image;
    }

    public void setUrl_of_image(String url_of_image) {
        this.url_of_image = url_of_image;
    }
}

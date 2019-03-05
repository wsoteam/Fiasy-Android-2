package com.wsoteam.diet.POJOSExercises;

import java.io.Serializable;

public class Article implements Serializable {
    private String text;
    private String img_url;

    public Article(String text, String img_url) {
        this.text = text;
        this.img_url = img_url;
    }

    public Article() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }
}

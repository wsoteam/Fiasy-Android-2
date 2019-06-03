package com.wsoteam.diet.Articles.POJO;

import java.io.Serializable;
import java.util.List;

public class ListArticles implements Serializable {
    private String name;
    private List<Article> listArticles;

    public ListArticles() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Article> getListArticles() {
        return listArticles;
    }

    public void setListArticles(List<Article> listArticles) {
        this.listArticles = listArticles;
    }
}

package com.losing.weight.articles.POJO;

import java.io.Serializable;
import java.util.List;

import com.losing.weight.model.Article;

public class ListArticles implements Serializable {
    private String name;
    private List<Article> listArticles;


    public ListArticles(String name) {
        this.name = name;
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

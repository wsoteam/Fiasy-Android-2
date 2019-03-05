package com.wsoteam.diet.POJOSExercises;

import java.io.Serializable;
import java.util.List;

public class AllWholeArticles implements Serializable {
    private String name;
    private String img_url;
    private List<WholeArticle> wholeArticleList;

    public AllWholeArticles() {
    }

    public AllWholeArticles(String name, String img_url, List<WholeArticle> wholeArticleList) {
        this.name = name;
        this.img_url = img_url;
        this.wholeArticleList = wholeArticleList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public List<WholeArticle> getWholeArticleList() {
        return wholeArticleList;
    }

    public void setWholeArticleList(List<WholeArticle> wholeArticleList) {
        this.wholeArticleList = wholeArticleList;
    }
}

package com.wsoteam.diet.POJOSExercises;

import java.io.Serializable;
import java.util.List;

public class WholeArticle implements Serializable {
    private String title;
    private String img_url;
    private List<Article> articleList;

    public WholeArticle(String title, String img_url, List<Article> articleList) {
        this.title = title;
        this.img_url = img_url;
        this.articleList = articleList;
    }

    public WholeArticle() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public List<Article> getArticleList() {
        return articleList;
    }

    public void setArticleList(List<Article> articleList) {
        this.articleList = articleList;
    }
}

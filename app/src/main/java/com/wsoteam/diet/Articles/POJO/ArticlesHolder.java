package com.wsoteam.diet.Articles.POJO;

public class ArticlesHolder {
    private static ListArticles listArticles;

    public void bind (ListArticles listArticles){
        this.listArticles = listArticles;
    }

    public static ListArticles getListArticles(){
        return listArticles;
    }
}

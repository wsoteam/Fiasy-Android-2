package com.wsoteam.diet.articles.POJO;

import android.util.Log;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.wsoteam.diet.model.Article;

public class SectionArticles implements GroupsArticles {

  List<ListArticles> sectionArticles;

  public SectionArticles(List<Article> articles) {

    sectionArticles = new ArrayList<>();

    ListArticles nutrition = new ListArticles("Питание");
    ListArticles training = new ListArticles("Тренировки");
    ListArticles authors = new ListArticles("От автора");

    List<Article> listNutrition = new ArrayList<>();
    List<Article> listTraining = new ArrayList<>();
    List<Article> listAuthors = new ArrayList<>();

    nutrition.setListArticles(listNutrition);
    training.setListArticles(listTraining);
    authors.setListArticles(listAuthors);

    sectionArticles.add(authors);
    sectionArticles.add(nutrition);
    sectionArticles.add(training);


    for (Article article :articles) {

      if (article.getCategory() != null)
        switch (article.getCategory().getId()) {
          case 2:
            //Log.d("xxx", "SectionArticles: 2 - " + article.getTitle());
            listTraining.add(article);
            break;
          case 3:
            //Log.d("xxx", "SectionArticles: 3 - " + article.getTitle());
            listNutrition.add(article);
            break;
          case 4:
            //Log.d("xxx", "SectionArticles: 3 - " + article.getTitle());
            listAuthors.add(article);
          default:{
            //Log.d("xxx", "SectionArticles: def - " + article.getTitle());
          }
        }
    }

    ArticleSeriesSort sort = new ArticleSeriesSort();
    try {
      Collections.sort(listAuthors, sort);
    } catch (Exception e){
      e.printStackTrace();
    }
  }

  @Override public List<ListArticles> getGroups() {
    return sectionArticles;
  }
}

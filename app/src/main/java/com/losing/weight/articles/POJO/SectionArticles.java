package com.losing.weight.articles.POJO;

import android.content.Context;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.losing.weight.R;
import com.losing.weight.model.Article;

public class SectionArticles implements GroupsArticles {

  private List<ListArticles> sectionArticles;

  public SectionArticles(List<Article> articles, Context context) {

    sectionArticles = new ArrayList<>();

    ListArticles nutrition = new ListArticles(context.getString(R.string.section_eating));
    ListArticles training = new ListArticles(context.getString(R.string.section_train));
    ListArticles authors = new ListArticles(null);

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

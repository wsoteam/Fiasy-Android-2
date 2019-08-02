package com.wsoteam.diet.Articles.POJO;

import java.util.ArrayList;
import java.util.List;

public class SectionArticles implements GroupsArticles {

  List<ListArticles> sectionArticles;

  public SectionArticles(List<Article> articles) {

    sectionArticles = new ArrayList<>();

    ListArticles nutrition = new ListArticles("Питание");
    ListArticles training = new ListArticles("Тренировки");

    List<Article> listNutrition = new ArrayList<>();
    List<Article> listTraining = new ArrayList<>();

    nutrition.setListArticles(listNutrition);
    training.setListArticles(listTraining);

    sectionArticles.add(nutrition);
    sectionArticles.add(training);

    for (Article article :
        ArticlesHolder.getListArticles().getListArticles()) {
      switch (article.getSection()) {
        case "nutrition":
          listNutrition.add(article);
          break;
        case "training":
          listTraining.add(article);
          break;
        default:
          listNutrition.add(article);
          break;
      }
    }
  }

  @Override public List<ListArticles> getGroups() {
    return sectionArticles;
  }
}

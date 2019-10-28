package com.wsoteam.diet.Articles.POJO;

import android.content.Context;
import com.wsoteam.diet.R;
import java.util.ArrayList;
import java.util.List;

public class SectionArticles implements GroupsArticles {

  List<ListArticles> sectionArticles;

  public SectionArticles(List<Article> articles, Context context) {

    sectionArticles = new ArrayList<>();

    ListArticles nutrition = new ListArticles(context.getString(R.string.section_eating));
    ListArticles training = new ListArticles(context.getString(R.string.section_train));

    List<Article> listNutrition = new ArrayList<>();
    List<Article> listTraining = new ArrayList<>();

    nutrition.setListArticles(listNutrition);
    training.setListArticles(listTraining);

    sectionArticles.add(nutrition);
    sectionArticles.add(training);

    for (Article article : articles) {
      switch (article.getSection()) {
        case "nutrition":
          listNutrition.add(article);
          break;
        case "training":
          listTraining.add(article);
          break;
      }
    }
  }

  @Override public List<ListArticles> getGroups() {
    return sectionArticles;
  }
}

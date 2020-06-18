package com.losing.weight.articles.POJO;

import com.losing.weight.model.Article;
import java.util.Comparator;

public class ArticleSeriesSort implements Comparator<Article> {
  @Override
  public int compare(Article a1, Article a2) {
    return Integer.compare(a1.getDay_in_series(), a2.getDay_in_series());
  }
  }
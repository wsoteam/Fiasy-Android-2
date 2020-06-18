package com.losing.weight.articles.recycler;

import android.view.View;
import com.losing.weight.model.Article;

public interface SeriesCallback {
  void Clicked(View v, Article article);
}

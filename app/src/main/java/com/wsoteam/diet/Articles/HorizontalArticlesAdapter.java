package com.wsoteam.diet.Articles;

import android.view.View;
import com.wsoteam.diet.Articles.POJO.Article;

public class HorizontalArticlesAdapter {

  public interface OnItemClickListener {
    void onItemClick(View view, int position, Article dietPlan);

    void onItemLongClick(View view, int position, Article dietPlan);
  }
}

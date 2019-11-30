package com.wsoteam.diet.articles.recycler;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import com.wsoteam.diet.model.Article;
import com.wsoteam.diet.articles.POJO.ListArticles;
import java.util.List;

public class HorizontalArticlesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  private OnItemClickListener mItemClickListener;
  private List<Article> articles;

  private final int MAX_ARTICLES = 3;
  private final int VH_DEFF = 0, VH_SPACE = 1;

  @NonNull @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
    switch (viewType) {
      case VH_SPACE: return new SpaceViewHolder(viewGroup);
      default: {
        return new ArticleViewHolder(viewGroup, mItemClickListener);
      }
    }
  }

  @Override public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

    if (position == 0 || position == getItemCount() - 1) return;

    switch (viewHolder.getItemViewType()) {
      default: {
        ArticleViewHolder articleViewHolder = (ArticleViewHolder) viewHolder;
        articleViewHolder.bind(articles.get(position - 1));
        break;
      }
    }
  }

  @Override public int getItemCount() {
    if (articles == null) return 0;
    else return (articles.size() >= MAX_ARTICLES ? MAX_ARTICLES : articles.size()) + 2;
  }

  @Override
  public int getItemViewType(int position) {
   if (position == 0 || position == getItemCount() - 1) return VH_SPACE;
   else return VH_DEFF;

  }

  public void updateList(List<Article> articles) {

    // выводим бесплатную статью в начало списка
    for (int i = 0; i < articles.size(); i++){
      if(articles.get(0) != null && !articles.get(0).isPremium()) break;

      if (!articles.get(i).isPremium()) {
        articles.add(0, articles.get(i));
        articles.remove(i + 1);
        break;
      }
    }
    this.articles = articles;
    notifyDataSetChanged();
  }

  // for both short and long click
  public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
    this.mItemClickListener = mItemClickListener;
  }

  public interface OnItemClickListener {
    void onItemClick(View view, int position, Article dietPlan);

    void onClickAll(View view, int position, ListArticles listArticles);

    void onClickBanner(View view);
  }
}

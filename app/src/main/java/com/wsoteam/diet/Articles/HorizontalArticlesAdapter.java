package com.wsoteam.diet.Articles;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.wsoteam.diet.Articles.POJO.Article;
import com.wsoteam.diet.R;
import java.util.List;

public class HorizontalArticlesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  private Context context;
  private OnItemClickListener mItemClickListener;
  private List<Article> articles;

  @NonNull @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
    context = viewGroup.getContext();
    switch (viewType) {
      default: {
        View v1 = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.article_view_holder, viewGroup, false);
        return new ArticleViewHolder(v1);
      }
    }
  }

  @Override public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
    switch (viewHolder.getItemViewType()) {
      default: {
        ArticleViewHolder articleViewHolder = (ArticleViewHolder) viewHolder;
        articleViewHolder.bind(articles.get(position));
        break;
      }
    }
  }

  @Override public int getItemCount() {
    if (articles == null) return 0;

    return articles.size();
  }

  public void updateList(List<Article> articles) {
    this.articles = articles;
    notifyDataSetChanged();
  }

  // for both short and long click
  public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
    this.mItemClickListener = mItemClickListener;
  }

  public interface OnItemClickListener {
    void onItemClick(View view, int position, Article dietPlan);

    void onItemLongClick(View view, int position, Article dietPlan);
  }
}

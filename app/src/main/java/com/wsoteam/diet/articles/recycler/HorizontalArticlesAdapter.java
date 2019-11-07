package com.wsoteam.diet.articles.recycler;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import com.wsoteam.diet.model.Article;
import com.wsoteam.diet.articles.POJO.ListArticles;
import java.util.List;

public class HorizontalArticlesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  private Context context;
  private OnItemClickListener mItemClickListener;
  private List<Article> articles;

  private final int VH_DEFF = 0, VH_SPACE = 1;

  @NonNull @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
    context = viewGroup.getContext();
    switch (viewType) {
      case VH_SPACE: return new SpaceViewHolder(viewGroup);
      default: {
        ArticleViewHolder viewHolder = new ArticleViewHolder(viewGroup);
        viewHolder.setOnClickListener((View v) -> {
            mItemClickListener.onItemClick(v, viewHolder.getAdapterPosition(), articles.get(viewHolder.getAdapterPosition()));
          });
        return viewHolder;
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

    return articles.size() + 2;
  }

  @Override
  public int getItemViewType(int position) {
   if (position == 0 || position == getItemCount() - 1) return VH_SPACE;
   else return VH_DEFF;

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

    void onClickAll(View view, int position, ListArticles listArticles);

    void onClickBanner(View view);
  }
}

package com.losing.weight.articles.recycler;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.losing.weight.R;
import com.losing.weight.Recipes.EmptyViewHolder;
import com.losing.weight.model.Article;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ListArticlesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  private List<Article> listItem;
  private Context context;
  private OnItemClickListener onItemClickListener;

  private final int EMPTY_VH = 0, DEFF_VH = 1;

  public ListArticlesAdapter(List<Article> listItem, OnItemClickListener onItemClickListener) {
    this.listItem = listItem;
    this.onItemClickListener = onItemClickListener;
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
    if (context == null) this.context = viewGroup.getContext();

    if (i == EMPTY_VH) {
      return new EmptyViewHolder(viewGroup);
    }
    ArticleViewHolder viewHolder = new ArticleViewHolder(viewGroup, R.layout.article_view_holder_2, onItemClickListener);
//        viewHolder.setOnClickListener(onItemClickListener);
    return viewHolder;
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
    if (position == 0) return;
    ((ArticleViewHolder) viewHolder).bind(listItem.get(position - 1));
  }

  @Override
  public int getItemCount() {
    if (listItem == null) return 1;

    return listItem.size() + 1;
  }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) return EMPTY_VH;
        else return DEFF_VH;
    }

    public void updateData(List<Article> listItem){
    this.listItem = listItem;
    notifyDataSetChanged();
  }

  public void setOnItemClickListener(OnItemClickListener onItemClickListener){
    this.onItemClickListener = onItemClickListener;
  }

  public interface OnItemClickListener {
    void onItemClick(View view, Article dietPlan);
  }
}

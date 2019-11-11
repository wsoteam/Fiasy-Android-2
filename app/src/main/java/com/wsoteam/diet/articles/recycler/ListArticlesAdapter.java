package com.wsoteam.diet.articles.recycler;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.wsoteam.diet.model.Article;
import com.wsoteam.diet.R;

import java.util.List;

public class ListArticlesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  private List<Article> listItem;
  private Context context;
  private OnItemClickListener onItemClickListener;

  public ListArticlesAdapter(List<Article> listItem, OnItemClickListener onItemClickListener) {
    this.listItem = listItem;
    this.onItemClickListener = onItemClickListener;
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
    this.context = viewGroup.getContext();

    ArticleViewHolder viewHolder = new ArticleViewHolder(viewGroup, R.layout.article_view_holder_2);

    viewHolder.setOnClickListener((View v) ->
      onItemClickListener.onItemClick(v, listItem.get(viewHolder.getAdapterPosition())));

    return viewHolder;
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

    ((ArticleViewHolder) viewHolder).bind(listItem.get(position));
  }

  @Override
  public int getItemCount() {
    if (listItem == null) return 0;

    return listItem.size();
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

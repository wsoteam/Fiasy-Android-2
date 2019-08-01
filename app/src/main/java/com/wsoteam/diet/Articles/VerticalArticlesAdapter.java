package com.wsoteam.diet.Articles;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseIntArray;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import com.wsoteam.diet.Articles.POJO.ListArticles;
import java.util.List;

public class VerticalArticlesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

  List<ListArticles> sectionsArticles;
  private SparseIntArray listPosition = new SparseIntArray();
  private HorizontalArticlesAdapter.OnItemClickListener mItemClickListener;
  private Context mContext;
  private RecyclerView.RecycledViewPool viewPool;

  public VerticalArticlesAdapter(
      List<ListArticles> sectionsArticles) {
    this.sectionsArticles = sectionsArticles;
    viewPool = new RecyclerView.RecycledViewPool();
  }

  class VerticalViewHolder extends RecyclerView.ViewHolder {
    public VerticalViewHolder(@NonNull View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }


  @NonNull @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
    return null;
  }

  @Override public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

  }

  @Override public int getItemCount() {
    return 0;
  }
}

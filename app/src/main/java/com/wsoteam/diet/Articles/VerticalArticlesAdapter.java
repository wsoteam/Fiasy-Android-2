package com.wsoteam.diet.Articles;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.wsoteam.diet.Articles.POJO.ListArticles;
import com.wsoteam.diet.R;
import java.util.List;

public class VerticalArticlesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

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

  @NonNull @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
    mContext = viewGroup.getContext();
    switch (viewType) {
      default: {
        View v0 = LayoutInflater.from(viewGroup.getContext())
            .inflate(R.layout.group_articles_item, viewGroup, false);
        return new VerticalViewHolder(v0);
      }
    }
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

    switch (viewHolder.getItemViewType()) {
      default: {
        VerticalViewHolder holder = (VerticalViewHolder) viewHolder;

        holder.setData(sectionsArticles.get(position));

        int lastSeenFirstPosition = listPosition.get(position, 0);
        if (lastSeenFirstPosition >= 0) {
          holder.layoutManager.scrollToPositionWithOffset(lastSeenFirstPosition, 0);
        }
        break;
      }
    }
  }

  @Override
  public void onViewRecycled(RecyclerView.ViewHolder viewHolder) {
    final int position = viewHolder.getAdapterPosition();
    VerticalViewHolder cellViewHolder = (VerticalViewHolder) viewHolder;
    int firstVisiblePosition = cellViewHolder.layoutManager.findFirstVisibleItemPosition();
    listPosition.put(position, firstVisiblePosition);

    super.onViewRecycled(viewHolder);
  }

  @Override public int getItemCount() {
    if (sectionsArticles == null) return 0;

    return sectionsArticles.size();
  }

  // for both short and long click
  public void SetOnItemClickListener(
      final HorizontalArticlesAdapter.OnItemClickListener mItemClickListener) {
    this.mItemClickListener = mItemClickListener;
  }

  class VerticalViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.tvGroupName) TextView tvGroupName;
    @BindView(R.id.recycler) RecyclerView recyclerView;
    private HorizontalArticlesAdapter adapter;
    private LinearLayoutManager layoutManager;

    public VerticalViewHolder(@NonNull View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);

      recyclerView.setRecycledViewPool(viewPool);
      recyclerView.setHasFixedSize(true);
      layoutManager = new LinearLayoutManager(mContext);
      layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
      recyclerView.setLayoutManager(layoutManager);

      adapter = new HorizontalArticlesAdapter();
      adapter.SetOnItemClickListener(mItemClickListener);
      recyclerView.setAdapter(adapter);
    }

    @OnClick({ R.id.llShowAll })
    void onClicked(View view) {
      mItemClickListener.onClickAll(view, getAdapterPosition(), sectionsArticles.get(getAdapterPosition()));
    }

    public void setData(ListArticles listArticles) {
      tvGroupName.setText(listArticles.getName());
      adapter.updateList(listArticles.getListArticles());
    }
  }
}

package com.wsoteam.diet.presentation.search.results.controllers.expandable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.wsoteam.diet.R;

public class ExpandableVH extends RecyclerView.ViewHolder {
  @BindView(R.id.tvTitle) TextView tvTitle;
  @BindView(R.id.tvKcal) TextView tvKcal;
  @BindView(R.id.ivSelect) ImageView ivSelect;
  public ExpandableVH(@NonNull LayoutInflater layoutInflater, ViewGroup parent) {
    super(layoutInflater.inflate(R.layout.item_search_result_exp, parent, false));
    ButterKnife.bind(this, itemView);
  }
}

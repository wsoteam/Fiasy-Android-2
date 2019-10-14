package com.wsoteam.diet.presentation.search.results.controllers;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.wsoteam.diet.R;
import com.wsoteam.diet.common.networking.food.HeaderObj;
import com.wsoteam.diet.common.networking.food.POJO.Result;

public class HeaderViewHolder extends RecyclerView.ViewHolder {
  @BindView(R.id.tvTitle) TextView tvTitle;

  public HeaderViewHolder(@NonNull LayoutInflater layoutInflater, ViewGroup parent) {
    super(layoutInflater.inflate(R.layout.item_search_header, parent, false));
    ButterKnife.bind(this, itemView);
  }

  public void bind(HeaderObj header) {
    tvTitle.setText(header.getTitle());
  }
}

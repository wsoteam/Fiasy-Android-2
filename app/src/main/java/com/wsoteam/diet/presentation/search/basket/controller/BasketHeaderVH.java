package com.wsoteam.diet.presentation.search.basket.controller;

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
import com.wsoteam.diet.common.networking.food.HeaderObj;

public class BasketHeaderVH extends RecyclerView.ViewHolder {
  @BindView(R.id.tvTitle) TextView tvTitle;
  @BindView(R.id.ivLastFood) ImageView ivLastFood;

  public BasketHeaderVH(@NonNull LayoutInflater layoutInflater, ViewGroup parent) {
    super(layoutInflater.inflate(R.layout.item_search_header, parent, false));
    ButterKnife.bind(this, itemView);
  }

  public void bind(HeaderObj header) {
    tvTitle.setText(header.getTitle());
    if (header.isNeedIcon() && ivLastFood.getVisibility() == View.GONE) {
      ivLastFood.setVisibility(View.VISIBLE);
    }
  }
}

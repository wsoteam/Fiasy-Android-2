package com.wsoteam.diet.presentation.search.basket.controller;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.wsoteam.diet.R;
import com.wsoteam.diet.presentation.search.basket.db.BasketEntity;

public class BasketItemVH extends RecyclerView.ViewHolder {
  @BindView(R.id.tvTitle) TextView tvTitle;
  @BindView(R.id.tvPortion) TextView tvPortion;
  @BindView(R.id.tvKcal) TextView tvKcal;
  @BindView(R.id.tbSelect) ToggleButton tbSelect;

  public BasketItemVH(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup) {
    super(layoutInflater.inflate(R.layout.item_search_result, viewGroup, false));
    ButterKnife.bind(this, itemView);
  }

  public void bind(BasketEntity food) {
    tvTitle.setText(food.getName());
    tvKcal.setText(String.valueOf(Math.round(food.getCalories() * 100)) + " Ккал");
    if (food.getBrand() != null && !food.getBrand().equals("")) {
      tvTitle.append(" (" + food.getBrand() + ")");
    }
  }
}

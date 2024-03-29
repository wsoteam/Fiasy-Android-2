package com.losing.weight.presentation.search.basket.controller;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

import com.losing.weight.R;
import com.losing.weight.presentation.search.basket.db.BasketEntity;
import com.losing.weight.presentation.search.results.controllers.ClickListener;

public class BasketItemVH extends RecyclerView.ViewHolder implements View.OnClickListener {
  @BindView(R.id.tvTitle) TextView tvTitle;
  @BindView(R.id.tvPortion) TextView tvPortion;
  @BindView(R.id.tvKcal) TextView tvKcal;
  @BindView(R.id.tbSelect) ToggleButton tbSelect;
  private ClickListener listener;

  public BasketItemVH(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup) {
    super(layoutInflater.inflate(R.layout.item_search_result, viewGroup, false));
    ButterKnife.bind(this, itemView);
    itemView.setOnClickListener(this);
  }

  @Override public void onClick(View view) {
    listener.open(getAdapterPosition());
  }

  public void bind(BasketEntity food, ClickListener listener) {
    this.listener = listener;
    tvTitle.setText(food.getName());
    tbSelect.setChecked(true);
    if (food.getBrand() != null && !food.getBrand().equals("")) {
      tvTitle.append(" (" + food.getBrand() + ")");
    }
    tvKcal.setText(String.valueOf(
        Math.round(food.getCalories()))
        + " "
        + itemView.getResources().getString(R.string.tvKkal));
    if (food.isLiquid()) {
      tvPortion.setText(itemView.getResources()
          .getString(R.string.srch_liquid, food.getCountPortions() * food.getSizePortion()));
    } else {
      tvPortion.setText(itemView.getResources()
          .getString(R.string.srch_not_liquid, food.getCountPortions() * food.getSizePortion()));
    }
    tbSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      @Override public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        listener.click(getAdapterPosition(), b);
      }
    });
  }

}

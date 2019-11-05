package com.wsoteam.diet.presentation.search.basket.controller;

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
import com.wsoteam.diet.Config;
import com.wsoteam.diet.R;
import com.wsoteam.diet.presentation.search.basket.db.BasketEntity;
import com.wsoteam.diet.presentation.search.results.controllers.ClickListener;

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
    handleWeightAndKcal(food);
    tvKcal.setText(String.valueOf(
        Math.round(food.getCalories() * food.getCountPortions() * food.getSizePortion()))
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

  private void handleWeightAndKcal(BasketEntity food) {
    String portion, kcal;
    if (food.getNamePortion().equals(Config.DEFAULT_PORTION_NAME)){

    }else {
      if (food.getNamePortion().equals(Config.DEFAULT_CUSTOM_NAME)){
        kcal = String.valueOf(
            Math.round(food.getCalories() * food.getCountPortions() * food.getSizePortion()))
            + " "
            + itemView.getResources().getString(R.string.tvKkal);
        portion = String.valueOf(food.getCountPortions() * food.getSizePortion());
      }else {

      }
    }
    if (food.getNamePortion().equals(Config.DEFAULT_CUSTOM_NAME)){
      portion
    }else {

    }
  }
}

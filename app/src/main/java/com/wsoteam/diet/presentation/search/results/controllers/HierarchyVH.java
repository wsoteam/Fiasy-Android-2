package com.wsoteam.diet.presentation.search.results.controllers;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.wsoteam.diet.R;
import com.wsoteam.diet.common.networking.food.POJO.Result;
import com.wsoteam.diet.presentation.search.results.controllers.expandable.ExpandableAdapter;

public class HierarchyVH extends RecyclerView.ViewHolder {
  @BindView(R.id.tvTitle) TextView tvTitle;
  @BindView(R.id.tvPortion) TextView tvPortion;
  @BindView(R.id.tvKcal) TextView tvKcal;
  @BindView(R.id.ivOpenList) ImageView ivOpenList;
  @BindView(R.id.rvExpList) RecyclerView rvExpList;

  public HierarchyVH(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup) {
    super(layoutInflater.inflate(R.layout.item_search_result_hierarchy, viewGroup, false));
    ButterKnife.bind(this, itemView);
    rvExpList.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
  }

  public void bind(Result result) {
    tvTitle.setText(result.getName());
    if (result.getBrand() != null && !result.getBrand().getName().equals("")) {
      tvTitle.append(" (" + result.getBrand().getName() + ")");
    }
    tvKcal.setText(String.valueOf(Math.round(result.getCalories() * 100)) + " " + itemView.getContext().getResources().getString(R.string.marker_kcal));
    tvKcal.setText(getKcalInterval(result));
    tvPortion.setText(getPortionsInterval(result));
    rvExpList.setAdapter(new ExpandableAdapter(result));
  }

  private String getPortionsInterval(Result result) {
    String unit;
    if (result.isLiquid()){
      unit = itemView.getResources().getString(R.string.srch_ml);
    }else {
      unit = itemView.getResources().getString(R.string.srch_gramm);
    }
    int size = result.getMeasurementUnits().size();
    int firstPortion = result.getMeasurementUnits().get(0).getAmount();
    int lastPortion = result.getMeasurementUnits().get(result.getMeasurementUnits().size() - 1).getAmount();
    return itemView.getResources().getQuantityString(R.plurals.srch_portions, size, size) + " (" + String.valueOf(firstPortion) + " - " + String.valueOf(lastPortion) + " " + unit;
  }

  private String getKcalInterval(Result result) {
    long firstKcal =
        Math.round(result.getMeasurementUnits().get(0).getAmount() * result.getCalories());
    long lastKcal = Math.round(
        result.getMeasurementUnits().get(result.getMeasurementUnits().size() - 1).getAmount()
            * result.getCalories());
    return String.valueOf(firstKcal)
        + " - "
        + String.valueOf(lastKcal)
        + " "
        + itemView.getContext().getResources().getString(R.string.marker_kcal);
  }
}

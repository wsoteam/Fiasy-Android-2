package com.losing.weight.presentation.search.results.controllers;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.bumptech.glide.Glide;
import com.losing.weight.R;
import com.losing.weight.common.networking.food.POJO.Result;
import com.losing.weight.presentation.search.results.controllers.expandable.ExpandableAdapter;
import java.util.List;

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

  public void bind(Result result, List<Integer> savedDeepIds, ExpandableClickListener listener) {
    tvTitle.setText(result.getName());
    if (result.getBrand() != null && result.getBrand().getName() != null && !result.getBrand().getName().equals("")) {
      tvTitle.append(" (" + result.getBrand().getName() + ")");
    }
    tvKcal.setText(
        String.valueOf(Math.round(result.getCalories() * 100)) + " " + itemView.getContext()
            .getResources()
            .getString(R.string.srch_kcal));
    tvKcal.setText(getKcalInterval(result));
    tvPortion.setText(getPortionsInterval(result));
    rvExpList.setAdapter(new ExpandableAdapter(result, listener, savedDeepIds));
  }

  private String getPortionsInterval(Result result) {
    String unit;
    if (result.isLiquid()) {
      unit = itemView.getResources().getString(R.string.srch_ml);
    } else {
      unit = itemView.getResources().getString(R.string.srch_gramm);
    }
    int size = result.getMeasurementUnits().size();
    int firstPortion = result.getMeasurementUnits().get(0).getAmount();
    int lastPortion =
        result.getMeasurementUnits().get(result.getMeasurementUnits().size() - 1).getAmount();
    return itemView.getResources().getQuantityString(R.plurals.srch_portions, size, size)
        + " ("
        + String.valueOf(firstPortion)
        + " - "
        + String.valueOf(lastPortion)
        + " "
        + unit
        + ")";
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
        + itemView.getContext().getResources().getString(R.string.srch_kcal);
  }

  @OnClick(R.id.ivOpenList) public void onViewClicked() {
    if (rvExpList.getVisibility() == View.GONE) {
      Glide.with(itemView.getContext()).load(R.drawable.srch_arrow_up).into(ivOpenList);
      rvExpList.setVisibility(View.VISIBLE);
    } else {
      Glide.with(itemView.getContext()).load(R.drawable.srch_arrow_down).into(ivOpenList);
      rvExpList.setVisibility(View.GONE);
    }
  }
}

package com.wsoteam.diet.presentation.search.results.controllers.expandable;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.wsoteam.diet.common.networking.food.POJO.Result;

public class ExpandableAdapter extends RecyclerView.Adapter<ExpandableVH> {
  private Result result;

  public ExpandableAdapter(Result result) {
    this.result = result;
  }

  @NonNull @Override
  public ExpandableVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
    return new ExpandableVH(layoutInflater, parent);
  }

  @Override public void onBindViewHolder(@NonNull ExpandableVH holder, int position) {
      holder.bind(result.getName(), result.getMeasurementUnits().get(position).getAmount(), result.getCalories(), result.isLiquid());
  }

  @Override public int getItemCount() {
    return result.getMeasurementUnits().size();
  }
}

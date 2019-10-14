package com.wsoteam.diet.presentation.search.results.controllers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.wsoteam.diet.common.networking.food.POJO.Result;
import java.util.List;

public class ResultAdapter extends RecyclerView.Adapter<ResultViewHolder> {
  private List<Result> foods;

  public ResultAdapter(
      List<Result> foods) {
    this.foods = foods;
  }

  @NonNull @Override
  public ResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
    return new ResultViewHolder(layoutInflater, parent);
  }

  @Override public void onBindViewHolder(@NonNull ResultViewHolder holder, int position) {
    holder.bind(foods.get(position));
  }

  @Override public int getItemCount() {
    return foods.size();
  }
}

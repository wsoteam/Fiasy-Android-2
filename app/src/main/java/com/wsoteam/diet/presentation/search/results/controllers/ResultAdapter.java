package com.wsoteam.diet.presentation.search.results.controllers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.wsoteam.diet.common.networking.food.HeaderObj;
import com.wsoteam.diet.common.networking.food.ISearchResult;
import com.wsoteam.diet.common.networking.food.POJO.Result;
import java.util.List;

public class ResultAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
  private List<ISearchResult> foods;
  private final int HEADER_TYPE = 0;
  private final int ITEM_TYPE = 1;

  public ResultAdapter(
      List<ISearchResult> foods) {
    this.foods = foods;
  }

  @NonNull @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
    switch (viewType) {
      case HEADER_TYPE:
        return new HeaderViewHolder(layoutInflater, parent);
      case ITEM_TYPE:
      default: return new ResultViewHolder(layoutInflater, parent);
    }
  }

  @Override public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
    switch (holder.getItemViewType()){
      case HEADER_TYPE:((HeaderViewHolder)holder).bind((HeaderObj) foods.get(position));
      case ITEM_TYPE:((ResultViewHolder)holder).bind((Result) foods.get(position));
    }
  }

  @Override public int getItemCount() {
    return foods.size();
  }

  @Override public int getItemViewType(int position) {
    if (foods.get(position) instanceof HeaderObj) {
      return HEADER_TYPE;
    } else if (foods.get(position) instanceof Result) {
      return ITEM_TYPE;
    } else {
      return 0;
    }
  }
}

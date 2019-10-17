package com.wsoteam.diet.presentation.search.basket.controller;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.wsoteam.diet.common.networking.food.HeaderObj;
import com.wsoteam.diet.common.networking.food.ISearchResult;
import com.wsoteam.diet.common.networking.food.POJO.Result;
import com.wsoteam.diet.presentation.search.basket.db.BasketEntity;
import java.util.ArrayList;
import java.util.List;

public class BasketAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
  private List<ISearchResult> adapterFoods;
  private final int HEADER_TYPE = 0;
  private final int ITEM_TYPE = 1;
  private List<List<BasketEntity>> allFood;
  private String[] namesSections;

  public BasketAdapter(List<List<BasketEntity>> allFood, String[] namesSections) {
    this.allFood = allFood;
    this.namesSections = namesSections;
    formGlobalList();
  }

  private void formGlobalList() {
    adapterFoods = new ArrayList<>();
    for (int i = 0; i < allFood.size(); i++) {
      if (allFood.get(i).size() > 0) {
        adapterFoods.add(new HeaderObj(namesSections[i], false));
        for (int j = 0; j < allFood.get(i).size(); j++) {
          adapterFoods.add(allFood.get(i).get(j));
        }
      }
    }
  }

  @NonNull @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
    switch (viewType) {
      case HEADER_TYPE:
        return new BasketHeaderVH(layoutInflater, parent);
      case ITEM_TYPE:
        return new BasketItemVH(layoutInflater, parent);
      default:
        throw new IllegalArgumentException("Invalid view type");
    }
  }

  @Override public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
    switch (holder.getItemViewType()) {
      case HEADER_TYPE:
        ((BasketHeaderVH) holder).bind((HeaderObj) adapterFoods.get(position));
        break;
      case ITEM_TYPE:
        ((BasketItemVH) holder).bind((BasketEntity) adapterFoods.get(position));
        break;
    }
  }

  @Override public int getItemCount() {
    return adapterFoods.size();
  }

  @Override public int getItemViewType(int position) {
    if (adapterFoods.get(position) instanceof HeaderObj) {
      return HEADER_TYPE;
    } else if (adapterFoods.get(position) instanceof BasketEntity) {
      return ITEM_TYPE;
    } else {
      return -1;
    }
  }
}

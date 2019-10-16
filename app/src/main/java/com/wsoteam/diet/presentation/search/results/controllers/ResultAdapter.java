package com.wsoteam.diet.presentation.search.results.controllers;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.wsoteam.diet.App;
import com.wsoteam.diet.BranchOfAnalyzer.POJOFoodSQL.FoodDAO;
import com.wsoteam.diet.common.networking.food.HeaderObj;
import com.wsoteam.diet.common.networking.food.ISearchResult;
import com.wsoteam.diet.common.networking.food.POJO.Result;
import com.wsoteam.diet.presentation.search.basket.db.BasketDAO;
import com.wsoteam.diet.presentation.search.basket.db.BasketEntity;
import java.util.List;

public class ResultAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
  private List<ISearchResult> foods;
  private final int HEADER_TYPE = 0;
  private final int ITEM_TYPE = 1;
  private final int EXPANDABLE_TYPE = 2;
  private Context context;

  public ResultAdapter(List<ISearchResult> foods, Context context) {
    this.foods = foods;
    this.context = context;
  }

  @NonNull @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    LayoutInflater layoutInflater = LayoutInflater.from(context);
    switch (viewType) {
      case HEADER_TYPE:
        return new HeaderVH(layoutInflater, parent);
      case ITEM_TYPE:
        return new ResultVH(layoutInflater, parent);
      case EXPANDABLE_TYPE:
        return new HierarchyVH(layoutInflater, parent);
      default:
        throw new IllegalArgumentException("Invalid view type");
    }
  }

  @Override public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
    switch (holder.getItemViewType()) {
      case HEADER_TYPE:
        ((HeaderVH) holder).bind((HeaderObj) foods.get(position));
        break;
      case ITEM_TYPE:
        ((ResultVH) holder).bind((Result) foods.get(position), new ClickListener() {
          @Override public void click(int position) {
              saveInBasket(position);
          }
        });
        break;
      case EXPANDABLE_TYPE:
        ((HierarchyVH) holder).bind((Result) foods.get(position));
        break;
    }
  }

  private void saveInBasket(int position) {
    BasketDAO basketDAO = App.getInstance().getFoodDatabase().basketDAO();
    basketDAO.insert(new BasketEntity((Result) foods.get(position), 100, 0));
    Toast.makeText(context, "saved", Toast.LENGTH_SHORT).show();
  }

  @Override public int getItemCount() {
    return foods.size();
  }

  @Override public int getItemViewType(int position) {
    if (foods.get(position) instanceof HeaderObj) {
      return HEADER_TYPE;
    } else if (foods.get(position) instanceof Result) {
      if (((Result) foods.get(position)).getMeasurementUnits() == null
          || ((Result) foods.get(position)).getMeasurementUnits().size() < 1) {
        return ITEM_TYPE;
      } else {
        Log.e("LOL", "kekkekeke");
        return EXPANDABLE_TYPE;
      }
    } else {
      return -1;
    }
  }
}

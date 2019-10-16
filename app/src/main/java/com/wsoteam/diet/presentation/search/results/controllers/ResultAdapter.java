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
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;
import java.util.List;
import java.util.concurrent.Callable;

public class ResultAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
  private List<ISearchResult> foods;
  private final int HEADER_TYPE = 0;
  private final int ITEM_TYPE = 1;
  private final int EXPANDABLE_TYPE = 2;
  private Context context;
  private List<BasketEntity> savedFood;
  BasketDAO basketDAO;

  public ResultAdapter(List<ISearchResult> foods, Context context, List<BasketEntity> savedFood) {
    this.foods = foods;
    this.context = context;
    this.savedFood = savedFood;
    basketDAO = App.getInstance().getFoodDatabase().basketDAO();
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
        ((ResultVH) holder).bind((Result) foods.get(position),
            getSaveStatus((Result) foods.get(position)), new ClickListener() {
              @Override public void click(int position, boolean isNeedSave) {
                if (isNeedSave) {
                  save(position);
                } else {
                  delete(position);
                }
              }
            });
        break;
      case EXPANDABLE_TYPE:
        ((HierarchyVH) holder).bind((Result) foods.get(position));
        break;
    }
  }

  private boolean getSaveStatus(Result result) {
    boolean isSaved = false;
    for (int i = 0; i < savedFood.size(); i++) {
      if (result.getId() == savedFood.get(i).getId()) {
        isSaved = true;
        break;
      }
    }
    return isSaved;
  }

  private void delete(int position) {
    Completable.fromAction(new Action() {
      @Override
      public void run() throws Exception {
        basketDAO.deleteById(((Result)foods.get(position)).getId());
      }
    }).subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe();
  }

  private BasketEntity getEntity(int position) {
    BasketEntity basketEntity = new BasketEntity();
    for (int i = 0; i < savedFood.size(); i++) {
      if (savedFood.get(i).getId() == ((Result) foods.get(position)).getId()){
        basketEntity = new BasketEntity((Result) foods.get(position), 100, 0);
        break;
      }
    }
    return basketEntity;
  }

  private void save(int position) {
    Completable.fromAction(new Action() {
      @Override
      public void run() throws Exception {
        basketDAO.insert(new BasketEntity((Result) foods.get(position), 100, 0));
      }
    }).subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe();
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
        return EXPANDABLE_TYPE;
      }
    } else {
      return -1;
    }
  }
}

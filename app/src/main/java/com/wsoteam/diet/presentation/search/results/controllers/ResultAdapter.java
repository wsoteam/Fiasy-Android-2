package com.wsoteam.diet.presentation.search.results.controllers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.wsoteam.diet.App;
import com.wsoteam.diet.Config;
import com.wsoteam.diet.common.diary.FoodWork;
import com.wsoteam.diet.common.networking.food.HeaderObj;
import com.wsoteam.diet.common.networking.food.ISearchResult;
import com.wsoteam.diet.common.networking.food.POJO.Result;
import com.wsoteam.diet.presentation.search.basket.db.BasketDAO;
import com.wsoteam.diet.presentation.search.basket.db.BasketEntity;
import com.wsoteam.diet.presentation.search.basket.db.HistoryEntity;
import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;
import java.util.ArrayList;
import java.util.List;

public class ResultAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
    implements IResult {
  private List<ISearchResult> foods;
  private final int HEADER_TYPE = 0;
  private final int ITEM_TYPE = 1;
  private final int EXPANDABLE_TYPE = 2;
  private final int HISTORY_TYPE = 3;
  private Context context;
  private List<BasketEntity> savedFood;
  private BasketDAO basketDAO;
  private BasketUpdater basketUpdater;

  @Override public void save(String date) {
    FoodWork.saveClearList(savedFood, date);
    clearDB();
    savedFood = new ArrayList<>();
  }

  private void clearDB() {
    Completable.fromAction(new Action() {
      @Override
      public void run() throws Exception {
        basketDAO.deleteAll();
      }
    }).subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe();
  }

  public ResultAdapter(List<ISearchResult> foods, Context context, List<BasketEntity> savedFood,
      BasketUpdater basketUpdater) {
    this.foods = foods;
    this.context = context;
    basketDAO = App.getInstance().getFoodDatabase().basketDAO();
    this.basketUpdater = basketUpdater;
    this.savedFood = savedFood;
    basketUpdater.getCurrentSize(savedFood.size());
  }

  @NonNull @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    LayoutInflater layoutInflater = LayoutInflater.from(context);
    switch (viewType) {
      case HEADER_TYPE:
        return new HeaderVH(layoutInflater, parent);
      case HISTORY_TYPE:
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
                BasketEntity basketEntity =
                    new BasketEntity((Result) foods.get(position), Config.DEFAULT_PORTION,
                        basketUpdater.getCurrentEating(), -1);
                if (isNeedSave) {
                  save(basketEntity);
                } else {
                  delete(basketEntity);
                }
              }
            });
        break;
      case HISTORY_TYPE:
        ((ResultVH) holder).bindHistoryEntity((HistoryEntity) foods.get(position),
            false, new ClickListener() {
              @Override public void click(int position, boolean isNeedSave) {
                BasketEntity basketEntity = (BasketEntity) foods.get(position);
                basketEntity.setEatingType(basketUpdater.getCurrentEating());
                if (isNeedSave) {
                  save(basketEntity);
                } else {
                  delete(basketEntity);
                }
              }
            });
        break;
      case EXPANDABLE_TYPE:
        ((HierarchyVH) holder).bind((Result) foods.get(position),
            getSavedDeepIds((Result) foods.get(position)), new ExpandableClickListener() {
              @Override public void click(BasketEntity basketEntity, boolean isNeedSave) {
                basketEntity.setEatingType(basketUpdater.getCurrentEating());
                if (isNeedSave) {
                  save(basketEntity);
                } else {
                  delete(basketEntity);
                }
              }
            });
        break;
    }
  }

  private boolean getSaveStatus(Result result) {
    boolean isSaved = false;
    for (int i = 0; i < savedFood.size(); i++) {
      if (result.getId() == savedFood.get(i).getServerId()) {
        isSaved = true;
        break;
      }
    }
    return isSaved;
  }

  private List<Integer> getSavedDeepIds(Result result) {
    List<Integer> savedDeepIds = new ArrayList<>();

    for (int i = 0; i < savedFood.size(); i++) {
      if (result.getId() == savedFood.get(i).getServerId()) {
        savedDeepIds.add(savedFood.get(i).getDeepId());
      }
    }
    return savedDeepIds;
  }

  private void delete(BasketEntity basketEntity) {
    Completable.fromAction(new Action() {
      @Override
      public void run() throws Exception {
        basketDAO.deleteById(basketEntity.getServerId(), basketEntity.getDeepId());
      }
    }).subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new CompletableObserver() {
          @Override public void onSubscribe(Disposable d) {

          }

          @Override public void onComplete() {
            removeItem(basketEntity);
          }

          @Override public void onError(Throwable e) {

          }
        });
  }

  private void removeItem(BasketEntity basketEntity) {
    for (int i = 0; i < savedFood.size(); i++) {
      if (savedFood.get(i).getServerId() == basketEntity.getServerId()
          && savedFood.get(i).getDeepId() == basketEntity.getDeepId()) {
        savedFood.remove(i);
        break;
      }
    }
    basketUpdater.getCurrentSize(savedFood.size());
  }

  private void save(BasketEntity basketEntity) {
    Completable.fromAction(new Action() {
      @Override
      public void run() throws Exception {
        basketDAO.insert(basketEntity);
      }
    }).subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new CompletableObserver() {
          @Override public void onSubscribe(Disposable d) {

          }

          @Override public void onComplete() {
            addNewItem(basketEntity);
          }

          @Override public void onError(Throwable e) {

          }
        });
  }

  private void addNewItem(BasketEntity basketEntity) {
    savedFood.add(basketEntity);
    basketUpdater.getCurrentSize(savedFood.size());
  }

  @Override public int getItemCount() {
    return foods.size();
  }

  @Override public int getItemViewType(int position) {
    if (foods.get(position) instanceof HistoryEntity) {
      return HISTORY_TYPE;
    } else if (foods.get(position) instanceof HeaderObj) {
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

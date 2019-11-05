package com.wsoteam.diet.presentation.search.results.controllers;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.wsoteam.diet.App;
import com.wsoteam.diet.Config;
import com.wsoteam.diet.common.diary.FoodWork;
import com.wsoteam.diet.common.networking.food.FoodResultAPI;
import com.wsoteam.diet.common.networking.food.FoodSearch;
import com.wsoteam.diet.common.networking.food.HeaderObj;
import com.wsoteam.diet.common.networking.food.ISearchResult;
import com.wsoteam.diet.common.networking.food.POJO.Result;
import com.wsoteam.diet.presentation.search.ParentActivity;
import com.wsoteam.diet.presentation.search.product.DetailActivity;
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
  private int currentPaginationTrigger = 1;
  private int page = 1;
  private String searchString = "";
  private FoodResultAPI foodResultAPI = FoodSearch.getInstance().getFoodSearchAPI();

  @Override public void sendSearchString(String searchString) {
    this.searchString = searchString;
  }

  @Override public void save(String date) {
    FoodWork.saveClearList(savedFood, date);
    clearDB();
    savedFood = new ArrayList<>();
  }

  @Override public void setNewBasket(List<BasketEntity> savedFood) {
    this.savedFood = savedFood;
    notifyDataSetChanged();
    basketUpdater.getCurrentSize(savedFood.size());
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
      String searchString, BasketUpdater basketUpdater) {
    this.searchString = searchString;
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

  private void loadNextPortion(int page) {
    Log.e("LOL", "pag_response");
    foodResultAPI
        .search(searchString, page)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(t -> addItems(t.getResults()), Throwable::printStackTrace);
  }

  private void addItems(List<Result> results) {
    Log.e("LOL", "pag" + String.valueOf(results.size()));
    for (int i = 0; i < results.size(); i++) {
      foods.add(results.get(i));
    }
    notifyDataSetChanged();
  }

  @Override public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
    switch (holder.getItemViewType()) {
      case HEADER_TYPE:
        onPagination(position);
        ((HeaderVH) holder).bind((HeaderObj) foods.get(position));
        break;
      case ITEM_TYPE:
        onPagination(position);
        ((ResultVH) holder).bind((Result) foods.get(position),
            getSaveStatus((Result) foods.get(position)), new ClickListener() {
              @Override public void click(int position, boolean isNeedSave) {
                BasketEntity basketEntity = createBasketEntity(position);
                if (isNeedSave) {
                  save(basketEntity);
                } else {
                  delete(basketEntity);
                }
              }

              @Override public void open(int position) {
                openDetailActivity(createBasketEntity(position));
              }
            });
        break;
      case HISTORY_TYPE:
        ((ResultVH) holder).bindHistoryEntity((HistoryEntity) foods.get(position),
            false, new ClickListener() {
              @Override public void click(int position, boolean isNeedSave) {
                BasketEntity basketEntity = createBasketEntity(position);
                if (isNeedSave) {
                  save(basketEntity);
                } else {
                  delete(basketEntity);
                }
              }

              @Override public void open(int position) {
                openDetailActivity(createBasketEntity(position));
              }
            });
        break;
      case EXPANDABLE_TYPE:
        onPagination(position);
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

              @Override public void open(BasketEntity basketEntity) {
                basketEntity.setEatingType(basketUpdater.getCurrentEating());
                openDetailActivity(basketEntity);
              }
            });
        break;
    }
  }

  private void openDetailActivity(BasketEntity basketEntity) {
    Intent intent = new Intent(new Intent(context, DetailActivity.class));
    intent.putExtra(Config.INTENT_DETAIL_FOOD, basketEntity);
    ((ParentActivity) context).startActivityForResult(intent, Config.RC_DETAIL_FOOD);
  }

  private BasketEntity createBasketEntity(int position) {
    return new BasketEntity((Result) foods.get(position), Config.DEFAULT_PORTION, Config.DEFAULT_WEIGHT, Config.DEFAULT_PORTION_NAME,
        basketUpdater.getCurrentEating(), -1);
  }


  private void onPagination(int position) {
    if (position == currentPaginationTrigger) {
      currentPaginationTrigger += Config.SEARCH_RESPONSE_LIMIT - 1;
      page += 1;
      loadNextPortion(page);
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

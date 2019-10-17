package com.wsoteam.diet.presentation.search.basket;

import android.content.Context;
import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.wsoteam.diet.App;
import com.wsoteam.diet.presentation.search.basket.db.BasketDAO;
import com.wsoteam.diet.presentation.search.basket.db.BasketEntity;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import java.util.ArrayList;
import java.util.List;

@InjectViewState
public class BasketPresenter extends MvpPresenter<BasketView> {
  private BasketDAO basketDAO = App.getInstance().getFoodDatabase().basketDAO();
  private final int BREAKFAST = 0, LUNCH = 1, DINNER = 2, SNACK = 3;
  private Context context;

  public BasketPresenter(Context context) {
    this.context = context;
  }

  public void getBasketLists() {
    Single.fromCallable(() -> {
      List<BasketEntity> savedItems = getSavedItems();
      return savedItems;
    })
        .subscribeOn(Schedulers.computation())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(t -> sortFood(t), Throwable::printStackTrace);
  }

  private void sortFood(List<BasketEntity> t) {
    List<BasketEntity> breakfasts = new ArrayList<>();
    List<BasketEntity> lunches = new ArrayList<>();
    List<BasketEntity> dinners = new ArrayList<>();
    List<BasketEntity> snacks = new ArrayList<>();

    for (int i = 0; i < t.size(); i++) {
      switch (t.get(i).getEatingType()) {
        case BREAKFAST:
          breakfasts.add(t.get(i));
          break;
        case LUNCH:
          lunches.add(t.get(i));
          break;
        case DINNER:
          dinners.add(t.get(i));
          break;
        case SNACK:
          snacks.add(t.get(i));
          break;
      }
    }
    List<List<BasketEntity>> allFood = new ArrayList<>();
    allFood.add(breakfasts);
    allFood.add(lunches);
    allFood.add(dinners);
    allFood.add(snacks);
    getViewState().getSortedData(allFood);
  }

  private List<BasketEntity> getSavedItems() {
    List<BasketEntity> entities = basketDAO.getAll();
    return entities;
  }
}

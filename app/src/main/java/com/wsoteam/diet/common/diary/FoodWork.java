package com.wsoteam.diet.common.diary;

import com.wsoteam.diet.App;
import com.wsoteam.diet.Sync.WorkWithFirebaseDB;
import com.wsoteam.diet.common.networking.food.ISearchResult;
import com.wsoteam.diet.model.Breakfast;
import com.wsoteam.diet.model.Dinner;
import com.wsoteam.diet.model.Lunch;
import com.wsoteam.diet.model.Snack;
import com.wsoteam.diet.presentation.search.basket.db.BasketDAO;
import com.wsoteam.diet.presentation.search.basket.db.BasketEntity;
import com.wsoteam.diet.presentation.search.basket.db.HistoryDAO;
import com.wsoteam.diet.presentation.search.basket.db.HistoryEntity;
import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class FoodWork {
  public static final int BREAKFAST = 0;
  public static final int LUNCH = 1;
  public static final int DINNER = 2;
  public static final int SNACK = 3;
  public static final int SIZE = 10;

  public static void saveMixedList(List<ISearchResult> foods, String date) {
    String[] arrayOfNumbersForDate = date.split("\\.");
    int day = Integer.parseInt(arrayOfNumbersForDate[0]);
    int month = Integer.parseInt(arrayOfNumbersForDate[1]) - 1;
    int year = Integer.parseInt(arrayOfNumbersForDate[2]);
    List<HistoryEntity> forSave = new ArrayList<>();
    for (int i = 0; i < foods.size(); i++) {
      if (foods.get(i) instanceof BasketEntity) {
        forSave.add(new HistoryEntity((BasketEntity)foods.get(i)));
        saveItem((BasketEntity) foods.get(i), day, month, year);
      }
    }
    updateHistoryList(forSave);
  }


  public static void saveClearList(List<BasketEntity> foods, String date) {
    String[] arrayOfNumbersForDate = date.split("\\.");
    int day = Integer.parseInt(arrayOfNumbersForDate[0]);
    int month = Integer.parseInt(arrayOfNumbersForDate[1]) - 1;
    int year = Integer.parseInt(arrayOfNumbersForDate[2]);
    List<HistoryEntity> forSave = new ArrayList<>();
    for (int i = 0; i < foods.size(); i++) {
      forSave.add(new HistoryEntity(foods.get(i)));
      saveItem(foods.get(i), day, month, year);
    }
    updateHistoryList(forSave);
  }

  public static void clearBasket(){
    Completable.fromAction(new Action() {
      @Override
      public void run() throws Exception {
        BasketDAO dao = App.getInstance().getFoodDatabase().basketDAO();
        dao.deleteAll();
      }
    }).subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe();
  }

  private static void saveItem(BasketEntity basketEntity, int day, int month, int year) {
    switch (basketEntity.getEatingType()) {
      case BREAKFAST:
        WorkWithFirebaseDB.
            addBreakfast(new Breakfast(basketEntity, day, month, year, 0));
        break;
      case LUNCH:
        WorkWithFirebaseDB.
            addLunch(new Lunch(basketEntity, day, month, year, 0));
        break;
      case DINNER:
        WorkWithFirebaseDB.
            addDinner(new Dinner(basketEntity, day, month, year, 0));
        break;
      case SNACK:
        WorkWithFirebaseDB.
            addSnack(new Snack(basketEntity, day, month, year, 0));
        break;
    }
  }

  private static void updateHistoryList(List<HistoryEntity> forSave) {
    Completable.fromAction(new Action() {
      @Override
      public void run() throws Exception {
        HistoryDAO dao = App.getInstance().getFoodDatabase().historyDAO();
        List<HistoryEntity> historyEntities = dao.getAll();
        dao.deleteAll();
        historyEntities.addAll(forSave);
        historyEntities = cut(historyEntities, SIZE);
        dao.insertMany(historyEntities);
      }
    }).subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe();
  }

  private static List<HistoryEntity> cut(List<HistoryEntity> historyEntities, int size) {
    if (historyEntities.size() < size){
      return historyEntities;
    }else {
      return historyEntities.subList(historyEntities.size() - size - 1, historyEntities.size() - 1);
    }
  }
}

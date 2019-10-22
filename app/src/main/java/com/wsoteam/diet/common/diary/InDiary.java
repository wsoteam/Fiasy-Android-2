package com.wsoteam.diet.common.diary;

import com.wsoteam.diet.App;
import com.wsoteam.diet.Sync.WorkWithFirebaseDB;
import com.wsoteam.diet.common.networking.food.ISearchResult;
import com.wsoteam.diet.model.Breakfast;
import com.wsoteam.diet.model.Dinner;
import com.wsoteam.diet.model.Lunch;
import com.wsoteam.diet.model.Snack;
import com.wsoteam.diet.presentation.search.basket.db.BasketEntity;
import com.wsoteam.diet.presentation.search.basket.db.HistoryDAO;
import com.wsoteam.diet.presentation.search.basket.db.HistoryEntity;
import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;
import java.util.Calendar;
import java.util.List;

public class InDiary {
  public static final int BREAKFAST = 0;
  public static final int LUNCH = 1;
  public static final int DINNER = 2;
  public static final int SNACK = 3;

  public static void saveMixedList(List<ISearchResult> foods) {
    for (int i = 0; i < foods.size(); i++) {
      if (foods.get(i) instanceof BasketEntity) {
        saveItem((BasketEntity) foods.get(i));
      }
    }
    updateHistoryList();
  }


  public static void saveClearList(List<BasketEntity> foods) {
    for (int i = 0; i < foods.size(); i++) {
      saveItem(foods.get(i));
    }
    updateHistoryList();
  }

  private static void saveItem(BasketEntity basketEntity) {
    Calendar calendar = Calendar.getInstance();
    int day = calendar.get(Calendar.DAY_OF_MONTH);
    int month = calendar.get(Calendar.MONDAY);
    int year = calendar.get(Calendar.YEAR);

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

    Completable.fromAction(new Action() {
      @Override
      public void run() throws Exception {
        App.getInstance().getFoodDatabase().historyDAO().insert(new HistoryEntity(basketEntity));
      }
    }).subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe();
  }

  private static void updateHistoryList() {
    Completable.fromAction(new Action() {
      @Override
      public void run() throws Exception {
        int counter = 0;
        HistoryDAO dao = App.getInstance().getFoodDatabase().historyDAO();
        List<HistoryEntity> historyEntities = dao.getAll();
        dao.deleteAll();
        for (int i = historyEntities.size() - 1; i >= 0; i--) {
          dao.insert(historyEntities.get(i));
          counter ++;
          if (counter > 10){
            break;
          }
        }
      }
    }).subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe();
  }
}

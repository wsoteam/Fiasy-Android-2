package com.wsoteam.diet.common.diary;

import com.wsoteam.diet.Sync.WorkWithFirebaseDB;
import com.wsoteam.diet.common.networking.food.ISearchResult;
import com.wsoteam.diet.model.Breakfast;
import com.wsoteam.diet.model.Dinner;
import com.wsoteam.diet.model.Eating;
import com.wsoteam.diet.model.Lunch;
import com.wsoteam.diet.model.Snack;
import com.wsoteam.diet.presentation.search.basket.db.BasketEntity;
import java.util.Calendar;
import java.util.List;

public class InDiary {
  public static final int BREAKFAST = 0;
  public static final int LUNCH = 1;
  public static final int DINNER = 2;
  public static final int SNACK = 3;

  public static void saveList(List<ISearchResult> foods){
    for (int i = 0; i < foods.size(); i++) {
      if (foods.get(i) instanceof BasketEntity){
        saveItem((BasketEntity) foods.get(i));
      }
    }
  }

  private static void saveItem(BasketEntity basketEntity) {
    Calendar calendar = Calendar.getInstance();
    int day = calendar.get(Calendar.DAY_OF_MONTH);
    int month = calendar.get(Calendar.MONDAY) - 1;
    int year = calendar.get(Calendar.YEAR);
    Eating eating = new Eating(basketEntity, day, month, year, 0);
    switch (basketEntity.getEatingType()){
      case BREAKFAST: WorkWithFirebaseDB.
          addBreakfast((Breakfast) eating);
      break;
      case LUNCH: WorkWithFirebaseDB.
          addLunch((Lunch) eating);
        break;
      case DINNER: WorkWithFirebaseDB.
          addDinner((Dinner) eating);
        break;
      case SNACK: WorkWithFirebaseDB.
          addSnack((Snack) eating);
        break;
    }
  }
}

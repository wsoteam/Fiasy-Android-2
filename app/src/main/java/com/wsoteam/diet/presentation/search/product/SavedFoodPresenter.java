package com.wsoteam.diet.presentation.search.product;

import android.content.Context;
import com.arellomobile.mvp.InjectViewState;
import com.wsoteam.diet.Config;
import com.wsoteam.diet.Sync.WorkWithFirebaseDB;
import com.wsoteam.diet.model.Breakfast;
import com.wsoteam.diet.model.Dinner;
import com.wsoteam.diet.model.Eating;
import com.wsoteam.diet.model.Lunch;
import com.wsoteam.diet.model.Snack;
import com.wsoteam.diet.presentation.search.basket.db.BasketEntity;

@InjectViewState
public class SavedFoodPresenter extends BasketDetailPresenter {
  private int typeEating;
  private int day, month, year;
  private String keySave;

  public SavedFoodPresenter(Eating eating, Context context, int typeEating) {
    super(context, new BasketEntity(eating, typeEating));
    this.typeEating = typeEating;
    day = eating.getDay();
    month = eating.getMonth();
    year = eating.getYear();
    keySave = eating.getUrlOfImages();
  }

  @Override public void saveEntity(BasketEntity basketEntity) {
    switch (typeEating) {
      case Config.BREAKFAST:
        WorkWithFirebaseDB.
            editBreakfast(
                new Breakfast(basketEntity, day, month, year, 0),
                keySave);
        break;
      case Config.LUNCH:
        WorkWithFirebaseDB.
            editLunch(new Lunch(basketEntity, day, month, year, 0),
                keySave);
        break;
      case Config.DINNER:
        WorkWithFirebaseDB.
            editDinner(
                new Dinner(basketEntity, day, month, year, 0),
                keySave);
        break;
      case Config.SNACK:
        WorkWithFirebaseDB.
            editSnack(new Snack(basketEntity, day, month, year, 0),
                keySave);
        break;
    }
    getViewState().close();
  }
}

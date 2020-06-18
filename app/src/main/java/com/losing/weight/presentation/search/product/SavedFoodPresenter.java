package com.losing.weight.presentation.search.product;

import android.content.Context;
import com.arellomobile.mvp.InjectViewState;
import com.losing.weight.Config;
import com.losing.weight.Sync.WorkWithFirebaseDB;
import com.losing.weight.model.Breakfast;
import com.losing.weight.model.Dinner;
import com.losing.weight.model.Eating;
import com.losing.weight.model.Lunch;
import com.losing.weight.model.Snack;
import com.losing.weight.presentation.search.basket.db.BasketEntity;

@InjectViewState
public class SavedFoodPresenter extends BasketDetailPresenter {
  private int typeEating;
  private int day, month, year;
  private String keySave;

  public SavedFoodPresenter(Eating eating, Context context, int typeEating) {
    super(context, new BasketEntity(eating, typeEating));
    getViewState().hideEatSpinner();
    this.typeEating = typeEating;
    day = eating.getDay();
    month = eating.getMonth();
    year = eating.getYear();
    keySave = eating.getUrlOfImages();
  }

  @Override public void saveEntity(BasketEntity basketEntity, int eating) {
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
    getViewState().close(eating);
  }
}

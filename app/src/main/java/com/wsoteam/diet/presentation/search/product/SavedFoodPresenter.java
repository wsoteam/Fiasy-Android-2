package com.wsoteam.diet.presentation.search.product;

import android.content.Context;
import com.arellomobile.mvp.InjectViewState;
import com.wsoteam.diet.R;
import com.wsoteam.diet.model.Eating;
import com.wsoteam.diet.presentation.search.basket.db.BasketEntity;
import java.util.ArrayList;

import static com.wsoteam.diet.Config.STANDART_PORTION;

@InjectViewState
public class SavedFoodPresenter extends BasketDetailPresenter {
  private int typeEating;

  public SavedFoodPresenter(Eating eating, Context context, int typeEating) {
    super(context, new BasketEntity(eating, typeEating));
    this.typeEating = typeEating;
  }
}

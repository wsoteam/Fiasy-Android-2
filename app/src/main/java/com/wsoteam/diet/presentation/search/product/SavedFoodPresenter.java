package com.wsoteam.diet.presentation.search.product;

import android.content.Context;
import com.arellomobile.mvp.InjectViewState;
import com.wsoteam.diet.model.Eating;
import com.wsoteam.diet.presentation.search.basket.db.BasketEntity;

@InjectViewState
public class SavedFoodPresenter extends BasketDetailPresenter {
  private Eating eating;
  private Context context;
  private final int EMPTY_TYPE = -1;

  public SavedFoodPresenter(Context context, Eating eating) {
    this.eating = eating;
    this.context = context;
  }

  @Override protected void onFirstViewAttach() {
    /*handlePortions();
    int portionSize = portionsSizes.get(0);
    if (portionSize == MINIMAL_PORTION) {
      portionSize = DEFAULT_PORTION;
    }*/
    getViewState().fillFields(eating.getName(), eating.getFat(), eating.getCarbohydrates(),
        eating.getProtein(), eating.getBrand(), eating.getSugar(), eating.getSaturatedFats(),
        eating.getMonoUnSaturatedFats(), eating.getPolyUnSaturatedFats(), eating.getCholesterol(), eating.getCellulose(),
        eating.getSodium(), eating.getPottassium(), EMPTY_TYPE, 1, eating.isLiquid());
  }

  @Override void calculate(CharSequence weight) {
    super.calculate(weight);
  }

  @Override public void showClaimAlert() {
    super.showClaimAlert();
  }

  @Override public void changePortion(int position) {
    super.changePortion(position);
  }

  @Override public void save(String weight, String prot, String fats, String carbo, String kcal,
      int selectedItemPosition) {
    super.save(weight, prot, fats, carbo, kcal, selectedItemPosition);
  }

  @Override void handlePortions() {
    super.handlePortions();
  }
}

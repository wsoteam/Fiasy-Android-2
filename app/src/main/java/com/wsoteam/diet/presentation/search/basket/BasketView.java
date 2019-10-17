package com.wsoteam.diet.presentation.search.basket;

import com.arellomobile.mvp.MvpView;
import com.wsoteam.diet.presentation.search.basket.db.BasketEntity;
import java.util.List;

public interface BasketView extends MvpView {
  void getSortedData(List<List<BasketEntity>> allFood);
}

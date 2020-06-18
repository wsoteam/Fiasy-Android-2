package com.losing.weight.presentation.search.basket;

import com.arellomobile.mvp.MvpView;
import com.losing.weight.presentation.search.basket.db.BasketEntity;
import java.util.List;

public interface BasketView extends MvpView {
  void getSortedData(List<List<BasketEntity>> allFood);
}

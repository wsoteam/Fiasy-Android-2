package com.wsoteam.diet.presentation.search.results.controllers;

import com.wsoteam.diet.presentation.search.basket.db.BasketEntity;

public interface ExpandableClickListener {
  void click(BasketEntity basketEntity, boolean isNeedSave);
  void open(BasketEntity basketEntity);
}

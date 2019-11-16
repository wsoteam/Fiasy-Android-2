package com.wsoteam.diet.presentation.search.results.controllers;

import com.wsoteam.diet.presentation.search.basket.db.BasketEntity;

public interface ClickListener {
  void click(int position, boolean isNeedSave);
  void open(int position);
}

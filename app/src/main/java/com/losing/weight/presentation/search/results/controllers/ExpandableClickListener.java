package com.losing.weight.presentation.search.results.controllers;

import com.losing.weight.presentation.search.basket.db.BasketEntity;

public interface ExpandableClickListener {
  void click(BasketEntity basketEntity, boolean isNeedSave);
  void open(BasketEntity basketEntity);
}

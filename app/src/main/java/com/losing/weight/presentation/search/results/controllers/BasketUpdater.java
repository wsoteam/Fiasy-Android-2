package com.losing.weight.presentation.search.results.controllers;

public interface BasketUpdater {
  void getCurrentSize(int size);
  void handleUndoCard(boolean isShow);
  int getCurrentEating();
}

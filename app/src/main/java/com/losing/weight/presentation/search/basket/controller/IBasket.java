package com.losing.weight.presentation.search.basket.controller;

public interface IBasket {
  void cancelRemove();
  void moveItem(int from, int to);
  void saveFood(String date);
  void emergencyDelete();
}

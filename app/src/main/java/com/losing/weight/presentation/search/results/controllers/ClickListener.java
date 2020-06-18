package com.losing.weight.presentation.search.results.controllers;

public interface ClickListener {
  void click(int position, boolean isNeedSave);
  void open(int position);
}

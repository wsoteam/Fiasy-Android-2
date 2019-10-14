package com.wsoteam.diet.presentation.search.results;

import com.arellomobile.mvp.MvpView;

public interface ResultsView extends MvpView {
  void sendClearSearchField();
  void sendSearchQuery(String query);
}

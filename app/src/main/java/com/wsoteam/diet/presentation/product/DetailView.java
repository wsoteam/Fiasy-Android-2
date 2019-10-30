package com.wsoteam.diet.presentation.product;

import com.arellomobile.mvp.MvpView;

public interface DetailView extends MvpView {
  void refreshCalculate(String kcal, String prot, String carbo, String fat);
}

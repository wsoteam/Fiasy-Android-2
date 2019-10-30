package com.wsoteam.diet.presentation.product;

import com.arellomobile.mvp.MvpView;
import com.wsoteam.diet.presentation.search.basket.db.BasketEntity;

public interface DetailView extends MvpView {
  void fillFields(String name, String fats, String carbo, String prot, String brand,
                  double sugar, double saturatedFats, double monoUnSaturatedFats, double polyUnSaturatedFats,
                  double cholesterol, double cellulose, double sodium, double pottassium);
  void refreshCalculate(String kcal, String prot, String carbo, String fat);
}

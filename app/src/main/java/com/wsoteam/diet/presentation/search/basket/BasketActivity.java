package com.wsoteam.diet.presentation.search.basket;

import android.os.Bundle;
import com.arellomobile.mvp.MvpAppCompatActivity;
import com.wsoteam.diet.R;

public class BasketActivity extends MvpAppCompatActivity implements BasketView {
  private BasketPresenter presenter;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_basket);
    presenter = new BasketPresenter();
    presenter.attachView(this);
  }
}

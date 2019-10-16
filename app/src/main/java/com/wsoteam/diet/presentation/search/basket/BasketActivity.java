package com.wsoteam.diet.presentation.search.basket;

import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.arellomobile.mvp.MvpAppCompatActivity;
import com.wsoteam.diet.R;

public class BasketActivity extends MvpAppCompatActivity implements BasketView {
  @BindView(R.id.rvBasket) RecyclerView rvBasket;
  private BasketPresenter presenter;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_basket);
    ButterKnife.bind(this);
    presenter = new BasketPresenter();
    presenter.attachView(this);
    rvBasket.setLayoutManager(new LinearLayoutManager(this));
    updateUI();
  }

  private void updateUI() {

  }
}

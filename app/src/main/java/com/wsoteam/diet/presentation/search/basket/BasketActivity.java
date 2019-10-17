package com.wsoteam.diet.presentation.search.basket;

import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.arellomobile.mvp.MvpAppCompatActivity;
import com.wsoteam.diet.R;
import com.wsoteam.diet.presentation.search.basket.controller.BasketAdapter;
import com.wsoteam.diet.presentation.search.basket.db.BasketEntity;
import java.util.List;

public class BasketActivity extends MvpAppCompatActivity implements BasketView {
  @BindView(R.id.rvBasket) RecyclerView rvBasket;
  private BasketPresenter presenter;
  private BasketAdapter adapter;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_basket);
    ButterKnife.bind(this);
    presenter = new BasketPresenter(this);
    presenter.attachView(this);
    rvBasket.setLayoutManager(new LinearLayoutManager(this));
    presenter.getBasketLists();
  }

  @Override public void getSortedData(List<List<BasketEntity>> allFood) {
    adapter = new BasketAdapter(allFood, getResources().getStringArray(R.array.srch_eating));
    rvBasket.setAdapter(adapter);
  }
}

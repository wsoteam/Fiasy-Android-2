package com.wsoteam.diet.presentation.search.basket;

import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.TextView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.arellomobile.mvp.MvpAppCompatActivity;
import com.wsoteam.diet.R;
import com.wsoteam.diet.presentation.search.basket.controller.BasketAdapter;
import com.wsoteam.diet.presentation.search.basket.db.BasketEntity;
import com.wsoteam.diet.presentation.search.results.controllers.BasketUpdater;
import java.util.List;

public class BasketActivity extends MvpAppCompatActivity implements BasketView {
  @BindView(R.id.rvBasket) RecyclerView rvBasket;
  @BindView(R.id.tvCounter) TextView tvCounter;
  @BindView(R.id.cvBasket) CardView cvBasket;
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
    adapter = new BasketAdapter(allFood, getResources().getStringArray(R.array.srch_eating), new BasketUpdater(){
      @Override public void getCurrentSize(int size) {
        updateBasket(size);
      }
    });
    rvBasket.setAdapter(adapter);
  }

  private void updateBasket(int size) {
    if (size > 0) {
      if (cvBasket.getVisibility() == View.GONE) {
        cvBasket.setVisibility(View.VISIBLE);
      }
      tvCounter.setText(getPaintedString(size));
    } else if (cvBasket.getVisibility() == View.VISIBLE) {
      onBackPressed();
    }
  }

  private Spannable getPaintedString(int size) {
    String string = getResources().getString(R.string.srch_basket_card, size);
    int positionPaint = string.indexOf(" ") + 1;
    Spannable spannable = new SpannableString(string);
    spannable.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.srch_painted_string)), positionPaint,
        string.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    return spannable;
  }
}

package com.wsoteam.diet.presentation.search.results;

import android.graphics.Paint;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import com.arellomobile.mvp.MvpAppCompatFragment;
import com.wsoteam.diet.App;
import com.wsoteam.diet.R;
import com.wsoteam.diet.common.Analytics.Events;
import com.wsoteam.diet.common.networking.food.FoodResultAPI;
import com.wsoteam.diet.common.networking.food.FoodSearch;
import com.wsoteam.diet.common.networking.food.HeaderObj;
import com.wsoteam.diet.common.networking.food.ISearchResult;
import com.wsoteam.diet.common.networking.food.POJO.Result;
import com.wsoteam.diet.presentation.search.basket.db.BasketDAO;
import com.wsoteam.diet.presentation.search.basket.db.BasketEntity;
import com.wsoteam.diet.presentation.search.results.controllers.BasketUpdater;
import com.wsoteam.diet.presentation.search.results.controllers.ResultAdapter;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import java.util.ArrayList;
import java.util.List;

public class ResultsFragment extends MvpAppCompatFragment implements ResultsView {
  ResultsPresenter presenter;
  Unbinder unbinder;
  @BindView(R.id.rvBlocks) RecyclerView rvBlocks;
  @BindView(R.id.ivSearchImage) ImageView ivSearchImage;
  @BindView(R.id.tvTitleEmptySearch) TextView tvTitleEmptySearch;
  @BindView(R.id.tvTextEmptySearch) TextView tvTextEmptySearch;
  @BindView(R.id.btnAddCustomFood) Button btnAddCustomFood;
  @BindView(R.id.tvCounter) TextView tvCounter;
  @BindView(R.id.cvBasket) CardView cvBasket;
  private FoodResultAPI foodResultAPI = FoodSearch.getInstance().getFoodSearchAPI();
  private String searchString = "";
  private int RESPONSE_LIMIT = 100;
  private ResultAdapter itemAdapter;
  private BasketDAO basketDAO = App.getInstance().getFoodDatabase().basketDAO();

  @Override public void sendClearSearchField() {

  }

  @Override public void sendSearchQuery(String query) {
    search(searchString.trim());
    Events.logSearch(searchString);
    searchString = query;
  }

  @Nullable @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_results, container, false);
    unbinder = ButterKnife.bind(this, view);
    presenter = new ResultsPresenter();
    presenter.attachView(this);
    updateUI();
    showStartUI();
    return view;
  }

  private void updateUI() {
    List<ISearchResult> foods = new ArrayList<>();
    itemAdapter = new ResultAdapter(foods, getActivity(), new ArrayList<BasketEntity>(),
        new BasketUpdater() {
          @Override public void getCurrentSize(int size) {
            updateBasket(size);
          }
        });
    rvBlocks.setLayoutManager(new LinearLayoutManager(getContext()));
    rvBlocks.setAdapter(itemAdapter);
  }

  private void updateBasket(int size) {
    if (size > 0) {
      if (cvBasket.getVisibility() == View.GONE) {
        cvBasket.setVisibility(View.VISIBLE);
      }
      tvCounter.setText(getPaintedString(size));
    } else if (cvBasket.getVisibility() == View.VISIBLE) {
      cvBasket.setVisibility(View.GONE);
    }
  }

  private Spannable getPaintedString(int size) {
    String string = getActivity().getResources().getString(R.string.srch_basket_card, size);
    int positionPaint = string.indexOf(" ") + 1;
    Spannable spannable = new SpannableString(string);
    spannable.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.srch_painted_string)), positionPaint,
        string.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    spannable.setSpan(new UnderlineSpan(), 0, string.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    return spannable;
  }

  private void hideMessageUI() {
    ivSearchImage.setVisibility(View.GONE);
    tvTitleEmptySearch.setVisibility(View.GONE);
    tvTextEmptySearch.setVisibility(View.GONE);
    btnAddCustomFood.setVisibility(View.GONE);
  }

  private void showNoFind() {
    ivSearchImage.setVisibility(View.VISIBLE);
    tvTitleEmptySearch.setVisibility(View.VISIBLE);
    tvTextEmptySearch.setVisibility(View.VISIBLE);
    btnAddCustomFood.setVisibility(View.VISIBLE);
  }

  private void showStartUI() {
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    unbinder.unbind();
  }

  private void search(String searchString) {
    foodResultAPI
        .getResponse(RESPONSE_LIMIT, 30, "Хлеб Тостовый")
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(t -> refreshAdapter(t.getResults()), Throwable::printStackTrace);
  }

  private void refreshAdapter(List<Result> list) {
    Single.fromCallable(() -> {
      List<BasketEntity> savedItems = getSavedItems();
      return savedItems;
    })
        .subscribeOn(Schedulers.computation())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(t -> updateAdapter(list, t), Throwable::printStackTrace);
    if (list.size() > 0) {
      hideMessageUI();
    } else {
      showNoFind();
    }
  }

  private List<BasketEntity> getSavedItems() {
    List<BasketEntity> entities = basketDAO.getAll();
    return entities;
  }

  private void updateAdapter(List<Result> t, List<BasketEntity> basketEntities) {
    rvBlocks.setAdapter(itemAdapter = new ResultAdapter(createHeadersArray(t), getActivity(),
        basketEntities, new BasketUpdater() {
      @Override public void getCurrentSize(int size) {
        updateBasket(size);
      }
    }));
  }

  private List<ISearchResult> createHeadersArray(List<Result> t) {
    List<ISearchResult> iSearchResults = new ArrayList<>();
    iSearchResults.add(new HeaderObj("kek", true));
    for (int i = 0; i < t.size(); i++) {
      iSearchResults.add(t.get(i));
    }
    return iSearchResults;
  }

  @OnClick({ R.id.btnAddCustomFood, R.id.tvCounter, R.id.tvAddToBasket })
  public void onViewClicked(View view) {
    switch (view.getId()) {
      case R.id.btnAddCustomFood:
        break;
      case R.id.tvCounter:
        break;
      case R.id.tvAddToBasket:
        break;
    }
  }
}

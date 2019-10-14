package com.wsoteam.diet.presentation.search.results;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.arellomobile.mvp.MvpAppCompatFragment;
import com.bumptech.glide.Glide;
import com.wsoteam.diet.BranchOfAnalyzer.Fragments.FragmentSearch;
import com.wsoteam.diet.R;
import com.wsoteam.diet.common.networking.food.FoodResultAPI;
import com.wsoteam.diet.common.networking.food.FoodSearch;
import com.wsoteam.diet.common.networking.food.POJO.Result;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import java.util.ArrayList;
import java.util.List;

public class ResultsFragment extends MvpAppCompatFragment implements ResultsView {
  ResultsPresenter presenter;
  Unbinder unbinder;
  private FoodResultAPI foodResultAPI = FoodSearch.getInstance().getFoodSearchAPI();
  private String searchString = "";
  private int RESPONSE_LIMIT = 100;

  @Nullable @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_results, container, false);
    unbinder = ButterKnife.bind(this, view);
    presenter = new ResultsPresenter();
    presenter.attachView(this);
    return view;
  }
  private void updateUI() {
    /*List<Result> foods = new ArrayList<>();
    itemAdapter = new FragmentSearch.ItemAdapter(foods);
    rvListOfSearchResponse.setLayoutManager(new LinearLayoutManager(getContext()));
    rvListOfSearchResponse.setAdapter(itemAdapter);*/
  }

  private void hideMessageUI() {
    /*ivSearchImage.setVisibility(View.GONE);
    tvTextAddFavorite.setVisibility(View.GONE);
    tvTitleFavoriteAdd.setVisibility(View.GONE);
    btnAddFavorite.setVisibility(View.GONE);*/
  }

  private void showNoFind() {
    /*Glide.with(getActivity()).load(R.drawable.ic_no_find).into(ivSearchImage);
    tvTitleFavoriteAdd.setText(getActivity().getResources().getString(R.string.title_no_find_food));
    tvTextAddFavorite.setText(getActivity().getResources().getString(R.string.text_no_find_food));
    ivSearchImage.setVisibility(View.VISIBLE);
    tvTextAddFavorite.setVisibility(View.VISIBLE);
    tvTitleFavoriteAdd.setVisibility(View.VISIBLE);
    btnAddFavorite.setVisibility(View.VISIBLE);*/
  }

  private void showStartUI() {
    /*Glide.with(getActivity()).load(R.drawable.ic_start_search).into(ivSearchImage);
    tvTextAddFavorite.setText(getActivity().getResources().getString(R.string.text_start_search));
    ivSearchImage.setVisibility(View.VISIBLE);
    tvTextAddFavorite.setVisibility(View.VISIBLE);*/
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    unbinder.unbind();
  }

  private void search(String searchString) {
    foodResultAPI
        .getResponse(RESPONSE_LIMIT, 0, searchString)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(t -> refreshAdapter(t.getResults()), Throwable::printStackTrace);
  }

  private void refreshAdapter(List<Result> t) {
    if (rvListOfSearchResponse == null) {
      return;
    }
    rvListOfSearchResponse.setAdapter(itemAdapter = new FragmentSearch.ItemAdapter(t));
    if (t.size() > 0) {
      hideMessageUI();
    } else {
      showNoFind();
    }
  }
}

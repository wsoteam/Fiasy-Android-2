package com.losing.weight.articles;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.losing.weight.R;
import com.losing.weight.Sync.UserDataHolder;
import com.losing.weight.Sync.WorkWithFirebaseDB;
import com.losing.weight.ads.FiasyAds;
import com.losing.weight.model.OpenArticles;
import java.util.Date;
import java.util.HashMap;

public class BurlakovAuthorFragment extends Fragment {

  private final String author_id = "burlakov";
  private View.OnClickListener clickListener;

  @Nullable @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.activity_author, container, false);
    ButterKnife.bind(this, view);
    if (getArguments() != null)
    view.findViewById(R.id.button2).setVisibility(getArguments().getBoolean(
        BurlakovAuthorActivity.HIDE_BTN, false) ? View.GONE : View.VISIBLE);
    return view;
  }

  @OnClick(R.id.button2)
  void onClicked(View v){
    try {
      if (UserDataHolder.getUserData() == null ||
          UserDataHolder.getUserData().getArticleSeries() == null ||
          UserDataHolder.getUserData().getArticleSeries().get(author_id) == null) {
        OpenArticles openArticles = new OpenArticles();
        openArticles.setId(author_id);
        openArticles.setDate(new Date().getTime());
        openArticles.setUnlockedArticles(1);
        HashMap<String, OpenArticles> map = new HashMap<>();
        map.put(author_id, openArticles);
        UserDataHolder.getUserData().setArticleSeries(map);
        WorkWithFirebaseDB.addArticleSeries(openArticles);
      }
    } catch (Exception e){
      Log.d("kkk", "onClicked: ", e);
    }
    if (clickListener != null){
      clickListener.onClick(v);
    }else {
      startActivity(new Intent(getContext(), ArticleSeriesActivity.class));
    }
  }

  public void setClickListener(View.OnClickListener clickListener){
    this.clickListener = clickListener;
  }

  @Override
  public void onStop() {
    super.onStop();
    FiasyAds.openInter(getContext());
  }
}

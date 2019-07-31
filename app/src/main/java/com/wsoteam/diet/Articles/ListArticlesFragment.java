package com.wsoteam.diet.Articles;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;


import com.wsoteam.diet.Articles.POJO.Article;
import com.wsoteam.diet.Articles.POJO.ArticlesHolder;
import com.wsoteam.diet.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ListArticlesFragment extends Fragment implements Observer {

  @BindView(R.id.rvArticle) RecyclerView recyclerView;
  @BindView(R.id.toolbar) Toolbar mToolbar;
  private Unbinder unbinder;
  private ListArticlesAdapter adapter;

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    Window window = getActivity().getWindow();
    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
    window.setStatusBarColor(Color.parseColor("#9C5E21"));

    View view = inflater.inflate(R.layout.fragment_list_articles, container, false);
    unbinder = ButterKnife.bind(this, view);

    mToolbar.setTitle("Статьи");
    mToolbar.inflateMenu(R.menu.toolbar_menu);
    mToolbar.setNavigationIcon(R.drawable.back_arrow_icon_white);
    mToolbar.setNavigationOnClickListener(onClickListener);
    mToolbar.setTitleTextColor(0xFFFFFFFF);

    Menu menu = mToolbar.getMenu();
    MenuItem mSearch = menu.findItem(R.id.action_search);
    SearchView mSearchView = (SearchView) mSearch.getActionView();
    mSearchView.setOnQueryTextListener(textListener);

    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

    if (ArticlesHolder.getListArticles() != null) {

      adapter = new ListArticlesAdapter(ArticlesHolder.getListArticles().getListArticles(),
          getActivity());
      recyclerView.setAdapter(adapter);
    } else {

      ArticlesHolder.subscribe(this);
      recyclerView.setAdapter(null);
    }

    return view;
  }

  private View.OnClickListener onClickListener = new View.OnClickListener() {
    @Override public void onClick(View view) {
      getActivity().onBackPressed();
    }
  };

  private SearchView.OnQueryTextListener textListener = new SearchView.OnQueryTextListener() {
    @Override
    public boolean onQueryTextSubmit(String s) {
      return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
      search(s);
      return false;
    }
  };

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    unbinder.unbind();
  }


  private void search(String str){
    String key = str.toLowerCase();
    List<Article> result = new ArrayList<>();
    List<Article> articles = ArticlesHolder.getListArticles().getListArticles();

    if (key.equals("")) {
      recyclerView.setAdapter(adapter);
    } else {
      for (Article article :
          articles) {
        if (article.getTitle().toLowerCase().contains(key)) {
          result.add(article);
        }
      }
      ListArticlesAdapter newAdapter = new ListArticlesAdapter(result, getActivity());
      recyclerView.setAdapter(newAdapter);
    }
  }


  @Override
  public void update(Observable o, Object arg) {
    adapter =
        new ListArticlesAdapter(ArticlesHolder.getListArticles().getListArticles(), getActivity());
    recyclerView.setAdapter(adapter);
    ArticlesHolder.unsubscribe(this);
  }

  @Override
  public void onPause() {
    ArticlesHolder.unsubscribe(this);
    super.onPause();
  }
}

package com.wsoteam.diet.articles;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.wsoteam.diet.Sync.UserDataHolder;
import com.wsoteam.diet.articles.recycler.HorizontalArticlesAdapter;
import com.wsoteam.diet.articles.recycler.ListArticlesAdapter;
import com.wsoteam.diet.articles.recycler.VerticalArticlesAdapter;
import com.wsoteam.diet.model.Article;
import com.wsoteam.diet.articles.POJO.ListArticles;
import com.wsoteam.diet.articles.POJO.SectionArticles;
import com.wsoteam.diet.Config;
import com.wsoteam.diet.R;

import com.wsoteam.diet.model.ApiResult;
import com.wsoteam.diet.model.ArticleViewModel;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import static android.content.Context.MODE_PRIVATE;

public class ListArticlesFragment extends Fragment implements Observer {

  @BindView(R.id.clFailSearch) ConstraintLayout constraintLayout;
  @BindView(R.id.rvArticle) RecyclerView recyclerView;
  @BindView(R.id.toolbar) Toolbar mToolbar;
  private Unbinder unbinder;
  private ListArticlesAdapter adapter;
  private VerticalArticlesAdapter verticalArticlesAdapter;
  private SectionArticles sectionArticles;

  private boolean isSubSection;
  private List<Article> subList;

  private ArticleViewModel model;
  private LiveData<ApiResult<Article>> data;

  private void startDetailActivity(Article article) {

    Intent intent;

    //if (false) {
    if (!checkSubscribe() && article.isPremium()) {
      intent = new Intent(getActivity(), ItemArticleWithoutPremActivity.class);
    } else {
      intent = new Intent(getActivity(), ItemArticleActivity.class);
    }
    intent.putExtra(Config.ARTICLE_INTENT, article.getId());

    getActivity().startActivity(intent);
  }

  private boolean checkSubscribe() {
    SharedPreferences sharedPreferences =
        getActivity().getSharedPreferences(Config.STATE_BILLING, MODE_PRIVATE);
    if (sharedPreferences.getBoolean(Config.STATE_BILLING, false)) {
      return true;
    } else {
      return false;
    }
  }

  HorizontalArticlesAdapter.OnItemClickListener onItemClickListener =
      new HorizontalArticlesAdapter.OnItemClickListener() {
        @Override public void onItemClick(View view, int position, Article article) {
          startDetailActivity(article);
        }

        @Override public void onClickAll(View view, int position, ListArticles listArticles) {
          if (position == 0) return;
          subList = listArticles.getListArticles();
          isSubSection = true;
          adapter.updateData(listArticles.getListArticles());
          recyclerView.setAdapter(adapter);
          mToolbar.setNavigationIcon(R.drawable.back_arrow_icon);
          mToolbar.setTitle(listArticles.getName());
        }

        @Override public void onClickBanner(View view) {
          if (UserDataHolder.getUserData() == null ||
              UserDataHolder.getUserData().getArticleSeries() == null ||
              UserDataHolder.getUserData().getArticleSeries().get("burlakov") == null){
            startActivity(new Intent(getActivity(), BurlakovAuthorActivity.class));
          } else {
            startActivity(new Intent(getActivity(), ArticleSeriesActivity.class));
          }

        }
      };

  private ListArticlesAdapter.OnItemClickListener clickListener =
      new ListArticlesAdapter.OnItemClickListener() {
        @Override public void onItemClick(View view, Article article) {
          startDetailActivity(article);
        }
      };
  private View.OnClickListener onClickListener = new View.OnClickListener() {
    @Override public void onClick(View view) {
      isSubSection = false;
      recyclerView.setAdapter(verticalArticlesAdapter);
      mToolbar.setTitle(getString(R.string.bnv_main_articles));
      mToolbar.setNavigationIcon(null);
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

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    Window window = getActivity().getWindow();
    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
    window.setStatusBarColor(getContext().getResources().getColor(R.color.highlight_line_color));

    adapter = new ListArticlesAdapter(null, clickListener);
    verticalArticlesAdapter = new VerticalArticlesAdapter(null);

    model = ViewModelProviders.of(this).get(ArticleViewModel.class);
    data = model.getData();
    data.observe(this,
        new androidx.lifecycle.Observer<ApiResult<Article>>() {
          @Override
          public void onChanged(ApiResult<Article> articleApiResult) {
            Log.d("kkk", "onChanged: ");
            sectionArticles = new SectionArticles(articleApiResult.getResults(), getContext());
            adapter.updateData(articleApiResult.getResults());
            verticalArticlesAdapter.setData(sectionArticles.getGroups());
            verticalArticlesAdapter.SetOnItemClickListener(onItemClickListener);
          }
        });

    if (model.getData().getValue() != null){
      //Log.d("kkk", "onCreateView: xx");
      //sectionArticles = new SectionArticles(model.getData().getValue().getResults());
      //adapter = new ListArticlesAdapter(model.getData().getValue().getResults(), clickListener);
      //verticalArticlesAdapter = new VerticalArticlesAdapter(sectionArticles.getGroups());
      //verticalArticlesAdapter.SetOnItemClickListener(onItemClickListener);
    }



    View view = inflater.inflate(R.layout.fragment_list_articles, container, false);
    unbinder = ButterKnife.bind(this, view);

    mToolbar.setTitle(getString(R.string.bnv_main_articles));
    mToolbar.inflateMenu(R.menu.menu_article);
    mToolbar.setNavigationIcon(null);
    mToolbar.setNavigationOnClickListener(onClickListener);


    Menu menu = mToolbar.getMenu();
    MenuItem mSearch = menu.findItem(R.id.action_search);
    SearchView mSearchView = (SearchView) mSearch.getActionView();
    mSearchView.setOnQueryTextListener(textListener);

    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    recyclerView.setAdapter(verticalArticlesAdapter);
    return view;
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    unbinder.unbind();
  }

  private void search(String str) {
    String key = str.toLowerCase();
    List<Article> result = new ArrayList<>();
    List<Article> articles;
    if (isSubSection) {
      articles = subList;
    } else {

      if (model.getData().getValue() != null) {
        articles = model.getData().getValue().getResults();
      } else {
        articles = new LinkedList<>();
      }
    }

    if (key.equals("")) {
      constraintLayout.setVisibility(View.INVISIBLE);
      if (isSubSection) {
        adapter.updateData(subList);
        recyclerView.setAdapter(adapter);
      } else {
        recyclerView.setAdapter(verticalArticlesAdapter);
      }
    } else {
      for (Article article :
          articles) {
        if (article.getTitle().toLowerCase().contains(key)
                && article.getCategory().getId() != 4) {
          result.add(article);
        }
      }
      if (result.size() == 0) {
        constraintLayout.setVisibility(View.VISIBLE);
      } else {
        constraintLayout.setVisibility(View.INVISIBLE);
      }
      ListArticlesAdapter newAdapter = new ListArticlesAdapter(result, clickListener);
      recyclerView.setAdapter(newAdapter);
    }
  }

  @Override
  public void update(Observable o, Object arg) {
    verticalArticlesAdapter = new VerticalArticlesAdapter(sectionArticles.getGroups());
    recyclerView.setAdapter(verticalArticlesAdapter);
  }
}

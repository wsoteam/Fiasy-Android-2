package com.losing.weight.articles;

import android.content.Intent;
import android.os.Bundle;
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

import com.google.android.material.appbar.AppBarLayout;
import com.losing.weight.AmplitudaEvents;
import com.losing.weight.Authenticate.POJO.Box;
import com.losing.weight.InApp.ActivitySubscription;
import com.losing.weight.Sync.UserDataHolder;
import com.losing.weight.ads.AdVH;
import com.losing.weight.ads.AdWorker;
import com.losing.weight.ads.AdWrapperAdapter;
import com.losing.weight.ads.FiasyAds;
import com.losing.weight.articles.recycler.HorizontalArticlesAdapter;
import com.losing.weight.articles.recycler.ListArticlesAdapter;
import com.losing.weight.articles.recycler.VerticalArticlesAdapter;
import com.losing.weight.common.Analytics.EventProperties;
import com.losing.weight.model.Article;
import com.losing.weight.articles.POJO.ListArticles;
import com.losing.weight.articles.POJO.SectionArticles;
import com.losing.weight.Config;
import com.losing.weight.R;

import com.losing.weight.model.ApiResult;
import com.losing.weight.model.ArticleViewModel;
import com.losing.weight.presentation.auth.AuthUtil;
import com.losing.weight.utils.RecyclerExtKt;
import com.losing.weight.utils.Subscription;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class ListArticlesFragment extends Fragment implements Observer {

  private final int FREE_SERIES_ARTICLE = 1;

  @BindView(R.id.clFailSearch) ConstraintLayout constraintLayout;
  @BindView(R.id.rvArticle) RecyclerView recyclerView;
  @BindView(R.id.toolbar) Toolbar mToolbar;
  @BindView(R.id.appbar) AppBarLayout appBarLayout;
  @BindView(R.id.articlePremium) View articlePremium;

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
//
//    if (!Subscription.check(getContext()) && article.isPremium()) {
//      intent = new Intent(getActivity(), ItemArticleWithoutPremActivity.class);
//    } else {
      intent = new Intent(getActivity(), ItemArticleActivity.class);
//    }
    intent.putExtra(Config.ARTICLE_INTENT, article.getId());

    startActivity(intent);
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
          AdWrapperAdapter wrapperAdapter = new AdWrapperAdapter(adapter, 5, AdVH::new);
          wrapperAdapter.insertAds(AdWorker.INSTANCE.getNativeAds());
          recyclerView.setAdapter(wrapperAdapter);

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
          (view, article) -> startDetailActivity(article);

  private View.OnClickListener onClickListener = new View.OnClickListener() {
    @Override public void onClick(View view) {
      isSubSection = false;
      recyclerView.setAdapter(verticalArticlesAdapter);
      mToolbar.setTitle(getString(R.string.bnv_main_articles));
      mToolbar.setNavigationIcon(null);
    }
  };

  @Override
  public void onResume() {
    super.onResume();
    Subscription.setVisibility(articlePremium);
  }

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

    View view = inflater.inflate(R.layout.fragment_list_articles, container, false);
    unbinder = ButterKnife.bind(this, view);
    mToolbar.setTitle(getString(R.string.bnv_main_articles));
    mToolbar.inflateMenu(R.menu.menu_article);
    mToolbar.setNavigationIcon(null);
    mToolbar.setNavigationOnClickListener(onClickListener);

    return view;
  }


  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    Menu menu = mToolbar.getMenu();
    MenuItem mSearch = menu.findItem(R.id.action_search);
    SearchView mSearchView = (SearchView) mSearch.getActionView();
    mSearchView.setOnQueryTextListener(textListener);

    AuthUtil.Companion.prepareLogInView(getContext(), view.findViewById(R.id.articleLogIn));

    adapter = new ListArticlesAdapter(null, clickListener);
    verticalArticlesAdapter = new VerticalArticlesAdapter(null);

    model = ViewModelProviders.of(this).get(ArticleViewModel.class);
    data = model.getData();
    data.observe(getViewLifecycleOwner(),
            articleApiResult -> {
              sectionArticles = new SectionArticles(articleApiResult.getResults(), getContext());
              adapter.updateData(articleApiResult.getResults());
              verticalArticlesAdapter.updateData(sectionArticles.getGroups());
              verticalArticlesAdapter.SetOnItemClickListener(onItemClickListener);
            });

    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    recyclerView.setAdapter(verticalArticlesAdapter);
    RecyclerExtKt.appBarLiftable(recyclerView, appBarLayout);

    articlePremium.setOnClickListener(v -> {

      Box box = new Box();
        box.setSubscribe(false);
        box.setOpenFromPremPart(true);
        box.setOpenFromIntrodaction(false);
        box.setComeFrom(AmplitudaEvents.view_prem_content);
        box.setBuyFrom(EventProperties.trial_from_articles); // TODO проверить правильность флагов
        Intent intent = new Intent(getContext(), ActivitySubscription.class).putExtra(Config.TAG_BOX, box);
        startActivity(intent);

    });

  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    FiasyAds.openInter(getContext());
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

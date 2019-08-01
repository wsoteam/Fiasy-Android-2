package com.wsoteam.diet.Articles;

import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import com.wsoteam.diet.Articles.POJO.Article;
import com.wsoteam.diet.Articles.POJO.ArticlesHolder;
import com.wsoteam.diet.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ListArticlesFragment extends Fragment implements Observer {

    @BindView(R.id.rvArticle) RecyclerView recyclerView;
    @BindView(R.id.etArticleItem) EditText searchEditText;
    private Unbinder unbinder;
    private ListArticlesAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Window window = getActivity().getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.parseColor("#9C5E21"));

        View view = inflater.inflate(R.layout.fragment_list_articles, container, false);
        unbinder = ButterKnife.bind(this, view);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        if (ArticlesHolder.getListArticles() != null) {

            adapter = new ListArticlesAdapter(ArticlesHolder.getListArticles().getListArticles(), getActivity());
            recyclerView.setAdapter(adapter);
            activateSearch();

        } else {

            ArticlesHolder.subscribe(this);
            recyclerView.setAdapter(null);
        }

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @OnClick(R.id.ArticleBackButton)
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ArticleBackButton:
                getActivity().onBackPressed();
                break;
        }
    }

    private void activateSearch() {

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String key = s.toString().toLowerCase();
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
            public void afterTextChanged(Editable s) {
            }
        });

    }

    @Override
    public void update(Observable o, Object arg) {
        adapter = new ListArticlesAdapter(ArticlesHolder.getListArticles().getListArticles(), getActivity());
        recyclerView.setAdapter(adapter);
        activateSearch();
        ArticlesHolder.unsubscribe(this);
    }

    @Override
    public void onPause() {
        ArticlesHolder.unsubscribe(this);
        super.onPause();
    }
}

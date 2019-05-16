package com.wsoteam.diet.Articles;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wsoteam.diet.Articles.POJO.Article;
import com.wsoteam.diet.Articles.POJO.ArticlesHolder;
import com.wsoteam.diet.Articles.POJO.Factory;
import com.wsoteam.diet.Articles.POJO.ListArticles;
import com.wsoteam.diet.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ListArticlesFragment extends Fragment {

    @BindView(R.id.rvArticle) RecyclerView recyclerView;
    @BindView(R.id.etArticleItem) EditText searchEditText;
    private Unbinder unbinder;
    private ListArticlesAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_articles, container, false);
        unbinder = ButterKnife.bind(this, view);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        updateUI();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void updateUI() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("ARTICLES");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                ListArticles listArticles = dataSnapshot.getValue(ListArticles.class);
                ArticlesHolder articlesHolder = new ArticlesHolder();
                articlesHolder.bind(listArticles);

                adapter = new ListArticlesAdapter(ArticlesHolder.getListArticles().getListArticles(), getActivity());
                recyclerView.setAdapter(adapter);

                activateSearch();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @OnClick(R.id.ArticleBackButton)
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.ArticleBackButton:
                getActivity().onBackPressed();
                break;
        }
    }

    private void activateSearch(){

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
                    for (Article article:
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

}

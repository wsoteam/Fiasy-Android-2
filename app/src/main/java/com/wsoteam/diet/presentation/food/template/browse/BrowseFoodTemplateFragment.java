package com.wsoteam.diet.presentation.food.template.browse;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.wsoteam.diet.BranchOfAnalyzer.TabsFragment;
import com.wsoteam.diet.BranchOfAnalyzer.templates.POJO.FoodTemplate;
import com.wsoteam.diet.R;
import com.wsoteam.diet.presentation.food.adapter.FoodTemplateAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BrowseFoodTemplateFragment  extends MvpAppCompatFragment
        implements BrowseFoodTemplateView, TabsFragment {

    @InjectPresenter
    BrowseFoodTemplatePresenter presenter;

    @BindView(R.id.recycler) RecyclerView recyclerView;
    @BindView(R.id.layoutWithButton) ConstraintLayout layoutWithBtn;

    FoodTemplateAdapter adapter;

    @ProvidePresenter
    BrowseFoodTemplatePresenter providePresenter() {
        return new BrowseFoodTemplatePresenter(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_browse_food_template, container, false);
        ButterKnife.bind(this, view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new FoodTemplateAdapter(getContext(), presenter);
        recyclerView.setAdapter(adapter);

        return view;
    }

    @OnClick({R.id.btnAddTemplate})
    public void onViewClicked(View view) {
       presenter.addTemplate();
    }


    @Override
    public void showProgress(boolean show) {

    }

    @Override
    public void showMessage(String message) {

    }

    @Override
    public void sendString(String searchString) {
        presenter.search(searchString);
    }

    @Override
    public void setData(List<FoodTemplate> foodTemplates) {
        adapter.setListContent(foodTemplates);
    }

    @Override
    public void showBtn() {
        layoutWithBtn.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideBtn() {
        layoutWithBtn.setVisibility(View.GONE);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isResumed()) {
            presenter.initAdapter();
            Log.d("kkk", "setUserVisibleHint: ");
        }
    }

}

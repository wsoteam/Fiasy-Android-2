package com.wsoteam.diet.presentation.plans;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.wsoteam.diet.DietPlans.POJO.DietPlansHolder;
import com.wsoteam.diet.R;
import com.wsoteam.diet.presentation.plans.adapter.VerticalBrowsePlansAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BrowsePlansFragment extends MvpAppCompatFragment implements BrowsePlansView {

    @BindView(R.id.recycler) RecyclerView recyclerView;
    @InjectPresenter
    BrowsePlansPresenter presenter;

    VerticalBrowsePlansAdapter adapter;

    @ProvidePresenter
    BrowsePlansPresenter providePresenter() {
        return new BrowsePlansPresenter();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_browse_plans,
                container, false);
        ButterKnife.bind(this, view);

        adapter = new VerticalBrowsePlansAdapter(DietPlansHolder.get().getListGroups());

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void showProgress(boolean show) {

    }

    @Override
    public void showMessage(String message) {

    }
}

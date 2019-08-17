package com.wsoteam.diet.presentation.plans.browse;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.wsoteam.diet.Config;
import com.wsoteam.diet.DietPlans.POJO.DietPlan;
import com.wsoteam.diet.DietPlans.POJO.DietPlansHolder;
import com.wsoteam.diet.R;
import com.wsoteam.diet.presentation.plans.adapter.HorizontalBrowsePlansAdapter;
import com.wsoteam.diet.presentation.plans.adapter.VerticalBrowsePlansAdapter;
import com.wsoteam.diet.presentation.plans.detail.DetailPlansActivity;
import com.wsoteam.diet.presentation.plans.detail.blocked.BlockedDetailPlansActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BrowsePlansFragment extends MvpAppCompatFragment implements BrowsePlansView {

    @BindView(R.id.toolbar) Toolbar mToolbar;
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

        mToolbar.setTitle(getString(R.string.plans));
        mToolbar.setNavigationIcon(R.drawable.back_arrow_icon_white);
        mToolbar.setNavigationOnClickListener(navigationListener);
        adapter = new VerticalBrowsePlansAdapter(DietPlansHolder.get().getListGroups());
        adapter.SetOnItemClickListener(onItemClickListener);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        return view;
    }

    View.OnClickListener navigationListener = new View.OnClickListener() {
        @Override public void onClick(View v) {
            getActivity().onBackPressed();
        }
    };

    HorizontalBrowsePlansAdapter.OnItemClickListener onItemClickListener = new HorizontalBrowsePlansAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position, DietPlan dietPlan) {
            Intent intent = new Intent(getContext(), DetailPlansActivity.class);
            intent.putExtra(Config.DIETS_PLAN_INTENT, dietPlan);
            startActivity(intent);
        }

        @Override
        public void onItemLongClick(View view, int position, DietPlan dietPlan) {
            Intent intent = new Intent(getContext(), BlockedDetailPlansActivity.class);
            intent.putExtra(Config.DIETS_PLAN_INTENT, dietPlan);
            startActivity(intent);
        }
    };

    @Override
    public void showProgress(boolean show) {

    }

    @Override
    public void showMessage(String message) {

    }
}

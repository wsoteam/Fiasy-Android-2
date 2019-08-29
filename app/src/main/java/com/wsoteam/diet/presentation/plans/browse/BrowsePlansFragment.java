package com.wsoteam.diet.presentation.plans.browse;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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
import com.wsoteam.diet.DietPlans.POJO.DietsList;
import com.wsoteam.diet.R;
import com.wsoteam.diet.Sync.UserDataHolder;
import com.wsoteam.diet.presentation.plans.adapter.HorizontalBrowsePlansAdapter;
import com.wsoteam.diet.presentation.plans.adapter.VerticalBrowsePlansAdapter;
import com.wsoteam.diet.presentation.plans.detail.DetailPlansActivity;
import com.wsoteam.diet.presentation.plans.detail.blocked.BlockedDetailPlansActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import java.util.LinkedList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class BrowsePlansFragment extends MvpAppCompatFragment implements BrowsePlansView {

    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.recycler) RecyclerView recyclerView;
    @InjectPresenter
    BrowsePlansPresenter presenter;

    VerticalBrowsePlansAdapter adapter;

    public static String currentPlanProperti = "CURRENT_PLAN";

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

        getActivity().getWindow().setStatusBarColor(Color.parseColor("#9E5203"));
        mToolbar.setTitle(getString(R.string.plans));
        mToolbar.setNavigationIcon(R.drawable.back_arrow_icon_white);
        mToolbar.setNavigationOnClickListener(navigationListener);

        adapter = new VerticalBrowsePlansAdapter(prepareList());
        adapter.SetOnItemClickListener(onItemClickListener);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        return view;
    }

    private List<DietsList> prepareList(){

        List<DietsList> listGroups = DietPlansHolder.get().getListGroups();
        if (listGroups.get(0).getProperties().equals(currentPlanProperti)){
            listGroups.remove(0);
        }

        if (UserDataHolder.getUserData().getPlan() != null) {
            DietsList dietsList = new DietsList();
            dietsList.setName("Мой план питания");
            dietsList.setProperties(currentPlanProperti);
            List<DietPlan> plan = new LinkedList<>();
            plan.add(UserDataHolder.getUserData().getPlan());

            dietsList.setDietPlans(plan);

            listGroups.add(0, dietsList);
        }
        return listGroups;
    }

    View.OnClickListener navigationListener = new View.OnClickListener() {
        @Override public void onClick(View v) {
            getActivity().onBackPressed();
        }
    };

    HorizontalBrowsePlansAdapter.OnItemClickListener onItemClickListener = new HorizontalBrowsePlansAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position, DietPlan dietPlan) {
            Intent intent;
            if (!checkSubscribe() && dietPlan.isPremium()){
                intent = new Intent(getContext(), BlockedDetailPlansActivity.class);
            }else {
                intent = new Intent(getContext(), DetailPlansActivity.class);
            }

            intent.putExtra(Config.DIETS_PLAN_INTENT, dietPlan);
            startActivity(intent);
        }

        @Override
        public void onItemLongClick(View view, int position, DietPlan dietPlan) {
            //Intent intent = new Intent(getContext(), DetailPlansActivity.class);
            //intent.putExtra(Config.DIETS_PLAN_INTENT, dietPlan);
            //startActivity(intent);
        }
    };

    @Override
    public void showProgress(boolean show) {

    }

    @Override
    public void showMessage(String message) {

    }

    private boolean checkSubscribe() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Config.STATE_BILLING, MODE_PRIVATE);
        if (sharedPreferences.getBoolean(Config.STATE_BILLING, false)) {
            return true;
        } else {
            return false;
        }
    }

    @Override public void onResume() {
        super.onResume();
        adapter.updateList(prepareList());
    }
}

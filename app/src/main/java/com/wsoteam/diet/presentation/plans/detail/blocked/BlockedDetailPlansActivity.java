package com.wsoteam.diet.presentation.plans.detail.blocked;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.squareup.picasso.Picasso;
import com.wsoteam.diet.Config;
import com.wsoteam.diet.DietPlans.POJO.DietPlan;
import com.wsoteam.diet.R;
import com.wsoteam.diet.di.CiceroneModule;
import com.wsoteam.diet.presentation.global.BaseActivity;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.text.TextUtils.concat;

public class BlockedDetailPlansActivity extends BaseActivity implements BlockedDetailPlansView {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.ivDietsPlan) ImageView ivDietsPlan;
    @BindView(R.id.tvPlansName) TextView tvPlansName;
    @BindView(R.id.tvPlansRecipes) TextView recipes;
    @BindView(R.id.tvTime) TextView days;
    @BindView(R.id.tvTimeCount) TextView dayAfterStart;

    @InjectPresenter
    BlockedDetailPlansPresenter presenter;

    @ProvidePresenter
    BlockedDetailPlansPresenter providePresenter(){
        return new BlockedDetailPlansPresenter(CiceroneModule.router(), getApplicationContext());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blocked_detail_plans);
        ButterKnife.bind(this);

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().setStatusBarColor(Color.parseColor("#32000000"));

        toolbar.inflateMenu(R.menu.diet_plans_menu);
        toolbar.setNavigationOnClickListener(navigationListener);
        toolbar.setOverflowIcon(getDrawable(R.drawable.ic_more));

        Menu menu = toolbar.getMenu();
        MenuItem shareMenu = menu.findItem(R.id.mShare);
        MenuItem leaveMenu = menu.findItem(R.id.mLeave);
        invalidateOptionsMenu();
        leaveMenu.setVisible(false);

        shareMenu.setOnMenuItemClickListener(menuListener);
        leaveMenu.setOnMenuItemClickListener(menuListener);

    }

    View.OnClickListener navigationListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            presenter.clickedClose();
        }
    };

    MenuItem.OnMenuItemClickListener menuListener = new MenuItem.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.mLeave: {
                    //presenter.clickedLeave();
                    return true;
                }
                case R.id.mShare:{
                    presenter.clickedShare();
                    return true;
                }
                default:
                    return false;
            }
        }
    };

    @OnClick({R.id.goPrem})
    void onClicked(){
        presenter.clickedPremButton();
    }

    @Override
    public void showProgress(boolean show) {

    }

    @Override
    public void showMessage(String message) {

    }

    @Override
    public void getDietPlan() {
        DietPlan dietPlan = (DietPlan)getIntent().getSerializableExtra(Config.DIETS_PLAN_INTENT);
        presenter.setDietPlan(dietPlan);
    }

    @Override
    public void showData(DietPlan dietPlan){

        days.setText(concat(dietPlan.getCountDays() + "", " ", getResources().getQuantityString(R.plurals.day_plurals, dietPlan.getCountDays())));
        recipes.setText(concat(presenter.getRecipes().size() + "", " ",
                getResources().getQuantityString(R.plurals.recipe_plurals, presenter.getRecipes().size())));
        tvPlansName.setText(dietPlan.getName());
        dayAfterStart.setText(String.format(getString(R.string.vertical_detail_plan_adapter_day_of_days),
                dietPlan.getDaysAfterStart() > 0 ? dietPlan.getDaysAfterStart() : 0, dietPlan.getCountDays()));
        Picasso.get()
                .load(dietPlan.getUrlImage())
                .into(ivDietsPlan);
    }

    @Override public void sharePlan(String str) {
        Intent i = new Intent(
            android.content.Intent.ACTION_SEND);
        i.setType("text/plain");
        i.putExtra(android.content.Intent.EXTRA_TEXT, str);
        startActivity(
            Intent.createChooser(i, getResources().getString(R.string.titleShareDialogPlan)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        getDietPlan();
    }
}

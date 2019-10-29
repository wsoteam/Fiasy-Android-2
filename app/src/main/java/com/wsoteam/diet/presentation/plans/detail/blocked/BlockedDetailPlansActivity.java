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
import com.bumptech.glide.Glide;
import com.wsoteam.diet.Config;
import com.wsoteam.diet.DietPlans.POJO.DietPlan;
import com.wsoteam.diet.R;
import com.wsoteam.diet.di.CiceroneModule;
import com.wsoteam.diet.presentation.global.BaseActivity;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BlockedDetailPlansActivity extends BaseActivity implements BlockedDetailPlansView {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.ivBlockedPlans) ImageView ivPlans;
    @BindView(R.id.tvPlansName) TextView tvName;
    @BindView(R.id.tvTime) TextView tvTime;
    @BindView(R.id.tvRecipes) TextView tvRecipes;
    @BindView(R.id.tvRecipesText) TextView tvRecipesTxt;
    @BindView(R.id.tvUsers) TextView tvUsers;
    @BindView(R.id.tvTimeText) TextView tvTimeTxt;


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

    @OnClick({R.id.btnGetSubscription})
    void onClicked(View view){
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
        tvName.setText(dietPlan.getName());
        tvTime.setText(dietPlan.getCountDays() + "");
        tvTimeTxt.setText(getResources().getQuantityString(R.plurals.day_plurals, dietPlan.getCountDays()));
        tvRecipes.setText(presenter.getRecipes().size() + "");
        tvRecipesTxt.setText(getResources().getQuantityString(R.plurals.recipe_plurals, presenter.getRecipes().size()));
        tvUsers.setText("475");

        Glide.with(this)
                .load(dietPlan.getUrlImage())
                .into(ivPlans);
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

package com.wsoteam.diet.presentation.plans.detail;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.bumptech.glide.Glide;
import com.wsoteam.diet.Config;
import com.wsoteam.diet.DietPlans.POJO.DietPlan;
import com.wsoteam.diet.R;
import com.wsoteam.diet.presentation.global.BaseActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjection;

public class DetailPlansActivity extends BaseActivity implements DetailPlansView {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.ivDietsPlan) ImageView imageView;
    @BindView(R.id.tvPlansName) TextView tvName;

    @Inject
    @InjectPresenter
    DetailPlansPresenter presenter;

    @ProvidePresenter
    DetailPlansPresenter  providePresenter() {
        return presenter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_plans);
        ButterKnife.bind(this);

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().setStatusBarColor(Color.parseColor("#32000000"));

        toolbar.inflateMenu(R.menu.diet_plans_menu);
        toolbar.setNavigationOnClickListener(navigationListener);

        Menu menu = toolbar.getMenu();
        MenuItem shareMenu = menu.findItem(R.id.mShare);
        MenuItem dotMenu = menu.findItem(R.id.mDots);

        shareMenu.setOnMenuItemClickListener(menuListener);
        dotMenu.setOnMenuItemClickListener(menuListener);

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

            return true;
        }
    };

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
//        tvTime.setText(dietPlan.getCountDays() + "");
//        tvRecipes.setText("55");
//        tvUsers.setText("475");

        Glide.with(this)
                .load(dietPlan.getUrlImage())
                .into(imageView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getDietPlan();
    }
}

package com.wsoteam.diet.presentation.plans.detail;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.wsoteam.diet.Config;
import com.wsoteam.diet.DietPlans.POJO.DietPlan;
import com.wsoteam.diet.R;
import com.wsoteam.diet.presentation.global.BaseActivity;
import com.wsoteam.diet.presentation.plans.adapter.VerticalDetailPlansAdapter;
import dagger.android.AndroidInjection;
import javax.inject.Inject;

public class DetailPlansActivity extends BaseActivity implements DetailPlansView {

  @BindView(R.id.toolbar) Toolbar toolbar;
  @BindView(R.id.recycler) RecyclerView recycler;
  @BindView(R.id.btnJoin) Button btnJoin;

  VerticalDetailPlansAdapter adapter;

  @Inject
  @InjectPresenter
  DetailPlansPresenter presenter;
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

  @ProvidePresenter
  DetailPlansPresenter providePresenter() {
    return presenter;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    AndroidInjection.inject(this);
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_detail_plans);
    ButterKnife.bind(this);

    getDietPlan();

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

    recycler.setLayoutManager(new LinearLayoutManager(this));
    DietPlan plan = (DietPlan) getIntent().getSerializableExtra(Config.DIETS_PLAN_INTENT);
    plan.setRecipes(presenter.getList(), presenter.plansRecipe.size());
    adapter = new VerticalDetailPlansAdapter(plan);
    recycler.setAdapter(adapter);
  }
  //test

  @Override
  public void showProgress(boolean show) {

  }

  @Override
  public void showMessage(String message) {

  }

  @Override
  public void getDietPlan() {
    DietPlan dietPlan = (DietPlan) getIntent().getSerializableExtra(Config.DIETS_PLAN_INTENT);
    presenter.setDietPlan(dietPlan);
  }

  @Override
  public void showData(DietPlan dietPlan) {

  }

  @Override
  public void setAdapter() {
    adapter.updateList(presenter.getList());
  }

  @Override
  protected void onResume() {
    super.onResume();
    getDietPlan();
  }
}

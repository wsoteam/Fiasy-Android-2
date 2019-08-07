package com.wsoteam.diet.presentation.plans.detail;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.wsoteam.diet.Config;
import com.wsoteam.diet.DietPlans.POJO.DietPlan;
import com.wsoteam.diet.R;
import com.wsoteam.diet.Recipes.POJO.RecipeItem;
import com.wsoteam.diet.Sync.UserDataHolder;
import com.wsoteam.diet.Sync.WorkWithFirebaseDB;
import com.wsoteam.diet.presentation.global.BaseActivity;
import com.wsoteam.diet.presentation.plans.DateHelper;
import com.wsoteam.diet.presentation.plans.adapter.HorizontalDetailPlansAdapter;
import com.wsoteam.diet.presentation.plans.adapter.VerticalDetailPlansAdapter;
import dagger.android.AndroidInjection;
import java.util.Date;
import javax.inject.Inject;

public class DetailPlansActivity extends BaseActivity implements DetailPlansView {

  @BindView(R.id.toolbar) Toolbar toolbar;
  @BindView(R.id.recycler) RecyclerView recycler;
  @BindView(R.id.btnJoin) Button btnJoin;

  VerticalDetailPlansAdapter adapter;
  DietPlan plan;

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

  HorizontalDetailPlansAdapter.OnItemClickListener adapterListener = new HorizontalDetailPlansAdapter.OnItemClickListener() {
    @Override
    public void onItemClick(RecipeItem recipeItem, String day, String meal, String recipeNumber) {
      Log.d("kkk", recipeItem.getName() + "\n" + day + "\n" + meal + "\n" + recipeNumber + "\n");
      if (recipeItem.isAddedInDiaryFromPlan()){
        recipeItem.setAddedInDiaryFromPlan(false);
        WorkWithFirebaseDB.setRecipeInDiaryFromPlan(day, meal,recipeNumber, false);
      } else {
        recipeItem.setAddedInDiaryFromPlan(true);
        WorkWithFirebaseDB.setRecipeInDiaryFromPlan(day, meal,recipeNumber, true);
      }
    }

    @Override public void onItemLongClick(View view, int position) {

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

    Log.d("kkk", "onCreate: " + UserDataHolder.getUserData().getPlan());

    plan = (DietPlan) getIntent().getSerializableExtra(Config.DIETS_PLAN_INTENT);
    if (UserDataHolder.getUserData() != null
        && UserDataHolder.getUserData().getPlan() != null
        && UserDataHolder.getUserData().getPlan().getName().equals(plan.getName())) {
      btnJoin.setVisibility(View.GONE);
      plan = UserDataHolder.getUserData().getPlan();
    }

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

    plan.setRecipes(presenter.getList(), presenter.plansRecipe.size());
    adapter = new VerticalDetailPlansAdapter(plan);
    adapter.SetOnItemClickListener(adapterListener);
    recycler.setAdapter(adapter);
  }

  @OnClick({ R.id.btnJoin })
  void onClicked(View view) {
    switch (view.getId()) {
      case R.id.btnJoin: {

        plan.setStartDate(DateHelper.dateToString(new Date()));
        WorkWithFirebaseDB.joinDietPlan(plan);
        btnJoin.setVisibility(View.GONE);
      }
      break;
    }
  }

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

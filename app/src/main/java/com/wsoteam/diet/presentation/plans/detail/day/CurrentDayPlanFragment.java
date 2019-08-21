package com.wsoteam.diet.presentation.plans.detail.day;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.arellomobile.mvp.MvpAppCompatFragment;
import com.wsoteam.diet.DietPlans.POJO.DietPlan;
import com.wsoteam.diet.R;
import com.wsoteam.diet.Recipes.POJO.RecipeItem;
import com.wsoteam.diet.Recipes.POJO.plan.RecipeForDay;
import com.wsoteam.diet.Sync.UserDataHolder;
import com.wsoteam.diet.di.CiceroneInject;
import com.wsoteam.diet.di.CiceroneModule;
import com.wsoteam.diet.presentation.global.Screens;
import com.wsoteam.diet.presentation.plans.adapter.HorizontalDetailPlansAdapter;
import com.wsoteam.diet.presentation.plans.browse.BrowsePlansActivity;
import javax.inject.Inject;
import ru.terrakok.cicerone.Navigator;
import ru.terrakok.cicerone.NavigatorHolder;
import ru.terrakok.cicerone.Router;
import ru.terrakok.cicerone.android.support.SupportAppNavigator;

public class CurrentDayPlanFragment extends MvpAppCompatFragment implements TabLayout.OnTabSelectedListener {

  @BindView(R.id.recycler) RecyclerView recyclerView;
  @BindView(R.id.tabs) TabLayout tabLayout;
  @BindView(R.id.clRecipes) ConstraintLayout activePlan;
  @BindView(R.id.clNotActivePlan) ConstraintLayout notActivePlan;
  @BindView(R.id.clFinishPlan) ConstraintLayout finishPlan;
  @BindView(R.id.tvPlanName) TextView planName;
  @BindView(R.id.textView154) TextView dayTextView;

  private LinearLayoutManager layoutManager;
  private HorizontalDetailPlansAdapter adapter;
  private RecipeForDay recipeForDay;
  private int day = 5;
  private Router router;


  NavigatorHolder navigatorHolder = CiceroneInject.navigator();

  private Navigator navigator = new SupportAppNavigator(getActivity(), -1);

  @Nullable @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_current_day_plan,
        container, false);
    ButterKnife.bind(this, view);

    router = CiceroneInject.router();

    Log.d("kkk", "onCreateView: " + router);
    tabLayout.addOnTabSelectedListener(this);
    layoutManager = new LinearLayoutManager(getContext());
    layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
    recyclerView.setLayoutManager(layoutManager);

    adapter = new HorizontalDetailPlansAdapter();
    adapter.SetOnItemClickListener(mItemClickListener);

    recyclerView.setLayoutManager(layoutManager);
    recyclerView.setAdapter(adapter);
    initData(UserDataHolder.getUserData().getPlan());
    return view;
  }

  private void initData(DietPlan plan){
    if (plan != null){
      day = plan.getDaysAfterStart();

      if (day >= plan.getCountDays()) {
        activePlan.setVisibility(View.GONE);
        notActivePlan.setVisibility(View.GONE);
        finishPlan.setVisibility(View.VISIBLE);
      }else {
        activePlan.setVisibility(View.VISIBLE);
        notActivePlan.setVisibility(View.GONE);
        finishPlan.setVisibility(View.GONE);
        recipeForDay = plan.getRecipeForDays().get(day);
        planName.setText("\"" + plan.getName() + "\"");
        dayTextView.setText(String.format(getString(R.string.planDays), day + 1, plan.getCountDays()));
        onTabSelected(tabLayout.getTabAt(tabLayout.getSelectedTabPosition()));

      }

    } else {
      recipeForDay = null;
      activePlan.setVisibility(View.GONE);
      notActivePlan.setVisibility(View.VISIBLE);
      finishPlan.setVisibility(View.GONE);
    }
  }

  HorizontalDetailPlansAdapter.OnItemClickListener mItemClickListener =
      new HorizontalDetailPlansAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(RecipeItem recipeItem, String days, String meal,
            String recipeNumber) {
          Log.d("kkk", "onItemClick: " + day + "  -- " + days + " -- " + router);
          router.navigateTo(new Screens.PlanRecipeScreen(recipeItem, View.VISIBLE, days, meal, recipeNumber) );
          //Screens.PlanRecipeScreen screen = new Screens.PlanRecipeScreen(recipeItem, View.VISIBLE, days, meal, recipeNumber);
          //getActivity().startActivity(screen.getActivityIntent(getContext()));

        }

        @Override public void onItemLongClick(View view, int position) {

        }
      };

  @Override
  public void onTabSelected(TabLayout.Tab tab) {

    if (recipeForDay != null) {
      switch (tab.getPosition()) {
        case 0:
          adapter.updateList(recipeForDay.getBreakfast(), true, day, "breakfast");
          break;
        case 1:
          adapter.updateList(recipeForDay.getLunch(), true, day, "lunch");
          break;
        case 2:
          adapter.updateList(recipeForDay.getDinner(), true, day, "dinner");
          break;
        case 3:
          adapter.updateList(recipeForDay.getSnack(), true, day, "snack");
          break;
        default:
          adapter.updateList(recipeForDay.getBreakfast(), true, day, "breakfast");
      }
    }
  }

  @OnClick({R.id.tvViewPlans, R.id.tvViewcOtherPlans})
  void clicked(){
    getActivity().startActivity(new Intent(getContext(), BrowsePlansActivity.class));
  }

  @Override public void onTabUnselected(TabLayout.Tab tab) {

  }

  @Override public void onTabReselected(TabLayout.Tab tab) {

  }

  @Override public void onResume() {
    super.onResume();
    Log.d("kkk", "onResume: ");
    initData(UserDataHolder.getUserData().getPlan());
  }
}

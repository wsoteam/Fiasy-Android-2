package com.wsoteam.diet.presentation.plans.detail.day;

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
import com.arellomobile.mvp.MvpAppCompatFragment;
import com.wsoteam.diet.DietPlans.POJO.DietPlan;
import com.wsoteam.diet.R;
import com.wsoteam.diet.Recipes.POJO.RecipeItem;
import com.wsoteam.diet.Recipes.POJO.plan.RecipeForDay;
import com.wsoteam.diet.Sync.UserDataHolder;
import com.wsoteam.diet.presentation.global.Screens;
import com.wsoteam.diet.presentation.plans.DateHelper;
import com.wsoteam.diet.presentation.plans.adapter.HorizontalDetailPlansAdapter;
import java.util.Date;
import ru.terrakok.cicerone.Router;

public class CurrentDayPlanFragment extends MvpAppCompatFragment implements TabLayout.OnTabSelectedListener {

  @BindView(R.id.recycler) RecyclerView recyclerView;
  @BindView(R.id.tabs) TabLayout tabLayout;
  @BindView(R.id.mainLayout) ConstraintLayout maimLayout;
  @BindView(R.id.tvPlanName) TextView planName;
  @BindView(R.id.textView154) TextView dayTextView;

  private LinearLayoutManager layoutManager;
  private HorizontalDetailPlansAdapter adapter;
  private RecipeForDay recipeForDay;
  private int day = 5;
  private Router router;

  @Nullable @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_current_day_plan,
        container, false);
    ButterKnife.bind(this, view);

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
      maimLayout.setVisibility(View.VISIBLE);

      day = plan.getDaysAfterStart();
      recipeForDay = plan.getRecipeForDays().get(day);
      planName.setText(plan.getName());
      dayTextView.setText(String.format(getString(R.string.planDays), day + 1, plan.getCountDays()));
      adapter.updateList(recipeForDay.getBreakfast(), true, day, "breakfast");


    } else {
      recipeForDay = null;
      maimLayout.setVisibility(View.GONE);
    }
  }

  HorizontalDetailPlansAdapter.OnItemClickListener mItemClickListener =
      new HorizontalDetailPlansAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(RecipeItem recipeItem, String days, String meal,
            String recipeNumber) {
          if (day == Integer.parseInt(days)) {
            router.navigateTo(new Screens.PlanRecipeScreen(recipeItem, View.VISIBLE, days, meal, recipeNumber));
          } else {
            router.navigateTo(new Screens.PlanRecipeScreen(recipeItem, days, meal, recipeNumber));
          }

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

  @Override public void onTabUnselected(TabLayout.Tab tab) {

  }

  @Override public void onTabReselected(TabLayout.Tab tab) {

  }
}

package com.wsoteam.diet.presentation.plans.detail.day;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AlertDialog;
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
import com.amplitude.api.Amplitude;
import com.arellomobile.mvp.MvpAppCompatFragment;
import com.wsoteam.diet.AmplitudaEvents;
import com.wsoteam.diet.BranchOfAnalyzer.Dialogs.AddFoodDialog;
import com.wsoteam.diet.Config;
import com.wsoteam.diet.DietPlans.POJO.DietPlan;
import com.wsoteam.diet.R;
import com.wsoteam.diet.Recipes.POJO.RecipeItem;
import com.wsoteam.diet.Recipes.POJO.plan.RecipeForDay;
import com.wsoteam.diet.Sync.UserDataHolder;
import com.wsoteam.diet.Sync.WorkWithFirebaseDB;
import com.wsoteam.diet.di.CiceroneModule;
import com.wsoteam.diet.model.Breakfast;
import com.wsoteam.diet.model.Dinner;
import com.wsoteam.diet.model.Lunch;
import com.wsoteam.diet.model.Snack;
import com.wsoteam.diet.presentation.global.Screens;
import com.wsoteam.diet.presentation.plans.browse.BrowsePlansActivity;
import java.util.Calendar;
import java.util.List;
import ru.terrakok.cicerone.Router;

import static android.content.Context.MODE_PRIVATE;

public class CurrentDayPlanFragment extends MvpAppCompatFragment implements TabLayout.OnTabSelectedListener {

  @BindView(R.id.recycler) RecyclerView recyclerView;
  @BindView(R.id.tabs) TabLayout tabLayout;
  @BindView(R.id.clRecipes) ConstraintLayout activePlan;
  @BindView(R.id.clNotActivePlan) ConstraintLayout notActivePlan;
  @BindView(R.id.clFinishPlan) ConstraintLayout finishPlan;
  @BindView(R.id.tvPlanName) TextView planName;
  @BindView(R.id.textView154) TextView dayTextView;

  private LinearLayoutManager layoutManager;
  private CurrentDayPlanAdapter adapter;
  private RecipeForDay recipeForDay;
  private int day = 5;
  private Router router;

  private final int BREAKFAST_POSITION = 0, LUNCH_POSITION = 1, DINNER_POSITION = 2,
      SNACK_POSITION = 3, EMPTY_FIELD = -1;

  @Nullable @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_current_day_plan,
        container, false);
    ButterKnife.bind(this, view);

    router = CiceroneModule.router();
    tabLayout.addOnTabSelectedListener(this);
    layoutManager = new LinearLayoutManager(getContext());
    layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
    recyclerView.setLayoutManager(layoutManager);

    adapter = new CurrentDayPlanAdapter();
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

  CurrentDayPlanAdapter.OnItemClickListener mItemClickListener =
      new CurrentDayPlanAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(RecipeItem recipeItem, String days, String meal,
            String recipeNumber) {
          Log.d("kkk", "onItemClick: " + day + "  -- " + days + " -- " + router);

          Screens.PlanRecipeScreen screen = new Screens.PlanRecipeScreen(recipeItem, View.VISIBLE, days, meal, recipeNumber);
          getActivity().startActivity(screen.getActivityIntent(getContext()));

        }

        @Override public void onButtonClick(RecipeItem recipeItem, String day, String meal,
            String recipeNumber) {
          savePortion(recipeItem, day, meal, recipeNumber);

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


  private void savePortion(RecipeItem recipe, String dayPlan, String meal,
      String recipeNumber) {

    Calendar c = Calendar.getInstance();
    int year = c.get(Calendar.YEAR);
    int month = c.get(Calendar.MONTH);
    int day = c.get(Calendar.DAY_OF_MONTH);

    int kcal = recipe.getCalories();
    int carbo = (int) recipe.getCarbohydrates();
    int prot = recipe.getPortions();
    int fat = (int) recipe.getFats();
    int weight = -1;

    String name = recipe.getName();
    String urlOfImage = recipe.getUrl();

    Amplitude.getInstance().logEvent(AmplitudaEvents.success_add_food);
    switch (meal) {
      case "breakfast":
        WorkWithFirebaseDB.
            addBreakfast(
                new Breakfast(name, urlOfImage, kcal, carbo, prot, fat, weight, day, month, year));
        break;
      case "lunch":
        WorkWithFirebaseDB.
            addLunch(new Lunch(name, urlOfImage, kcal, carbo, prot, fat, weight, day, month, year));
        break;
      case "dinner":
        WorkWithFirebaseDB.
            addDinner(
                new Dinner(name, urlOfImage, kcal, carbo, prot, fat, weight, day, month, year));
        break;
      case "snack":
        WorkWithFirebaseDB.
            addSnack(new Snack(name, urlOfImage, kcal, carbo, prot, fat, weight, day, month, year));
        break;
    }

    WorkWithFirebaseDB.setRecipeInDiaryFromPlan(dayPlan, meal, recipeNumber, true);


    AlertDialog alertDialog = AddFoodDialog.createChoiseEatingAlertDialog(getContext());
    alertDialog.show();
    getActivity().getSharedPreferences(Config.IS_ADDED_FOOD, MODE_PRIVATE).edit()
        .putBoolean(Config.IS_ADDED_FOOD, true)
        .commit();
    new CountDownTimer(800, 100) {
      @Override
      public void onTick(long millisUntilFinished) {

      }

      @Override
      public void onFinish() {
        setAddedInDiaryFromPlan(recipeNumber);
        alertDialog.dismiss();
      }
    }.start();
  }

  private void setAddedInDiaryFromPlan(String recipeNumber) {

    List<RecipeItem> recipeItemList = adapter.getLisrRecipe();

    if (recipeItemList != null){
      Log.d("kkk", "setAddedInDiaryFromPlan: != null");
      RecipeItem recipeItem = recipeItemList.get(Integer.parseInt(recipeNumber));
      Log.d("kkk", "setAddedInDiaryFromPlan: " + recipeItem.getName());
      recipeItem.setAddedInDiaryFromPlan(true);
    }
    adapter.notifyDataSetChanged();
  }
}

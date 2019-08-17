package com.wsoteam.diet.presentation.plans.detail;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import com.arellomobile.mvp.InjectViewState;
import com.wsoteam.diet.Config;
import com.wsoteam.diet.DietPlans.POJO.DietPlan;
import com.wsoteam.diet.R;
import com.wsoteam.diet.Recipes.POJO.RecipeItem;
import com.wsoteam.diet.Recipes.POJO.RecipesHolder;
import com.wsoteam.diet.Recipes.POJO.plan.PlansGroupsRecipe;
import com.wsoteam.diet.Recipes.POJO.plan.RecipeForDay;
import com.wsoteam.diet.Sync.UserDataHolder;
import com.wsoteam.diet.Sync.WorkWithFirebaseDB;
import com.wsoteam.diet.presentation.global.BasePresenter;
import com.wsoteam.diet.presentation.global.Screens;
import com.wsoteam.diet.presentation.plans.DateHelper;
import com.wsoteam.diet.presentation.plans.adapter.HorizontalDetailPlansAdapter;
import com.wsoteam.diet.presentation.plans.adapter.VerticalDetailPlansAdapter;
import java.util.Date;
import java.util.List;
import javax.inject.Inject;
import ru.terrakok.cicerone.Router;

@InjectViewState
public class DetailPlansPresenter extends BasePresenter<DetailPlansView> {

  Router router;
  Intent intent;
  DietPlan dietPlan;
  @Inject
  Context context;

  PlansGroupsRecipe plansRecipe;
  List<RecipeForDay> recipeForDays;
  VerticalDetailPlansAdapter adapter;
  boolean isCurrentPlan;

  public DetailPlansPresenter(Router router, Intent intent) {
    //Log.d("kkk", "DetailPlansPresenter: constructor");
    this.router = router;
    this.intent = intent;
    initDietPlan();
  }

  private void initDietPlan() {
    dietPlan = (DietPlan) intent.getSerializableExtra(Config.DIETS_PLAN_INTENT);
    if (UserDataHolder.getUserData() != null
        && UserDataHolder.getUserData().getPlan() != null
        && UserDataHolder.getUserData().getPlan().getName().equals(dietPlan.getName())) {
      getViewState().visibilityButtonJoin(false);
      dietPlan = UserDataHolder.getUserData().getPlan();
      adapter = new VerticalDetailPlansAdapter(dietPlan, true);
       Log.d("kkk", "onCreate: " + dietPlan.getRecipeForDays().get(5).getBreakfast().get(0).isAddedInDiaryFromPlan());
       isCurrentPlan = true;
    } else {
      Log.d("kkk", "initDietPlan: else " );
      plansRecipe = new PlansGroupsRecipe(RecipesHolder.get(), dietPlan);
      recipeForDays = plansRecipe.getRecipeForDays();
      Log.d("kkkk", "initDietPlan: " + recipeForDays.size());
      dietPlan.setRecipes(getList(), plansRecipe.size());
      Log.d("kkkk", "initDietPlan: getList() " + getList().size());
      adapter = new VerticalDetailPlansAdapter(dietPlan);
      isCurrentPlan = false;
    }

    adapter.SetOnItemClickListener(adapterListener);
    getViewState().setAdapter(adapter);
    Log.d("kkk", "initDietPlan: adapter " + adapter);
  }

  void clickedClose() {
    router.exit();
  }


  List<RecipeForDay> getList() {
    return recipeForDays;
  }

  PlansGroupsRecipe getRecipes() {
    return plansRecipe;
  }

  void clickedJoin() {
    if (UserDataHolder.getUserData().getPlan() == null){
      joinPlans();
    } else {
      getViewState().startAlert(UserDataHolder.getUserData().getPlan().getName());
    }
  }

  void joinPlans(){
    dietPlan.setStartDate(DateHelper.dateToString(new Date()));
    UserDataHolder.getUserData().setPlan(dietPlan);
    WorkWithFirebaseDB.joinDietPlan(dietPlan);
    getViewState().visibilityButtonJoin(false);
    initDietPlan();
  }

  void clickedLeave(){
    WorkWithFirebaseDB.leaveDietPlan();
    getViewState().visibilityButtonJoin(true);
    UserDataHolder.getUserData().setPlan(null);
    initDietPlan();
  }

  void onResume(){
    Log.d("kkk", "onResume: ");
    //adapter.notifyDataSetChanged();
    if (isCurrentPlan)
    adapter.updateList(UserDataHolder.getUserData().getPlan().getRecipeForDays());
  }


  HorizontalDetailPlansAdapter.OnItemClickListener adapterListener = new HorizontalDetailPlansAdapter.OnItemClickListener() {
    @Override
    public void onItemClick(RecipeItem recipeItem, String day, String meal, String recipeNumber) {
      Log.d("kkk", recipeItem.getName() + "\n" + day + "\n" + meal + "\n" + recipeNumber + "\n" + recipeItem.isAddedInDiaryFromPlan());

      if (dietPlan.getDaysAfterStart() == Integer.parseInt(day)) {
        router.navigateTo(new Screens.PlanRecipeScreen(recipeItem, View.VISIBLE, day, meal, recipeNumber));
      } else {
        router.navigateTo(new Screens.PlanRecipeScreen(recipeItem, day, meal, recipeNumber));
      }
      //if (recipeItem.isAddedInDiaryFromPlan()){
      //  recipeItem.setAddedInDiaryFromPlan(false);
      //  WorkWithFirebaseDB.setRecipeInDiaryFromPlan(day, meal,recipeNumber, false);
      //} else {
      //  recipeItem.setAddedInDiaryFromPlan(true);
      //  WorkWithFirebaseDB.setRecipeInDiaryFromPlan(day, meal,recipeNumber, true);
      //}
    }

    @Override public void onItemLongClick(View view, int position) {

    }
  };
}

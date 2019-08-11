package com.wsoteam.diet.presentation.plans.detail;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import com.arellomobile.mvp.InjectViewState;
import com.wsoteam.diet.Config;
import com.wsoteam.diet.DietPlans.POJO.DietPlan;
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

  public DetailPlansPresenter(Router router, Intent intent) {
    Log.d("kkk", "DetailPlansPresenter: constructor");
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
    } else {
      Log.d("kkk", "initDietPlan: else " );
      plansRecipe = new PlansGroupsRecipe(RecipesHolder.get(), dietPlan);
      recipeForDays = plansRecipe.getRecipeForDays();
      dietPlan.setRecipes(getList(), plansRecipe.size());
      adapter = new VerticalDetailPlansAdapter(dietPlan);
    }

    adapter.SetOnItemClickListener(adapterListener);
    getViewState().setAdapter(adapter);
    Log.d("kkk", "initDietPlan: adapter " + adapter);
  }

  void clickedClose() {
    router.exit();
  }

  //void setDietPlan(DietPlan diet){
  //    this.dietPlan = diet;
  //    plansRecipe = new PlansGroupsRecipe(RecipesHolder.get(), dietPlan);
  //    recipeForDays = plansRecipe.getRecipeForDays();
  //    getViewState().showData(dietPlan);
  //    getViewState().setAdapter();
  //}

  DietPlan getDietPlan() {
    return dietPlan;
  }

  List<RecipeForDay> getList() {
    return recipeForDays;
  }

  PlansGroupsRecipe getRecipes() {
    return plansRecipe;
  }

  void clickedJoin() {
    dietPlan.setStartDate(DateHelper.dateToString(new Date()));
    WorkWithFirebaseDB.joinDietPlan(dietPlan);
    getViewState().visibilityButtonJoin(false);
  }


  HorizontalDetailPlansAdapter.OnItemClickListener adapterListener = new HorizontalDetailPlansAdapter.OnItemClickListener() {
    @Override
    public void onItemClick(RecipeItem recipeItem, String day, String meal, String recipeNumber) {
      Log.d("kkk", recipeItem.getName() + "\n" + day + "\n" + meal + "\n" + recipeNumber + "\n");
      router.navigateTo(new Screens.PlanRecipeScreen(recipeItem, day, meal, recipeNumber));
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

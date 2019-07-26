package com.wsoteam.diet.presentation.plans.detail;


import com.arellomobile.mvp.InjectViewState;
import com.wsoteam.diet.DietPlans.POJO.DietPlan;
import com.wsoteam.diet.Recipes.POJO.RecipesHolder;
import com.wsoteam.diet.Recipes.POJO.plan.PlansGroupsRecipe;
import com.wsoteam.diet.Recipes.POJO.plan.RecipeForDay;
import com.wsoteam.diet.presentation.global.BasePresenter;
import com.wsoteam.diet.presentation.plans.adapter.VerticalDetailPlansAdapter;

import java.util.List;

import ru.terrakok.cicerone.Router;

@InjectViewState
public class DetailPlansPresenter extends BasePresenter<DetailPlansView> {

    Router router;
    DietPlan dietPlan;

    PlansGroupsRecipe plansRecipe;
    List<RecipeForDay> recipeForDays;

    public DetailPlansPresenter(Router router) {
        this.router = router;
    }

    void clickedClose(){
        router.exit();
    }

    void setDietPlan(DietPlan diet){
        this.dietPlan = diet;
        plansRecipe = new PlansGroupsRecipe(RecipesHolder.get(), dietPlan);
        recipeForDays = plansRecipe.getRecipeForDays();
        getViewState().showData(dietPlan);
        getViewState().setAdapter();
    }

    DietPlan getDietPlan(){
        return dietPlan;
    }

    List<RecipeForDay> getList(){
       return recipeForDays;
    }

    PlansGroupsRecipe getRecipes(){
        return plansRecipe;
    }
}

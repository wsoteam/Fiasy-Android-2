package com.wsoteam.diet.presentation.plans.detail;


import com.arellomobile.mvp.InjectViewState;
import com.wsoteam.diet.DietPlans.POJO.DietPlan;
import com.wsoteam.diet.Recipes.POJO.RecipesHolder;
import com.wsoteam.diet.Recipes.POJO.plan.PlansGroupsRecipe;
import com.wsoteam.diet.Recipes.POJO.plan.RecipeForDay;
import com.wsoteam.diet.presentation.global.BasePresenter;

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
        plansRecipe = new PlansGroupsRecipe(RecipesHolder.get(), dietPlan.getFlag());
        recipeForDays = plansRecipe.getRecipeForDays();
        getViewState().showData(dietPlan);
    }

    PlansGroupsRecipe getRecipes(){
        return plansRecipe;
    }
}

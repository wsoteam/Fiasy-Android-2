package com.wsoteam.diet.presentation.plans.detail;


import com.arellomobile.mvp.InjectViewState;
import com.wsoteam.diet.DietPlans.POJO.DietPlan;
import com.wsoteam.diet.Recipes.POJO.PlansGroupsRecipe;
import com.wsoteam.diet.Recipes.POJO.RecipesHolder;
import com.wsoteam.diet.presentation.global.BasePresenter;

import ru.terrakok.cicerone.Router;

@InjectViewState
public class DetailPlansPresenter extends BasePresenter<DetailPlansView> {

    Router router;
    DietPlan dietPlan;

    PlansGroupsRecipe plansRecipe;

    public DetailPlansPresenter(Router router) {
        this.router = router;
    }

    void clickedClose(){
        router.exit();
    }

    void setDietPlan(DietPlan diet){
        this.dietPlan = diet;
        plansRecipe = new PlansGroupsRecipe(RecipesHolder.get(), dietPlan.getFlag());
        getViewState().showData(dietPlan);
    }

    PlansGroupsRecipe getRecipes(){
        return plansRecipe;
    }
}

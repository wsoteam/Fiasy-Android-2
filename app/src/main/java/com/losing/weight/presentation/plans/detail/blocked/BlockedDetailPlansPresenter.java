package com.losing.weight.presentation.plans.detail.blocked;



import android.content.Context;

import com.arellomobile.mvp.InjectViewState;
import com.losing.weight.DietPlans.POJO.DietPlan;
import com.losing.weight.Recipes.POJO.RecipesHolder;
import com.losing.weight.Recipes.POJO.plan.PlansGroupsRecipe;
import com.losing.weight.common.Analytics.Events;
import com.losing.weight.presentation.global.BasePresenter;
import com.losing.weight.presentation.global.Screens;

import ru.terrakok.cicerone.Router;

@InjectViewState
public class BlockedDetailPlansPresenter extends BasePresenter<BlockedDetailPlansView> {

    private DietPlan dietPlan;
    private Router router;
    private Context context;
    PlansGroupsRecipe plansRecipe;

    public BlockedDetailPlansPresenter(Router router, Context context) {
        this.router = router;
        this.context = context;
    }

    void setDietPlan(DietPlan diet){
        this.dietPlan = diet;
        plansRecipe = new PlansGroupsRecipe(RecipesHolder.get(), dietPlan, context);
        getViewState().showData(dietPlan);
        Events.logViewPlan(diet.getFlag());
    }

    void clickedShare(){
        getViewState().sharePlan(dietPlan.getName() + "\n https://play.google.com/store/apps/details?id=com.wild.diet" );
    }

    void clickedPremButton(){
       router.navigateTo(new Screens.SubscriptionScreen());
    }

    void clickedClose(){
        router.exit();
    }

    PlansGroupsRecipe getRecipes(){
        return plansRecipe;
    }
}

package com.wsoteam.diet.presentation.plans.detail.blocked;



import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.wsoteam.diet.DietPlans.POJO.DietPlan;
import com.wsoteam.diet.presentation.global.BasePresenter;
import com.wsoteam.diet.presentation.global.Screens;

import ru.terrakok.cicerone.Router;

@InjectViewState
public class BlockedDetailPlansPresenter extends BasePresenter<BlockedDetailPlansView> {

    private DietPlan dietPlan;
    private Router router;

    public BlockedDetailPlansPresenter(Router router) {
        this.router = router;
    }

    void setDietPlan(DietPlan diet){
        this.dietPlan = diet;
        getViewState().showData(dietPlan);
    }

    void clickedPremButton(){
       router.navigateTo(new Screens.SubscriptionScreen());
    }

    void clickedClose(){
        router.exit();
    }
}

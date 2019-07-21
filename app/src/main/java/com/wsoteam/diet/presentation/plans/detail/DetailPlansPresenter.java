package com.wsoteam.diet.presentation.plans.detail;

import com.arellomobile.mvp.InjectViewState;
import com.wsoteam.diet.DietPlans.POJO.DietPlan;
import com.wsoteam.diet.presentation.global.BasePresenter;

import ru.terrakok.cicerone.Router;

@InjectViewState
public class DetailPlansPresenter extends BasePresenter<DetailPlansView> {

    Router router;
    DietPlan dietPlan;

    public DetailPlansPresenter(Router router) {
        this.router = router;
    }

    void clickedClose(){
        router.exit();
    }

    void setDietPlan(DietPlan diet){
        this.dietPlan = diet;
        getViewState().showData(dietPlan);
    }
}

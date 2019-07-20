package com.wsoteam.diet.presentation.plans.detail.blocked;

import com.arellomobile.mvp.InjectViewState;
import com.wsoteam.diet.DietPlans.POJO.DietPlan;
import com.wsoteam.diet.presentation.global.BasePresenter;

@InjectViewState
public class BlockedDetailPlansPresenter extends BasePresenter<BlockedDetailPlansView> {

    private DietPlan dietPlan;

    public BlockedDetailPlansPresenter() {
    }

    void setDietPlan(DietPlan diet){
        this.dietPlan = diet;
        getViewState().showData(dietPlan);
    }
}

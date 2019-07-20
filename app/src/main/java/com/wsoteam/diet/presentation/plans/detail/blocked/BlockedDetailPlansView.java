package com.wsoteam.diet.presentation.plans.detail.blocked;

import com.wsoteam.diet.DietPlans.POJO.DietPlan;
import com.wsoteam.diet.presentation.global.BaseView;

public interface BlockedDetailPlansView extends BaseView {

    void getDietPlan();
    void showData(DietPlan dietPlan);
}

package com.wsoteam.diet.presentation.plans.detail;

import com.wsoteam.diet.DietPlans.POJO.DietPlan;
import com.wsoteam.diet.presentation.global.BaseView;

public interface DetailPlansView extends BaseView {
    void getDietPlan();
    void showData(DietPlan dietPlan);
    void setAdapter();
}

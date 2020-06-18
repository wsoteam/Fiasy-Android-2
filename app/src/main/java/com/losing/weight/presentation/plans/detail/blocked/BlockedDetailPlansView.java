package com.losing.weight.presentation.plans.detail.blocked;

import com.losing.weight.DietPlans.POJO.DietPlan;
import com.losing.weight.presentation.global.BaseView;

public interface BlockedDetailPlansView extends BaseView {

    void getDietPlan();
    void showData(DietPlan dietPlan);
    void sharePlan(String str);
}

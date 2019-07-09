package com.wsoteam.diet.presentation.food.template.create;

import com.wsoteam.diet.BranchOfAnalyzer.templates.POJO.FoodTemplate;
import com.wsoteam.diet.presentation.global.BaseView;

public interface CreateFoodTemplateView  extends BaseView {

    void setData(FoodTemplate foodTemplate);
    void setName(String str);
    void updateRecyclerView();

}

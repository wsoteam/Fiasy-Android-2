package com.losing.weight.presentation.food.template.create;

import com.losing.weight.BranchOfAnalyzer.templates.POJO.FoodTemplate;
import com.losing.weight.presentation.global.BaseView;

public interface CreateFoodTemplateView  extends BaseView {

    void setData(FoodTemplate foodTemplate);
    void setName(String str);
    void updateRecyclerView();
    void setColorSaveButton(int i);

}

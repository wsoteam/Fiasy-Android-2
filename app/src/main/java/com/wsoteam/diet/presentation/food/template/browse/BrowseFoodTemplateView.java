package com.wsoteam.diet.presentation.food.template.browse;

import com.wsoteam.diet.BranchOfAnalyzer.templates.POJO.FoodTemplate;
import com.wsoteam.diet.presentation.global.BaseView;

import java.util.List;

public interface BrowseFoodTemplateView extends BaseView {

    void setData(List<FoodTemplate> foodTemplates);
    void showBtn();
    void hideBtn();
}

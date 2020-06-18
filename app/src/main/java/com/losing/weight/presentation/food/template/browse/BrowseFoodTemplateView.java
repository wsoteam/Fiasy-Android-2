package com.losing.weight.presentation.food.template.browse;

import com.losing.weight.BranchOfAnalyzer.templates.POJO.FoodTemplate;
import com.losing.weight.presentation.global.BaseView;

import java.util.List;

public interface BrowseFoodTemplateView extends BaseView {

    void setData(List<FoodTemplate> foodTemplates);
    void showBtn();
    void hideBtn();
}

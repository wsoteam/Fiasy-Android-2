package com.losing.weight.presentation.food.template.create;


import android.content.Intent;

import com.arellomobile.mvp.InjectViewState;
import com.losing.weight.BranchOfAnalyzer.POJOFoodSQL.Food;
import com.losing.weight.BranchOfAnalyzer.templates.POJO.FoodTemplate;
import com.losing.weight.BranchOfAnalyzer.templates.POJO.FoodTemplateHolder;
import com.losing.weight.Config;
import com.losing.weight.Sync.WorkWithFirebaseDB;
import com.losing.weight.common.Analytics.Events;
import com.losing.weight.presentation.global.BasePresenter;
import com.losing.weight.presentation.global.Screens;

import java.util.List;

import ru.terrakok.cicerone.Router;


@InjectViewState
public class CreateFoodTemplatePresenter extends BasePresenter<CreateFoodTemplateView> {


    private Router router;
    private FoodTemplate foodTemplate;
    List<Food> foods;

    boolean isEdit;

    public CreateFoodTemplatePresenter(Router router, FoodTemplate foodTemplate) {
        this.router = router;
        init(foodTemplate);

    }

    void init(FoodTemplate template){
        if (template != null) {
            this.foodTemplate = template;
            foods = template.getFoodList();
            FoodTemplateHolder.bind(foods);
            getViewState().setData(foodTemplate);
            getViewState().setName(foodTemplate.getName());
        }
    }

    void checkIntent(Intent intent){
        FoodTemplate template = (FoodTemplate)intent.getSerializableExtra(Config.FOOD_TEMPLATE_INTENT);

        if (template != null) {
            init(template);
            isEdit = true;
        }
    }

    void onCancelClicked(){
        router.exit();
    }

    void onSaveClicked(String stringExtra){

        getViewState().setColorSaveButton(foods.size());

        if ("".equals(foodTemplate.getName().trim())){
            getViewState().showMessage("Ввведите название");
        } else if (foodTemplate.getFoodList().size() < 1){
            getViewState().showMessage("Нет продуктов в списке");
        } else {
            if (isEdit){
                WorkWithFirebaseDB.editFoodTemplate(foodTemplate.getKey(), foodTemplate);

            }else {
                Events.logCreateTemplate(stringExtra, foodTemplate.getEating(), foodTemplate.getFoodList());
                WorkWithFirebaseDB.addFoodTemplate(foodTemplate);
            }
            getViewState().showMessage("Сохранено в раздел Шаблоны");
            FoodTemplateHolder.bind(null);
            router.exit();

        }
    }

    public void checkListFoodsSize(){
        getViewState().setColorSaveButton(foods.size());
    }

    void onAddFoodClicked(){
        router.navigateTo(new Screens.CreateSearchFoodActivity());
    }

    void onNameChanged(String str){
        foodTemplate.setName(str.trim());
    }

    void onEatingChanged(String str){
        foodTemplate.setEating(str);
    }

}

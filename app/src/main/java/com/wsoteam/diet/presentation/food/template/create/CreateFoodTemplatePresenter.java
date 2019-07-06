package com.wsoteam.diet.presentation.food.template.create;


import com.arellomobile.mvp.InjectViewState;
import com.wsoteam.diet.BranchOfAnalyzer.POJOFoodSQL.Food;
import com.wsoteam.diet.BranchOfAnalyzer.templates.POJO.FoodTemplate;
import com.wsoteam.diet.Sync.WorkWithFirebaseDB;
import com.wsoteam.diet.presentation.global.BasePresenter;

import java.util.List;

import ru.terrakok.cicerone.Router;

@InjectViewState
public class CreateFoodTemplatePresenter extends BasePresenter<CreateFoodTemplateView> {


    private Router router;
    private FoodTemplate foodTemplate;
    List<Food> foods;

    public CreateFoodTemplatePresenter(Router router, FoodTemplate foodTemplate) {
        this.router = router;
        this.foodTemplate = foodTemplate;

        foods = foodTemplate.getFoodList();
        foods.add(foodFactory());
        foods.add(foodFactory());
        foods.add(foodFactory());
        foodTemplate.setName("");
        foodTemplate.setEating("Ужин");

        getViewState().setData(foodTemplate);

    }

    Food foodFactory(){
        Food food = new Food();
        food.setFullInfo("23132123132 231321321");
        food.setCalories(321.0);
        food.setProteins(23.0);
        food.setFats(23.0);
        food.setCarbohydrates(2.0);
        return food;
    }

    void onCancelClicked(){
        router.exit();
    }

    void onSaveClicked(){

        if ("".equals(foodTemplate.getName().trim())){
            getViewState().showMessage("Ввведите название");
        } else if (foodTemplate.getFoodList().size() < 1){
            getViewState().showMessage("Нет продуктов в списке");
        } else {
            WorkWithFirebaseDB.addFoodTemplate(foodTemplate);
            getViewState().showMessage("Шаблон сохранен");
            router.exit();
        }

    }

    void onAddFoodClicked(){

    }

    void onNameChanged(String str){
        foodTemplate.setName(str);
    }

    void onEatingChanged(String str){
        foodTemplate.setEating(str);
    }


}

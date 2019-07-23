package com.wsoteam.diet.presentation.food.template.browse;

import android.app.Activity;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.amplitude.api.Amplitude;
import com.arellomobile.mvp.InjectViewState;
import com.wsoteam.diet.AmplitudaEvents;
import com.wsoteam.diet.BranchOfAnalyzer.ActivityListAndSearch;
import com.wsoteam.diet.BranchOfAnalyzer.Dialogs.AddFoodDialog;
import com.wsoteam.diet.BranchOfAnalyzer.POJOEating.Breakfast;
import com.wsoteam.diet.BranchOfAnalyzer.POJOEating.Dinner;
import com.wsoteam.diet.BranchOfAnalyzer.POJOEating.Lunch;
import com.wsoteam.diet.BranchOfAnalyzer.POJOEating.Snack;
import com.wsoteam.diet.BranchOfAnalyzer.POJOFoodSQL.Food;
import com.wsoteam.diet.BranchOfAnalyzer.templates.POJO.FoodTemplate;
import com.wsoteam.diet.Config;
import com.wsoteam.diet.Sync.UserDataHolder;
import com.wsoteam.diet.Sync.WorkWithFirebaseDB;
import com.wsoteam.diet.presentation.food.template.create.CreateFoodTemplateActivity;
import com.wsoteam.diet.presentation.global.BasePresenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

@InjectViewState
public class BrowseFoodTemplatePresenter extends BasePresenter<BrowseFoodTemplateView> {

    HashMap<String, FoodTemplate> foodTemplates;
    Activity activity;

    public BrowseFoodTemplatePresenter(Activity activity) {
        this.activity = activity;
        initAdapter();
    }

    void initAdapter(){
        foodTemplates = UserDataHolder.getUserData().getFoodTemplates();
        if (foodTemplates != null){
            getViewState().setData(new ArrayList<>(foodTemplates.values()));
            getViewState().hideBtn();
        } else {
            getViewState().showBtn();
            getViewState().setData(null);
        }
    }

    public void editTemplate(FoodTemplate template){
        Intent intent = new Intent(activity, CreateFoodTemplateActivity.class);
        intent.putExtra(Config.FOOD_TEMPLATE_INTENT, template);
        activity.startActivity(intent);
    }

    public  void addToDiary(FoodTemplate template){

        for (Food food:
             template.getFoodList()) {
            savePortion(food);
        }

        AlertDialog alertDialog = AddFoodDialog.createChoiseEatingAlertDialog(activity);
        alertDialog.show();
        activity.getSharedPreferences(Config.IS_ADDED_FOOD, MODE_PRIVATE).edit().putBoolean(Config.IS_ADDED_FOOD, true).commit();
        new CountDownTimer(800, 100) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                alertDialog.dismiss();

            }
        }.start();

    }

    void addTemplate(){
        activity.startActivity(new Intent(activity, CreateFoodTemplateActivity.class)
                .putExtra(Config.EATING_SPINNER_POSITION, ((ActivityListAndSearch) activity).spinnerId));
    }

    void search(String str){

        initAdapter();

        if (foodTemplates == null || foodTemplates.size() == 0){
            return;
        }

        List<FoodTemplate> result = new ArrayList<>();
        String key;

        if (str != null) {
            key = str.toLowerCase();
        } else {
            key = null;
        }

        if (key == null || key.equals("")) {
            getViewState().setData(new ArrayList<>(foodTemplates.values()));
        } else {
            for( FoodTemplate template : new ArrayList<>(foodTemplates.values())) {
                if (template.getName() != null && template.getName().toLowerCase().contains(key)) {
                    result.add(template);
                }
            }
            getViewState().setData(result);
        }
    }

    private void savePortion(Food food) {
        final int BREAKFAST_POSITION = 0, LUNCH_POSITION = 1, DINNER_POSITION = 2, SNACK_POSITION = 3, EMPTY_FIELD = -1;

        String wholeDate = activity.getIntent().getStringExtra(Config.INTENT_DATE_FOR_SAVE);
        String[] arrayOfNumbersForDate = wholeDate.split("\\.");

        int day = Integer.parseInt(arrayOfNumbersForDate[0]);
        int month = Integer.parseInt(arrayOfNumbersForDate[1]) - 1;
        int year = Integer.parseInt(arrayOfNumbersForDate[2]);

        int kcal = (int)(food.getCalories() * food.getPortion());
        int carbo = (int)(food.getCarbohydrates() * food.getPortion());
        int prot = (int)(food.getProteins() * food.getPortion());
        int fat = (int)(food.getFats() * food.getPortion());

        int weight = (int)food.getPortion();

        String name = food.getName();
        String urlOfImage = "empty_url";

        Amplitude.getInstance().logEvent(AmplitudaEvents.success_add_food);

        switch (((ActivityListAndSearch) activity).spinnerId) {
            case BREAKFAST_POSITION:
                WorkWithFirebaseDB.
                        addBreakfast(new Breakfast(name, urlOfImage, kcal, carbo, prot, fat, weight, day, month, year));
                break;
            case LUNCH_POSITION:
                WorkWithFirebaseDB.
                        addLunch(new Lunch(name, urlOfImage, kcal, carbo, prot, fat, weight, day, month, year));
                break;
            case DINNER_POSITION:
                WorkWithFirebaseDB.
                        addDinner(new Dinner(name, urlOfImage, kcal, carbo, prot, fat, weight, day, month, year));
                break;
            case SNACK_POSITION:
                WorkWithFirebaseDB.
                        addSnack(new Snack(name, urlOfImage, kcal, carbo, prot, fat, weight, day, month, year));
                break;
        }

    }

}

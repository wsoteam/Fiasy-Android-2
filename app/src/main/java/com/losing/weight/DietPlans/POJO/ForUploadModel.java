package com.losing.weight.DietPlans.POJO;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class ForUploadModel {

    public static void upload() {
        List<DietPlan> dietPlans = new ArrayList<>();
        dietPlans.add(getDietPlan());
        dietPlans.add(getDietPlan());
        dietPlans.add(getDietPlan());

        DietsList dietsList = new DietsList("имя группы диет", "описание группы диет", dietPlans);

        List<DietPlan> dietPlans1 = new ArrayList<>();
        dietPlans1.add(getDietPlan());
        dietPlans1.add(getDietPlan());
        dietPlans1.add(getDietPlan());
        dietPlans1.add(getDietPlan());
        dietPlans1.add(getDietPlan());

        DietsList dietsList1 = new DietsList("имя группы диет", "описание группы диет", dietPlans1);

        List<DietPlan> dietPlans2 = new ArrayList<>();
        dietPlans2.add(getDietPlan());
        dietPlans2.add(getDietPlan());
        dietPlans2.add(getDietPlan());
        dietPlans2.add(getDietPlan());
        dietPlans2.add(getDietPlan());

        DietsList dietsList2 = new DietsList("имя группы диет", "описание группы диет", dietPlans2);

        List<DietsList> dietsLists = new ArrayList<>();
        dietsLists.add(dietsList);
        dietsLists.add(dietsList1);
        dietsLists.add(dietsList2);

        DietModule dietModule = new DietModule("name", dietsLists);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("PLANS");
        myRef.setValue(dietModule);

    }

    public static DietPlan getDietPlan(){
        return new DietPlan("имя плана питания", 0, "описание диеты", "ссылка на изображение", "метка", true, "");
    }
}

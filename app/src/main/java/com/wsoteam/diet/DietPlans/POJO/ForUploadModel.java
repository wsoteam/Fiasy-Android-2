package com.wsoteam.diet.DietPlans.POJO;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.wsoteam.diet.Config;

import java.util.ArrayList;
import java.util.List;

public class ForUploadModel {

    public static void upload() {
        List<DietPlan> dietPlans = new ArrayList<>();
        dietPlans.add(new DietPlan("имя плана питания", 0, "описание диеты", "ссылка на изображение", "метка"));
        dietPlans.add(new DietPlan("имя плана питания", 0, "описание диеты", "ссылка на изображение", "метка"));
        dietPlans.add(new DietPlan("имя плана питания", 0, "описание диеты", "ссылка на изображение", "метка"));

        DietsList dietsList = new DietsList("имя группы диет", "описание группы диет", dietPlans);

        List<DietPlan> dietPlans1 = new ArrayList<>();
        dietPlans1.add(new DietPlan("имя плана питания", 0, "описание диеты", "ссылка на изображение", "метка"));
        dietPlans1.add(new DietPlan("имя плана питания", 0, "описание диеты", "ссылка на изображение", "метка"));
        dietPlans1.add(new DietPlan("имя плана питания", 0, "описание диеты", "ссылка на изображение", "метка"));
        dietPlans1.add(new DietPlan("имя плана питания", 0, "описание диеты", "ссылка на изображение", "метка"));
        dietPlans1.add(new DietPlan("имя плана питания", 0, "описание диеты", "ссылка на изображение", "метка"));

        DietsList dietsList1 = new DietsList("имя группы диет", "описание группы диет", dietPlans1);

        List<DietPlan> dietPlans2 = new ArrayList<>();
        dietPlans2.add(new DietPlan("имя плана питания", 0, "описание диеты", "ссылка на изображение", "метка"));
        dietPlans2.add(new DietPlan("имя плана питания", 0, "описание диеты", "ссылка на изображение", "метка"));
        dietPlans2.add(new DietPlan("имя плана питания", 0, "описание диеты", "ссылка на изображение", "метка"));
        dietPlans2.add(new DietPlan("имя плана питания", 0, "описание диеты", "ссылка на изображение", "метка"));
        dietPlans2.add(new DietPlan("имя плана питания", 0, "описание диеты", "ссылка на изображение", "метка"));

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
}

package com.wsoteam.diet.presentation.food.template.browse;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.wsoteam.diet.BranchOfAnalyzer.POJOFoodSQL.Food;
import com.wsoteam.diet.BranchOfAnalyzer.templates.POJO.FoodTemplate;
import com.wsoteam.diet.R;
import com.wsoteam.diet.presentation.food.adapter.FoodTemplateAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BrowseFoodTemplateFragment  extends MvpAppCompatFragment implements BrowseFoodTemplateView  {

    @BindView(R.id.recycler) RecyclerView recyclerView;
    FoodTemplateAdapter adapter;

    List<FoodTemplate> foodTemplates = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_browse_food_template, container, false);
        ButterKnife.bind(this, view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new FoodTemplateAdapter(getContext());
        adapter.setListContent(foodTemplates);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);

       foodTemplates.add(createTemplate());
        foodTemplates.add(createTemplate());
        foodTemplates.add(createTemplate());
        foodTemplates.add(createTemplate());
        foodTemplates.add(createTemplate());

        return view;
    }

    FoodTemplate createTemplate(){
        FoodTemplate foodTemplate = new FoodTemplate();
        List<Food> foods = new ArrayList<>();
        foods.add(foodFactory());
        foods.add(foodFactory());
        foods.add(foodFactory());
        foods.add(foodFactory());

        foodTemplate.setFoodList(foods);
        foodTemplate.setName("Test");
        foodTemplate.setEating("Ужин");

        return foodTemplate;
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

    @Override
    public void showProgress(boolean show) {

    }

    @Override
    public void showMessage(String message) {

    }
}

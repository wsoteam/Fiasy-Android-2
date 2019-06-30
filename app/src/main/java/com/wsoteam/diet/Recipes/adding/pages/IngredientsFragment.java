package com.wsoteam.diet.Recipes.adding.pages;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wsoteam.diet.BranchOfAnalyzer.POJOFoodSQL.Food;
import com.wsoteam.diet.Config;
import com.wsoteam.diet.R;
import com.wsoteam.diet.Recipes.adding.AddingRecipeActivity;
import com.wsoteam.diet.Recipes.adding.IngredientAdapter;
import com.wsoteam.diet.Recipes.adding.ProductSearchActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class IngredientsFragment extends Fragment {

    @BindView(R.id.rvIngredients) RecyclerView recyclerView;
    private List<Food> foodList;
    private IngredientAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_adding_recipe_ingredients,
                container, false);
        ButterKnife.bind(this, view);
        foodList = ((AddingRecipeActivity) getActivity()).getFoods();
        adapter = new IngredientAdapter(getContext(), foodList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        return view;
    }

    @OnClick({R.id.btnAddIngredient})
    public void onViewClicked(View view) {
        Intent intent = new Intent(getActivity(), ProductSearchActivity.class);
        startActivityForResult(intent, Config.RECIPE_FOOD_FOR_RESULT_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Config.RECIPE_FOOD_FOR_RESULT_CODE:
                Log.d("kkk", "onActivityResult: fragment FOOD_FOR_RESULT_CODE");
                if (resultCode == getActivity().RESULT_OK) {

                    Bundle bundle = data.getExtras();
                    if (bundle != null) {
                        List<Food> foods = (List<Food>) bundle.get(Config.RECIPE_FOOD_INTENT);
                        for (Food f:
                             foods) {
                            Log.d("kkk", "onActivityResult: " + f.getName());
                            foodList.add(f);
                        }
                        adapter.notifyDataSetChanged();
                        ((AddingRecipeActivity) getActivity()).updateUI();
                    }
                }
                break;

        }
    }
}

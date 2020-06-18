package com.losing.weight.Recipes.adding.pages;


import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.losing.weight.BranchOfAnalyzer.POJOFoodSQL.Food;
import com.losing.weight.Config;
import com.losing.weight.R;
import com.losing.weight.Recipes.adding.AddingRecipeActivity;
import com.losing.weight.Recipes.adding.IngredientAdapter;
import com.losing.weight.Recipes.adding.ProductSearchActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class IngredientsFragment extends Fragment {

    @BindView(R.id.rvIngredients) RecyclerView recyclerView;
    private List<Food> foodList;
    private RecyclerView.Adapter adapter;

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

                if (resultCode == getActivity().RESULT_OK) {

                    Bundle bundle = data.getExtras();
                    if (bundle != null) {
                        List<Food> foods = (List<Food>) bundle.get(Config.RECIPE_FOOD_INTENT);
                        for (Food f :
                                foods) {
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

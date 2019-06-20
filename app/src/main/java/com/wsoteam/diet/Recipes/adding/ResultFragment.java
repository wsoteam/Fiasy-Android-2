package com.wsoteam.diet.Recipes.adding;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.wsoteam.diet.BranchOfAnalyzer.POJOFoodSQL.Food;
import com.wsoteam.diet.R;
import com.wsoteam.diet.Recipes.POJO.RecipeItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ResultFragment extends Fragment {


    @BindView(R.id.mainLayout) LinearLayout mainLayout;
    @BindView(R.id.ingredientsLayout) LinearLayout ingredientsLayout;
    @BindView(R.id.instructionsLayout) LinearLayout instructionsLayout;
    @BindView(R.id.tvNameRecipe) TextView nameTextView;
    @BindView(R.id.tvTimeRecipe) TextView timeTextView;
    @BindView(R.id.tvComplexity) TextView complexityTextView;

    RecipeItem recipeItem;
    List<Food> foodList;

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {

        super.onStop();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_adding_recipe_result, container, false);
        ButterKnife.bind(this, view);

        recipeItem = ((AddingRecipeActivity)getActivity()).getRecipeItem();
        foodList = ((AddingRecipeActivity) getActivity()).getFoods();

        mainLayout.setOnClickListener((View.OnClickListener) getActivity());
        ingredientsLayout.setOnClickListener((View.OnClickListener) getActivity());
        instructionsLayout.setOnClickListener((View.OnClickListener) getActivity());

        return view;
    }


    public void updateUI() {
        super.onResume();
        nameTextView.setText(recipeItem.getName());
        timeTextView.setText(String.valueOf(recipeItem.getTime()));
        complexityTextView.setText(String.valueOf(recipeItem.getComplexity()));

        ingredientsLayout.removeAllViews();
        for (Food food:
             foodList) {
            onAddField(food);
        }

        for (String string: recipeItem.getInstruction()){
            Log.d("testresult", "updateUI: " + string);
        }

    }

    private void onAddField(Food food){
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.adding_recipe_ingredients_item, null);
        TextView nameTextView = rowView.findViewById(R.id.tvName);
        TextView portionTextView = rowView.findViewById(R.id.tvPortion);
        TextView caloriesTextView = rowView.findViewById(R.id.tvCalories);

        nameTextView.setText(food.getName());
        portionTextView.setText(String.valueOf((int)food.getPortion()) + " г");
        caloriesTextView.setText(String.valueOf((int)food.getCalories()) + " Ккал");

        ingredientsLayout.addView(rowView);

    }


}

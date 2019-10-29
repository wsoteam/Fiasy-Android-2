package com.wsoteam.diet.Recipes.adding.pages;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wsoteam.diet.BranchOfAnalyzer.POJOFoodSQL.Food;
import com.wsoteam.diet.R;
import com.wsoteam.diet.Recipes.POJO.RecipeItem;
import com.wsoteam.diet.Recipes.adding.AddingRecipeActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ResultFragment extends Fragment {
    
    @BindView(R.id.mainLayout) LinearLayout mainLayout;
    @BindView(R.id.ingredientsLayoutIn) LinearLayout ingredientsLayout;
    @BindView(R.id.instructionsLayoutIn) LinearLayout instructionsLayout;
    @BindView(R.id.tvNameRecipe) TextView nameTextView;
    @BindView(R.id.tvTimeRecipe) TextView timeTextView;
    @BindView(R.id.tvComplexity) TextView complexityTextView;

    RecipeItem recipeItem;
    List<Food> foodList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_adding_recipe_result, container, false);
        ButterKnife.bind(this, view);

        recipeItem = ((AddingRecipeActivity) getActivity()).getRecipeItem();
        foodList = ((AddingRecipeActivity) getActivity()).getFoods();
        mainLayout.setOnClickListener((View.OnClickListener) getActivity());
        ingredientsLayout.setOnClickListener((View.OnClickListener) getActivity());
        instructionsLayout.setOnClickListener((View.OnClickListener) getActivity());

        return view;
    }


    public void updateUI() {
        nameTextView.setText(recipeItem.getName());
        timeTextView.setText(String.valueOf(recipeItem.getTime()));
        complexityTextView.setText(String.valueOf(recipeItem.getComplexity()));

        ingredientsLayout.removeAllViews();
        instructionsLayout.removeAllViews();

        for (String str :
                recipeItem.getInstruction()) {
            onAddFieldInstruction(str);
        }

        for (Food food :
                foodList) {
            onAddFieldIngredient(food);
        }

    }

    private void onAddFieldIngredient(Food food) {
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.adding_recipe_ingredients_result, null);
        TextView nameTextView = rowView.findViewById(R.id.tvIngredintName);
        TextView portionTextView = rowView.findViewById(R.id.tvIngredientPortion);

        nameTextView.setText(food.getName());
        portionTextView.setText(String.format(getString(R.string.n_g), (int)food.getPortion()));
        ingredientsLayout.addView(rowView);
    }

    private void onAddFieldInstruction(String str) {
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.adding_recipe_instructions_result, null);
        TextView textView = rowView.findViewById(R.id.tvInstruction);
        textView.setText(str);
        instructionsLayout.addView(rowView);
    }
}

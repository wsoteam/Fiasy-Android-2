package com.wsoteam.diet.Recipes.adding;

import android.content.Context;
import android.content.Intent;
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
import com.wsoteam.diet.Config;
import com.wsoteam.diet.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class IngredientsFragment extends Fragment {

    @BindView(R.id.svContainerInstructions)
    LinearLayout container;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_adding_recipe_ingredients,
                container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @OnClick({R.id.btnAddIngredient})
    public void onViewClicked(View view) {
        Log.d("testresult", "btn");
        Intent intent = new Intent(getActivity(), ProductSearchActivity.class);
        startActivityForResult(intent, Config.RECIPE_FOOD_FOR_RESULT_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Config.RECIPE_FOOD_FOR_RESULT_CODE:
                if (resultCode == getActivity().RESULT_OK) {

                    Bundle bundle = data.getExtras();
                   if (bundle != null){
                       Food food = (Food) bundle.get(Config.RECIPE_FOOD_INTENT);
                       Log.d("testresult", "onActivityResult: " + food.getName());
                       onAddField(food);
                   }


                }
                break;

        }
    }

    private void onAddField(Food food){
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.adding_recipe_instruction_item, null);
        TextView nameTextView = rowView.findViewById(R.id.tvName);
        TextView portionTextView = rowView.findViewById(R.id.tvPortion);
        TextView caloriesTextView = rowView.findViewById(R.id.tvCalories);

        nameTextView.setText(food.getName());
        portionTextView.setText(String.valueOf((int)food.getPortion()) + " г");
        caloriesTextView.setText(String.valueOf((int)food.getCalories()) + " Ккал");

        container.addView(rowView);
    }
}

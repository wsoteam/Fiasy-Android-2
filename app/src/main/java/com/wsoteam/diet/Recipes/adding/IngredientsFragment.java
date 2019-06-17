package com.wsoteam.diet.Recipes.adding;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wsoteam.diet.BranchOfAnalyzer.POJOFoodSQL.Food;
import com.wsoteam.diet.Config;
import com.wsoteam.diet.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class IngredientsFragment extends Fragment {



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
                       
                   }


                }
                break;

        }
    }
}

package com.wsoteam.diet.Recipes.adding;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;

import com.wsoteam.diet.R;
import com.wsoteam.diet.Recipes.POJO.RecipeItem;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainFragment extends Fragment {

    @BindView(R.id.tvNameRecipe) EditText nameEditText;
    @BindView(R.id.etPortionsRecipe) EditText portionsEditText;
    @BindView(R.id.etTimeRecipe) EditText timeEditText;
    @BindView(R.id.sComplexityRecipe) Spinner complexitySpinner;
    @BindView(R.id.switchShareRecipe) Switch shareSwitch;

    private RecipeItem recipeItem;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_adding_recipe_main, container, false);
        ButterKnife.bind(this, view);

        recipeItem = ((AddingRecipeActivity)getActivity()).getRecipeItem();
        recipeItem.setName("Жаба в кляре");

        if (recipeItem.getName() != null && !recipeItem.getName().equals("")){
            nameEditText.setText(recipeItem.getName());
        }

        portionsEditText.setText(String.valueOf(recipeItem.getPortions()));
        timeEditText.setText(String.valueOf(recipeItem.getTime()));

        nameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                recipeItem.setName(nameEditText.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        complexitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return view;
    }
}

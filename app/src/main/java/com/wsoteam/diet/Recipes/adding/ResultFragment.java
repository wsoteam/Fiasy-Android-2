package com.wsoteam.diet.Recipes.adding;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.wsoteam.diet.Config;
import com.wsoteam.diet.R;
import com.wsoteam.diet.Recipes.POJO.RecipeItem;
import com.wsoteam.diet.Sync.WorkWithFirebaseDB;

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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_adding_recipe_result, container, false);
        ButterKnife.bind(this, view);

        recipeItem = ((AddingRecipeActivity)getActivity()).getRecipeItem();


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

    }


}

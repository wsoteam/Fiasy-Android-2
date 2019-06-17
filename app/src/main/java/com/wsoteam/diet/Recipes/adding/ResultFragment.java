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

import com.wsoteam.diet.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ResultFragment extends Fragment {


    @BindView(R.id.mainLayout) LinearLayout mainLayout;
    @BindView(R.id.ingredientsLayout) LinearLayout ingredientsLayout;
    @BindView(R.id.instructionsLayout) LinearLayout instructionsLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_adding_recipe_result, container, false);
        ButterKnife.bind(this, view);

        getActivity().setTitle("gggggggggggggggggg");

        mainLayout.setOnClickListener((View.OnClickListener) getActivity());
        ingredientsLayout.setOnClickListener((View.OnClickListener) getActivity());
        instructionsLayout.setOnClickListener((View.OnClickListener) getActivity());

        return view;
    }
}

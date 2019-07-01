package com.wsoteam.diet.Recipes.adding.pages;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wsoteam.diet.R;
import com.wsoteam.diet.Recipes.POJO.RecipeItem;
import com.wsoteam.diet.Recipes.adding.AddInstructionAlertDialog;
import com.wsoteam.diet.Recipes.adding.AddingRecipeActivity;
import com.wsoteam.diet.Recipes.adding.InstructionAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class InstructionsFragment extends Fragment {

    @BindView(R.id.rvInstructions) RecyclerView recyclerView;

    private RecipeItem recipeItem;
    private List<String> instructions;
    private RecyclerView.Adapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_adding_recipe_instructions,
                container, false);
        ButterKnife.bind(this, view);

        recipeItem = ((AddingRecipeActivity) getActivity()).getRecipeItem();
        instructions = recipeItem.getInstruction();
        adapter = new InstructionAdapter(getContext(), instructions);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        return view;
    }

    @OnClick({R.id.btnAddStep})
    public void onViewClicked(View view) {

        if (instructions.size() < 99) {
            AddInstructionAlertDialog.init(adapter, getContext(), instructions);
        }
    }
}

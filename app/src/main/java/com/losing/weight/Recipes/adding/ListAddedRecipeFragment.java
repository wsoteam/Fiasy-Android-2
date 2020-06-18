package com.losing.weight.Recipes.adding;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.losing.weight.BranchOfAnalyzer.TabsFragment;
import com.losing.weight.R;
import com.losing.weight.Recipes.POJO.RecipeItem;
import com.losing.weight.Recipes.helper.FragmentRecipeContainer;
import com.losing.weight.Sync.UserDataHolder;
import com.losing.weight.common.Analytics.EventProperties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ListAddedRecipeFragment extends Fragment implements TabsFragment {

    @BindView(R.id.layoutWithButton) ConstraintLayout layout;
    @BindView(R.id.rvRecipes) RecyclerView recyclerView;

    List<RecipeItem> list;

    @Override
    public void onResume() {
        initial();

        String str = ((FragmentRecipeContainer) getParentFragment()).getSearchKey();
        if (str != null && !str.equals("")) {
            search(str);
        }
        super.onResume();

    }

    @Override
    public void sendClearSearchField() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_added_recipe,
                container, false);
        ButterKnife.bind(this, view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return view;
    }

    @OnClick({R.id.btnAddRecipe})
    public void onViewClicked(View view) {
        Intent intent = new Intent(getActivity(), AddingRecipeActivity.class).putExtra(EventProperties.recipe_from, EventProperties.recipe_from_button);
        startActivity(intent);
    }

    @Override
    public void sendString(String searchString) {
        search(searchString);
    }


    private void updateUI(List<RecipeItem> recipeItems) {
        if (recipeItems == null || recipeItems.size() == 0) {
            recyclerView.setAdapter(null);
        } else {
            recyclerView.setAdapter(new AddedRecipeAdapter(recipeItems, getActivity(), getContext()));
        }
    }

    private void initial() {
        if (UserDataHolder.getUserData() != null && UserDataHolder.getUserData().getRecipes() != null) {
            HashMap<String, RecipeItem> recipesHashMap = UserDataHolder.getUserData().getRecipes();
            list = new ArrayList<>(recipesHashMap.values());
            updateUI(list);
            layout.setVisibility(View.INVISIBLE);
        } else {
            layout.setVisibility(View.VISIBLE);
            updateUI(null);
        }
    }

    private void search(String str) {
        List<RecipeItem> result = new ArrayList<>();
        String key;

        if (str != null) {
            key = str.toLowerCase();
        } else {
            key = null;
        }

        if (key == null || key.equals("") || list == null) {
            updateUI(list);
        } else {
            for (RecipeItem recipe : list) {
                if (recipe.getName() != null && recipe.getName().toLowerCase().contains(key)) {
                    result.add(recipe);
                }
            }
            updateUI(result);
        }
    }
}

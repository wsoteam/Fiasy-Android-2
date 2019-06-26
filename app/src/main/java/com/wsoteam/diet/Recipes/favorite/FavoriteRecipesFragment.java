package com.wsoteam.diet.Recipes.favorite;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wsoteam.diet.BranchOfAnalyzer.TabsFragment;
import com.wsoteam.diet.R;
import com.wsoteam.diet.Recipes.POJO.RecipeItem;
import com.wsoteam.diet.Recipes.adding.AddedRecipeAdapter;
import com.wsoteam.diet.Recipes.v2.ListRecipesAdapter;
import com.wsoteam.diet.Sync.UserDataHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavoriteRecipesFragment extends Fragment implements TabsFragment {

    @BindView(R.id.rvRecipes) RecyclerView recyclerView;
    @BindView(R.id.layoutWithButton) ConstraintLayout layout;

    private HashMap<String, RecipeItem> favoriteRecipes;
    private ListRecipesAdapter adapter;

    @Override
    public void onResume() {

        initial();
        super.onResume();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_favorite_recipes, container, false);
        ButterKnife.bind(this, view);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        return view;
    }

    @Override
    public void sendString(String searchString) {
        search(searchString);
    }

    private void initial() {
        if (UserDataHolder.getUserData() != null && UserDataHolder.getUserData().getFavoriteRecipes() != null) {
            layout.setVisibility(View.INVISIBLE);
            this.favoriteRecipes = UserDataHolder.getUserData().getFavoriteRecipes();
            adapter = new ListRecipesAdapter(new ArrayList<RecipeItem>(favoriteRecipes.values()), getActivity());
            recyclerView.setAdapter(adapter);
        } else {
            layout.setVisibility(View.VISIBLE);
            recyclerView.setAdapter(null);
        }
    }

    private void search(String str) {
        String key;
        if (str != null){
            key = str.toLowerCase();
        } else {
            key = null;
        }

        List<RecipeItem> result = new ArrayList<>();
        List<RecipeItem> list;

        if (favoriteRecipes != null) {
            list = new ArrayList<RecipeItem>(favoriteRecipes.values());
        } else {
            list = null;
        }

        if (key == null || key.equals("") || list == null) {
            initial();
        } else {
            for (RecipeItem recipe : list) {
                if (recipe.getName() != null && recipe.getName().toLowerCase().contains(key)) {
                    result.add(recipe);
                }
            }
            adapter = new ListRecipesAdapter(result, getActivity());
            recyclerView.setAdapter(adapter);
        }
    }
}

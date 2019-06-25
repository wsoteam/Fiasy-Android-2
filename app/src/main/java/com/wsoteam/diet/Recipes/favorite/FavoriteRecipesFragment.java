package com.wsoteam.diet.Recipes.favorite;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wsoteam.diet.R;
import com.wsoteam.diet.Recipes.POJO.RecipeItem;
import com.wsoteam.diet.Recipes.v2.ListRecipesAdapter;
import com.wsoteam.diet.Sync.UserDataHolder;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavoriteRecipesFragment extends Fragment {

    @BindView(R.id.rvRecipes) RecyclerView recyclerView;

    private HashMap<String, RecipeItem> favoriteRecipes;
    private ListRecipesAdapter adapter;

    @Override
    public void onResume() {
        if (UserDataHolder.getUserData() != null && UserDataHolder.getUserData().getFavoriteRecipes() != null) {
            this.favoriteRecipes = UserDataHolder.getUserData().getFavoriteRecipes();
            adapter = new ListRecipesAdapter(new ArrayList<RecipeItem>(favoriteRecipes.values()), getActivity());
            recyclerView.setAdapter(adapter);
        } else {
            recyclerView.setAdapter(null);
        }
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
}

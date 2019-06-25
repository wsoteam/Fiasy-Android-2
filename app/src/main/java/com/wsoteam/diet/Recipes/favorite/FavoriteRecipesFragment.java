package com.wsoteam.diet.Recipes.favorite;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wsoteam.diet.R;
import com.wsoteam.diet.Recipes.POJO.RecipeItem;
import com.wsoteam.diet.Sync.UserDataHolder;

import java.util.HashMap;
import java.util.List;

public class FavoriteRecipesFragment extends Fragment {

    private HashMap<String, RecipeItem> favoriteRecipes;

    @Override
    public void onResume() {
        if (UserDataHolder.getUserData() != null) {
            this.favoriteRecipes = UserDataHolder.getUserData().getFavoriteRecipes();
        }
        super.onResume();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_favorite_recipes, container, false);

        return view;
    }
}

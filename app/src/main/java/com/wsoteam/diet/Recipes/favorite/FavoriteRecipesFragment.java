package com.wsoteam.diet.Recipes.favorite;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wsoteam.diet.BranchOfAnalyzer.TabsFragment;
import com.wsoteam.diet.R;
import com.wsoteam.diet.Recipes.POJO.RecipeItem;
import com.wsoteam.diet.Recipes.helper.FragmentRecipeContainer;
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

    private List<RecipeItem> list;
    private ListRecipesAdapter adapter;

    @Override
    public void sendClearSearchField() {

    }

    @Override
    public void onResume() {
        initial();
        updateUI(list);
        String str = ((FragmentRecipeContainer)getParentFragment()).getSearchKey();
        if (str != null && !str.equals("")) {
            search(str);
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

    @Override
    public void sendString(String searchString) {
        search(searchString);
    }

    private void initial() {
        if (UserDataHolder.getUserData() != null && UserDataHolder.getUserData().getFavoriteRecipes() != null) {
            HashMap<String, RecipeItem> favoriteRecipes = UserDataHolder.getUserData().getFavoriteRecipes();
            list = new ArrayList<>(favoriteRecipes.values());
            layout.setVisibility(View.INVISIBLE);
        } else {
            layout.setVisibility(View.VISIBLE);
            list = null;
        }
    }

    private void updateUI(List<RecipeItem> recipeItemList){
        if (recipeItemList == null || recipeItemList.size() == 0) {
            recyclerView.setAdapter(null);
        } else {
            adapter = new ListRecipesAdapter(recipeItemList);
            recyclerView.setAdapter(adapter);
        }

    }

    private void search(String str) {
        List<RecipeItem> result = new ArrayList<>();
        initial();

        String key;
        if (str != null){
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

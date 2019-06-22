package com.wsoteam.diet.Recipes.adding;

import android.content.Intent;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wsoteam.diet.BranchOfAnalyzer.POJOFoodSQL.Food;
import com.wsoteam.diet.Config;
import com.wsoteam.diet.ObjectHolder;
import com.wsoteam.diet.POJOS.ItemRecipes;
import com.wsoteam.diet.POJOS.ListOfGroupsRecipes;
import com.wsoteam.diet.R;
import com.wsoteam.diet.Recipes.POJO.ListRecipes;
import com.wsoteam.diet.Recipes.POJO.RecipeItem;
import com.wsoteam.diet.Recipes.v1.ListRecipesAdapter;
import com.wsoteam.diet.Sync.UserDataHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ListAddedRecipeFragment extends Fragment {

    @BindView(R.id.layoutWithButton) ConstraintLayout layout;
    @BindView(R.id.rvRecipes) RecyclerView recyclerView;

    List<RecipeItem> list;
    HashMap<String, RecipeItem> recipesHashMap;

    @Override
    public void onResume() {
        super.onResume();
        initial();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_added_recipe,
                container, false);
        ButterKnife.bind(this, view);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        return view;
    }

    @OnClick({R.id.btnAddRecipe})
    public void onViewClicked(View view) {
        Intent intent = new Intent(getActivity(), AddingRecipeActivity.class);
        startActivity(intent);
    }


    private void updateUI(List<RecipeItem> recipeItems){
        if (recipeItems == null || recipeItems.size() ==0){
            recyclerView.setAdapter(null);
            list = new ArrayList<>();
            layout.setVisibility(View.VISIBLE);
            Log.d("fr", "onCreateView: NULL");
        } else {
            Log.d("fr", "onCreateView: OK");
            layout.setVisibility(View.INVISIBLE);
            recyclerView.setAdapter(new AddedRecipeAdapter(recipeItems, getActivity(), getContext()));
        }
    }

    private void initial(){
        if (UserDataHolder.getUserData() != null && UserDataHolder.getUserData().getRecipes() != null){
            recipesHashMap = UserDataHolder.getUserData().getRecipes();
            list = new ArrayList<>(recipesHashMap.values());
            updateUI(list);
        } else {
            updateUI(null);
        }
    }

    private void search(String str){
        String key = str.toLowerCase();
        List<RecipeItem> result = new ArrayList<>();

        if (key.equals("") || list == null){
           initial();
        } else {
                for (RecipeItem recipe : list) {
                    if (recipe.getName().toLowerCase().contains(key)) {
                        result.add(recipe);
                    }
                    recyclerView.setAdapter(new AddedRecipeAdapter(result, getActivity(), getContext()));
            }
        }
    }
}

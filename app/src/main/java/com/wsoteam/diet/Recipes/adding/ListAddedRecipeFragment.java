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
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ListAddedRecipeFragment extends Fragment {

    @BindView(R.id.layoutWithButton) ConstraintLayout layout;
    @BindView(R.id.rvRecipes) RecyclerView recyclerView;

    List<RecipeItem> list;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_added_recipe,
                container, false);
        ButterKnife.bind(this, view);

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.setAdapter(null);

        HashMap<String, RecipeItem> recipes = UserDataHolder.getUserData().getRecipes();

       if (recipes == null){
           recyclerView.setAdapter(null);
           Log.d("fr", "onCreateView: NULL");
       } else {

           Log.d("fr", "onCreateView: OK");
           list = new ArrayList<>(recipes.values());
           updateUI(list);
       }
        return view;
    }

    @OnClick({R.id.btnAddRecipe})
    public void onViewClicked(View view) {
        Intent intent = new Intent(getActivity(), AddingRecipeActivity.class);
        startActivityForResult(intent, Config.ADD_NEW_RECIPE_RESULT_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Config.ADD_NEW_RECIPE_RESULT_CODE:
                if (resultCode == getActivity().RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    if (bundle != null){
                        RecipeItem recipeItem = (RecipeItem) bundle.get(Config.RECIPE_ITEM_INTENT);
                        Log.d("fr", "onActivityResult: " + recipeItem.getName());
                        if (list != null){
                            list.add(0, recipeItem);
                            updateUI(list);
                        }
                    }
                }
                break;
        }
    }

    private void updateUI(List<RecipeItem> recipeItems){
        recyclerView.setAdapter(new AddedRecipeAdapter(recipeItems, getActivity(), getContext()));
    }

}

package com.wsoteam.diet.Recipes.helper;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import com.wsoteam.diet.BranchOfAnalyzer.TabsFragment;
import com.wsoteam.diet.R;
import com.wsoteam.diet.Recipes.adding.ListAddedRecipeFragment;
import com.wsoteam.diet.Recipes.favorite.FavoriteRecipesFragment;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class FragmentRecipeContainer extends Fragment implements TabsFragment {

    private Fragment favoriteRecipes;
    private Fragment addedRecipes;
    private TabsFragment tabsFragment;
    private String searchKey = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_container, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick({R.id.radio0, R.id.radio1})
    public void onRadioButtonClicked(RadioButton radioButton) {
        boolean checked = radioButton.isChecked();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();

        switch (radioButton.getId()) {
            case R.id.radio0:
                if (checked) {
                    transaction.replace(R.id.childContainer, favoriteRecipes).commit();
                    tabsFragment = (TabsFragment) favoriteRecipes;
                }
                break;
            case R.id.radio1:
                if (checked) {
                    transaction.replace(R.id.childContainer, addedRecipes).commit();
                    tabsFragment = (TabsFragment) addedRecipes;
                }
                break;
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        favoriteRecipes = new FavoriteRecipesFragment();
        addedRecipes = new ListAddedRecipeFragment();
        tabsFragment = (TabsFragment) favoriteRecipes;
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.childContainer, favoriteRecipes).commit();
    }

    @Override
    public void sendString(String searchString) {
        this.searchKey = searchString;
        tabsFragment.sendString(searchKey);
    }

    public String getSearchKey() {
        return searchKey;
    }
}

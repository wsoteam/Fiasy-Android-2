package com.wsoteam.diet.Recipes.helper;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.wsoteam.diet.BranchOfAnalyzer.TabsFragment;
import com.wsoteam.diet.R;
import com.wsoteam.diet.Recipes.adding.ListAddedRecipeFragment;
import com.wsoteam.diet.Recipes.favorite.FavoriteRecipesFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FragmentRecipeContainer extends Fragment implements TabsFragment {

    @BindView(R.id.radio_button_group) RadioGroup radioGroup;

    private Fragment favoriteRecipes;
    private Fragment addedRecipes;
    private TabsFragment tabsFragment;
    private String searchKey = "";

    @Override
    public void sendClearSearchField() {
        sendString("");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_container, container, false);
        ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        switch (radioGroup.getCheckedRadioButtonId()) {
            case R.id.radio0:
                    transaction.replace(R.id.childContainer, favoriteRecipes).commit();
                    tabsFragment = (TabsFragment) favoriteRecipes;
                break;
            case R.id.radio1:
                    transaction.replace(R.id.childContainer, addedRecipes).commit();
                    tabsFragment = (TabsFragment) addedRecipes;

                break;
        }

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

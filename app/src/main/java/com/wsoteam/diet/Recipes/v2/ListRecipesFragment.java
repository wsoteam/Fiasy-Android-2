package com.wsoteam.diet.Recipes.v2;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.amplitude.api.Amplitude;
import com.wsoteam.diet.AmplitudaEvents;
import com.wsoteam.diet.Config;
import com.wsoteam.diet.R;
import com.wsoteam.diet.Recipes.POJO.GroupsHolder;
import com.wsoteam.diet.Recipes.POJO.GroupsRecipes;
import com.wsoteam.diet.Recipes.POJO.RecipeItem;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;

public class ListRecipesFragment extends Fragment {


    private RecyclerView recyclerView;
    private int position;
    private RecyclerView.LayoutManager layoutManager;
    private ListRecipesAdapter adapter;
    private Toolbar mToolbar;
    private Window window;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Bundle bundle = getArguments();
        if (bundle != null) {
            position = bundle.getInt(Config.RECIPES_BUNDLE);
            Log.d("testrt", "onCreateView: position " + position);
        }

        View view = inflater.inflate(R.layout.fragment_list_recipes_v2, container, false);

        window = getActivity().getWindow();
        window.setStatusBarColor(Color.parseColor("#BB6001"));
        mToolbar = view.findViewById(R.id.toolbar);
        mToolbar.inflateMenu(R.menu.toolbar_menu);
        mToolbar.setTitleTextColor(0xFFFFFFFF);
        Menu menu = mToolbar.getMenu();

        mToolbar.setNavigationIcon(R.drawable.back_arrow_icon_white);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        MenuItem mSearch = menu.findItem(R.id.action_search);
        SearchView mSearchView = (SearchView) mSearch.getActionView();
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                searchAndShow(s);
                return false;
            }
        });

        recyclerView = view.findViewById(R.id.rvListRecipes);
        layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);
        updateUI();
        Amplitude.getInstance().logEvent(AmplitudaEvents.view_group_recipes);
        return view;
    }

    private void updateUI() {
        mToolbar.setTitle(GroupsHolder.getGroupsRecipes().getGroups().get(position).getName());
        adapter = new ListRecipesAdapter(GroupsHolder.getGroupsRecipes().getGroups().get(position).getListrecipes(), getActivity());
        recyclerView.setAdapter(adapter);
    }

    public void searchAndShow(CharSequence s){
        String key = s.toString().toLowerCase();
        Set<RecipeItem> result = new LinkedHashSet<>();
        GroupsRecipes groupsRecipes = GroupsHolder.getGroupsRecipes();

        if (key.equals("")){
            recyclerView.setAdapter(adapter);
        } else {
            for (int i = 0; i < groupsRecipes.getGroups().size(); i++) {

                for (RecipeItem recipe :
                        groupsRecipes.getGroups().get(i).getListrecipes()) {
                    if (recipe.getName().toLowerCase().contains(key)) {
                        result.add(recipe);
                    }
                }
            }
            ListRecipesAdapter adapterNew = new ListRecipesAdapter(new LinkedList<>(result), getActivity());
            recyclerView.setAdapter(adapterNew);
        }

    }
}
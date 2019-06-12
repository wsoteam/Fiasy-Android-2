package com.wsoteam.diet.Recipes.v2;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.amplitude.api.Amplitude;
import com.wsoteam.diet.AmplitudaEvents;

import com.wsoteam.diet.R;
import com.wsoteam.diet.Recipes.POJO.GroupsHolder;
import com.wsoteam.diet.Recipes.POJO.GroupsRecipes;
import com.wsoteam.diet.Recipes.POJO.RecipeItem;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

public class GroupsFragment extends Fragment implements Observer {

    private String TAG = "GroupsFragment";
    private GroupsFragment groupsFragment;
    private Context context = getContext();
    private ViewGroup viewGroup;
    private RecyclerView recyclerView;
    private GroupsAdapter adapter;
    private CardView cardView;
    private RecyclerView.LayoutManager layoutManager;
    private Window window;

    private int statusBarHeight(android.content.res.Resources res) {
        return (int) (24 * res.getDisplayMetrics().density);
    }

    public int dpToPx(int dp) {
        float density = getContext().getResources()
                .getDisplayMetrics()
                .density;
        return Math.round((float) dp * density);
    }

    @Override
    public void onPause() {
        super.onPause();
        GroupsHolder.unsubscribe(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        window = getActivity().getWindow();
        window.setStatusBarColor(Color.parseColor("#BB6001"));
        groupsFragment = this;
        viewGroup = container;

        View view = inflater.inflate(R.layout.fragment_groups_recipes_v2,
                container, false);
        layoutManager = new LinearLayoutManager(getContext());
        cardView = view.findViewById(R.id.cvGroupsRecipes);

        Toolbar mToolbar = view.findViewById(R.id.toolbar);
        mToolbar.setTitle("Рецепты");
        mToolbar.inflateMenu(R.menu.toolbar_menu);
        mToolbar.setTitleTextColor(0xFFFFFFFF);

        Menu menu = mToolbar.getMenu();

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

        recyclerView = view.findViewById(R.id.rvGroupsRecipe);
        recyclerView.setLayoutManager(layoutManager);

        if (GroupsHolder.getGroupsRecipes() != null) {
            adapter = new GroupsAdapter(GroupsHolder.getGroupsRecipes().getGroups(), groupsFragment, viewGroup.getId());
            recyclerView.setAdapter(adapter);
        } else {
            GroupsHolder.subscribe(this);
            recyclerView.setAdapter(null);
        }

        Amplitude.getInstance().logEvent(AmplitudaEvents.view_all_recipes);
        return view;
    }


    public void searchAndShow(CharSequence s) {
        String key = s.toString().toLowerCase();
        Set<RecipeItem> result = new LinkedHashSet<>();
        GroupsRecipes groupsRecipes = GroupsHolder.getGroupsRecipes();
        RecyclerView.LayoutManager gridLayout = new GridLayoutManager(getContext(), 2);

        if (key.equals("")) {

            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(layoutManager);
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
            recyclerView.setLayoutManager(gridLayout);
            recyclerView.setAdapter(adapterNew);

        }

    }

    @Override
    public void update(Observable o, Object arg) {
        adapter = new GroupsAdapter(GroupsHolder.getGroupsRecipes().getGroups(), groupsFragment, viewGroup.getId());
        recyclerView.setAdapter(adapter);
        GroupsHolder.unsubscribe(this);

    }
}

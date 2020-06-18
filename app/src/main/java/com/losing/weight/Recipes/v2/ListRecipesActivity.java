package com.losing.weight.Recipes.v2;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.appbar.AppBarLayout;
import com.losing.weight.Config;
import com.losing.weight.R;
import com.losing.weight.Recipes.POJO.GroupsHolder;
import com.losing.weight.Recipes.POJO.GroupsRecipes;
import com.losing.weight.Recipes.POJO.RecipeItem;
import com.losing.weight.ads.AdVH;
import com.losing.weight.ads.AdWorker;
import com.losing.weight.ads.AdWrapperAdapter;
import com.losing.weight.ads.FiasyAds;
import com.losing.weight.common.Analytics.Events;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;

public class ListRecipesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private int position;
    private ListRecipesAdapter adapter;
    ListRecipesAdapter adapterSearch = new ListRecipesAdapter(null);
    private Toolbar mToolbar;

    @BindView(R.id.appbar)
    AppBarLayout appBarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_recipes);

        ButterKnife.bind(this);
        appBarLayout.setLiftable(true);
        Intent intent = getIntent();
        position = intent.getIntExtra(Config.RECIPES_BUNDLE, 0);

        mToolbar = findViewById(R.id.toolbar);
        mToolbar.inflateMenu(R.menu.menu_recipe);
        Menu menu = mToolbar.getMenu();

        mToolbar.setNavigationIcon(R.drawable.back_arrow_icon);
        mToolbar.setNavigationOnClickListener(view -> onBackPressed());

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

        recyclerView = findViewById(R.id.rvListRecipes);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager linearLayoutManager1 = (LinearLayoutManager) recyclerView.getLayoutManager();

                int firstVisibleItem = 0;
                if (linearLayoutManager1 != null)
                    firstVisibleItem = linearLayoutManager1.findFirstVisibleItemPosition();
                appBarLayout.setLiftable(firstVisibleItem == 0);
            }
        });
        updateUI();
    }

    private void updateUI() {
        Events.logChoiseRecipeCategory(GroupsHolder.getGroupsRecipes().getGroups().get(position).getName());
        mToolbar.setTitle(GroupsHolder.getGroupsRecipes().getGroups().get(position).getName());
        adapter = new ListRecipesAdapter(GroupsHolder.getGroupsRecipes().getGroups().get(position).getListrecipes());
        AdWrapperAdapter wrapperAdapter = new AdWrapperAdapter(adapter, 5, AdVH::new);
        wrapperAdapter.insertAds(AdWorker.INSTANCE.getNativeAds());
        recyclerView.setAdapter(wrapperAdapter);
    }

    public void searchAndShow(CharSequence s) {
        String key = s.toString().toLowerCase();
        Set<RecipeItem> result = new LinkedHashSet<>();
        GroupsRecipes groupsRecipes = GroupsHolder.getGroupsRecipes();

        if (key.equals("")) {
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
            adapterSearch.setData(new LinkedList<>(result));
            recyclerView.setAdapter(adapterSearch);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (recyclerView.getAdapter() != null)
            recyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        FiasyAds.openInter(this);
        super.onBackPressed();
    }
}

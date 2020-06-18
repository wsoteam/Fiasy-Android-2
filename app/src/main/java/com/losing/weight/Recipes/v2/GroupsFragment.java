package com.losing.weight.Recipes.v2;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import com.google.android.material.appbar.AppBarLayout;
import com.losing.weight.AmplitudaEvents;
import com.losing.weight.Authenticate.POJO.Box;
import com.losing.weight.Config;
import com.losing.weight.InApp.ActivitySubscription;
import com.losing.weight.R;
import com.losing.weight.Recipes.POJO.GroupsHolder;
import com.losing.weight.Recipes.POJO.GroupsRecipes;
import com.losing.weight.Recipes.POJO.RecipeItem;
import com.losing.weight.common.Analytics.EventProperties;
import com.losing.weight.presentation.auth.AuthUtil;
import com.losing.weight.utils.Subscription;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

public class GroupsFragment extends Fragment implements Observer {

    private GroupsFragment groupsFragment;
    private ViewGroup viewGroup;
    private RecyclerView recyclerView;
    private GroupsAdapter adapter;
    ListRecipesAdapter adapterSearch;

    @BindView(R.id.appbar) AppBarLayout appBarLayout;
    @BindView(R.id.recipeLogIn) Button recipeLogIn;
    @BindView(R.id.recipePremium) View recipePremium;

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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        AuthUtil.Companion.prepareLogInView(getContext(), recipeLogIn);

    }

    @OnClick(R.id.recipePremium)
    void premium(){
        Box box = new Box();
        box.setSubscribe(false);
        box.setOpenFromPremPart(true);
        box.setOpenFromIntrodaction(false);
        box.setComeFrom(AmplitudaEvents.view_prem_content);
        box.setBuyFrom(EventProperties.trial_from_recipe); // TODO проверить правильность флагов
        Intent intent = new Intent(getContext(), ActivitySubscription.class).putExtra(Config.TAG_BOX, box);
        startActivity(intent);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        window = getActivity().getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.highlight_line_color));
        groupsFragment = this;
        viewGroup = container;

        View view = inflater.inflate(R.layout.fragment_groups_recipes_v2,
                container, false);
        ButterKnife.bind(this, view);
        appBarLayout.setLiftable(true);

        Toolbar mToolbar = view.findViewById(R.id.toolbar);
        mToolbar.setTitle(getString(R.string.tab_lat_recipes_name));
        mToolbar.inflateMenu(R.menu.menu_recipe);

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
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
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

        if (GroupsHolder.getGroupsRecipes() != null) {
            adapter = new GroupsAdapter(GroupsHolder.getGroupsRecipes().getGroups(), groupsFragment, viewGroup.getId());
            recyclerView.setAdapter(adapter);
        } else {
            GroupsHolder.subscribe(this);
            recyclerView.setAdapter(null);
        }
        adapterSearch = new ListRecipesAdapter(null);
        return view;
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

            ListRecipesAdapter adapterNew = new ListRecipesAdapter(new LinkedList<>(result));
            recyclerView.setAdapter(adapterNew);

        }

    }

    @Override
    public void update(Observable o, Object arg) {
        adapter = new GroupsAdapter(GroupsHolder.getGroupsRecipes().getGroups(), groupsFragment, viewGroup.getId());
        recyclerView.setAdapter(adapter);
        GroupsHolder.unsubscribe(this);

    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            searchAndShow(query);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handleIntent(getActivity().getIntent());
    }

    @Override
    public void onResume() {
        super.onResume();

        Subscription.setVisibility(recipePremium);

        if (recyclerView.getAdapter() != null) recyclerView.getAdapter().notifyDataSetChanged();

    }
}

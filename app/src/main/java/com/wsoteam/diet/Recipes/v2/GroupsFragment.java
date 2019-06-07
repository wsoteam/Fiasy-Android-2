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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.amplitude.api.Amplitude;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wsoteam.diet.AmplitudaEvents;
import com.wsoteam.diet.Config;
import com.wsoteam.diet.ObjectHolder;
import com.wsoteam.diet.POJOS.ListOfGroupsRecipes;
import com.wsoteam.diet.POJOS.ListOfRecipes;
import com.wsoteam.diet.R;
import com.wsoteam.diet.Recipes.GroupsAdapter;
import com.wsoteam.diet.Recipes.GroupsAdapterNew;
import com.wsoteam.diet.Recipes.ListRecipesAdapterNew;
import com.wsoteam.diet.Recipes.POJO.EatingGroupsRecipes;
import com.wsoteam.diet.Recipes.POJO.GroupsHolder;
import com.wsoteam.diet.Recipes.POJO.GroupsRecipes;
import com.wsoteam.diet.Recipes.POJO.ListRecipes;
import com.wsoteam.diet.Recipes.POJO.RecipeItem;

import java.util.ArrayList;
import java.util.List;

public class GroupsFragment extends Fragment {

    private String TAG = "GroupsFragment";
    private com.wsoteam.diet.Recipes.v2.GroupsFragment groupsFragment;
    private Context context = getContext();
    private ViewGroup viewGroup;
    private RecyclerView recyclerView;
    private GroupsAdapterNew adapter;
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
    public void onResume() {
        super.onResume();

        window.getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        window.setStatusBarColor(Color.parseColor("#32000000"));
    }

    @Override
    public void onPause() {
        super.onPause();
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        window = getActivity().getWindow();
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
        if (Config.RELEASE) {
            updateUI();
            cardView.setVisibility(View.GONE);
        } else {
            updateUINew();
        }

        Amplitude.getInstance().logEvent(AmplitudaEvents.view_all_recipes);
        return view;
    }

    private void updateUI() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(Config.NAME_OF_ENTITY_FOR_DB).
                child("listOfGroupsRecipes");

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ObjectHolder objectHolder = new ObjectHolder();
                objectHolder.bindObjectWithHolder(dataSnapshot.getValue(ListOfGroupsRecipes.class));

                recyclerView.setAdapter(new GroupsAdapter((ArrayList<ListOfRecipes>) ObjectHolder.
                        getListOfGroupsRecipes().getListOfGroupsRecipes(), groupsFragment));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {


            }
        });
    }

    private void updateUINew() {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("RECIPES_PLANS_NEW");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                ListRecipes groupsRecipes = dataSnapshot.getValue(ListRecipes.class);

                EatingGroupsRecipes eatingGroupsRecipes = new EatingGroupsRecipes(groupsRecipes);
                GroupsHolder groupsHolder = new GroupsHolder();
                groupsHolder.bind(eatingGroupsRecipes);

                adapter = new GroupsAdapterNew(GroupsHolder.getGroupsRecipes().getGroups(), groupsFragment, viewGroup.getId());
                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {


            }
        });

    }


    public void searchAndShow(CharSequence s) {
        String key = s.toString().toLowerCase();
        List<RecipeItem> result = new ArrayList<>();
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
            ListRecipesAdapterNew adapterNew = new ListRecipesAdapterNew(result, getActivity());
            recyclerView.setLayoutManager(gridLayout);
            recyclerView.setAdapter(adapterNew);

        }

    }

}

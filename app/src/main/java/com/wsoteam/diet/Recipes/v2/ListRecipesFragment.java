package com.wsoteam.diet.Recipes.v2;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.amplitude.api.Amplitude;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wsoteam.diet.AmplitudaEvents;
import com.wsoteam.diet.Config;
import com.wsoteam.diet.MainScreen.MainActivity;
import com.wsoteam.diet.ObjectHolder;
import com.wsoteam.diet.POJOS.ItemRecipes;
import com.wsoteam.diet.POJOS.ListOfGroupsRecipes;
import com.wsoteam.diet.R;
import com.wsoteam.diet.Recipes.ListRecipesAdapter;
import com.wsoteam.diet.Recipes.ListRecipesAdapterNew;
import com.wsoteam.diet.Recipes.POJO.GroupsHolder;
import com.wsoteam.diet.Recipes.POJO.GroupsRecipes;
import com.wsoteam.diet.Recipes.POJO.RecipeItem;

import java.util.ArrayList;
import java.util.List;

public class ListRecipesFragment extends Fragment {


    private RecyclerView recyclerView;
    private int position;
    private RecyclerView.LayoutManager layoutManager;
    private ListRecipesAdapterNew adapter;
    private CardView cardView;
    private Toolbar mToolbar;
    private Window window;


    @Override
    public void onResume() {
        super.onResume();
        window.getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        window.setStatusBarColor(Color.parseColor("#32000000"));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Bundle bundle = getArguments();
        if (bundle != null) {
            position = bundle.getInt(Config.RECIPES_BUNDLE);
            Log.d("testrt", "onCreateView: position " + position);
        }

        View view = inflater.inflate(R.layout.fragment_list_recipes_v2, container, false);
        cardView = view.findViewById(R.id.cvRecipeItem);

        window = getActivity().getWindow();
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

        if (Config.RELEASE){
            updateUI();
            cardView.setVisibility(View.GONE);
        }else {
            updateUINew();
        }


        Amplitude.getInstance().logEvent(AmplitudaEvents.view_group_recipes);
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
                recyclerView.setAdapter(new ListRecipesAdapter((List<ItemRecipes>) ObjectHolder.getListOfGroupsRecipes().getListOfGroupsRecipes().get(position).getListRecipes(), getActivity()));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void updateUINew() {
        mToolbar.setTitle(GroupsHolder.getGroupsRecipes().getGroups().get(position).getName());
        adapter = new ListRecipesAdapterNew(GroupsHolder.getGroupsRecipes().getGroups().get(position).getListrecipes(), getActivity());
        recyclerView.setAdapter(adapter);
    }

    public void searchAndShow(CharSequence s){
        String key = s.toString().toLowerCase();
        List<RecipeItem> result = new ArrayList<>();
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
            ListRecipesAdapterNew adapterNew = new ListRecipesAdapterNew(result, getActivity());
            recyclerView.setAdapter(adapterNew);
        }

    }
}

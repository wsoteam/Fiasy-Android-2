package com.wsoteam.diet.Recipes;

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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

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
import com.wsoteam.diet.Recipes.POJO.EatingGroupsRecipes;
import com.wsoteam.diet.Recipes.POJO.GroupsHolder;
import com.wsoteam.diet.Recipes.POJO.GroupsRecipes;
import com.wsoteam.diet.Recipes.POJO.ListRecipes;
import com.wsoteam.diet.Recipes.POJO.RecipeItem;

import java.util.ArrayList;
import java.util.List;

public class GroupsFragment extends Fragment {

    private String TAG = "GroupsFragment";
    private GroupsFragment groupsFragment;
    private Context context = getContext();
    private Button backButton;
    private ViewGroup viewGroup;
    private RecyclerView recyclerView;
    private GroupsAdapterNew adapter;
    private CardView cardView;
    private RecyclerView.LayoutManager layoutManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Window window = getActivity().getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.parseColor("#9C5E21"));

        groupsFragment = this;
        viewGroup = container;

        View view = inflater.inflate(R.layout.fragment_groups_recipes, container, false);
        layoutManager = new LinearLayoutManager(getContext());
        backButton = view.findViewById(R.id.btnback5);
        cardView = view.findViewById(R.id.cvGroupsRecipes);
        EditText etSearch = view.findViewById(R.id.etGroupsRecipes);

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchAndShow(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
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

                recyclerView.setAdapter(new GroupsAdapter((ArrayList<ListOfRecipes>) ObjectHolder.getListOfGroupsRecipes().getListOfGroupsRecipes(), groupsFragment));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {


            }
        });
    }

    private void updateUINew() {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("TEST_FOR_PLANS");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                ListRecipes groupsRecipes = dataSnapshot.getValue(ListRecipes.class);

                int i = 0;
                for (RecipeItem recipe :
                        groupsRecipes.getListrecipes()) {
                    Log.d(TAG, "onDataChange: " + recipe.getName());
                    i++;
                    if (i == 5) break;
                }

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

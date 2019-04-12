package com.wsoteam.diet.Recipes;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.internal.bind.ReflectiveTypeAdapterFactory;
import com.wsoteam.diet.BranchOfRecipes.ActivityGroupsOfRecipes;
import com.wsoteam.diet.Config;
import com.wsoteam.diet.ObjectHolder;
import com.wsoteam.diet.POJOS.ItemRecipes;
import com.wsoteam.diet.POJOS.ListOfGroupsRecipes;
import com.wsoteam.diet.POJOS.ListOfRecipes;
import com.wsoteam.diet.R;

import java.util.ArrayList;
import java.util.List;

import static android.support.v7.widget.GridLayoutManager.*;

public class GroupsRecipesFragment extends Fragment {

    GroupsRecipesFragment groupsRecipesFragment = this;
    Context context = getContext();

    Button backButton;

    RecyclerView groups;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_groups_recipes, container, false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        backButton = view.findViewById(R.id.btnback5);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                groups.setLayoutManager(layoutManager);
                updateUI();
            }
        });


        groups = view.findViewById(R.id.rvGroupsRecipe);

        groups.setLayoutManager(layoutManager);
        updateUI();
        return view;
    }

    public void updateAdapter(List<ItemRecipes> list) {
        groups.setLayoutManager(new GridLayoutManager(getContext(), 2));
        groups.setAdapter(new RecipeAdapter(list));
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
                groups.setAdapter(new GroupsRecipesAdapter((ArrayList<ListOfRecipes>) ObjectHolder.getListOfGroupsRecipes().getListOfGroupsRecipes(), groupsRecipesFragment));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}

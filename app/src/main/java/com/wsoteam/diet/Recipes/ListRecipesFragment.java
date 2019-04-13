package com.wsoteam.diet.Recipes;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wsoteam.diet.Config;
import com.wsoteam.diet.ObjectHolder;
import com.wsoteam.diet.POJOS.ItemRecipes;
import com.wsoteam.diet.POJOS.ListOfGroupsRecipes;
import com.wsoteam.diet.POJOS.ListOfRecipes;
import com.wsoteam.diet.R;

import java.util.ArrayList;
import java.util.List;

public class ListRecipesFragment extends Fragment {


    private RecyclerView recyclerView;
    private TextView textView;
    private Button backButton;
    private int position;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Bundle bundle = getArguments();
        if (bundle != null) {

            position = bundle.getInt(Config.RECIPES_BUNDLE);
            Log.d("testrt", "onCreateView: position " + position);
        }

        View view = inflater.inflate(R.layout.fragment_list_recipes, container, false);
        textView = view.findViewById(R.id.tvListRecipes);
        textView.setText(ObjectHolder.getListOfGroupsRecipes().getListOfGroupsRecipes().get(position).getName());

        backButton = view.findViewById(R.id.btnback5);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        recyclerView = view.findViewById(R.id.rvListRecipes);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        updateUI();

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
                recyclerView.setAdapter(new ListRecipesAdapter((List<ItemRecipes>) ObjectHolder.getListOfGroupsRecipes().getListOfGroupsRecipes().get(position).getListRecipes()));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}

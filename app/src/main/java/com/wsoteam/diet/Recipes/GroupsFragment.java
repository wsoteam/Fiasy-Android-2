package com.wsoteam.diet.Recipes;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.amplitude.api.Amplitude;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wsoteam.diet.AmplitudaEvents;
import com.wsoteam.diet.Config;
import com.wsoteam.diet.ObjectHolder;
import com.wsoteam.diet.POJOS.ItemRecipes;
import com.wsoteam.diet.POJOS.ListOfGroupsRecipes;
import com.wsoteam.diet.POJOS.ListOfRecipes;
import com.wsoteam.diet.R;
import com.wsoteam.diet.Recipes.POJO.EatingGroupsRecipes;
import com.wsoteam.diet.Recipes.POJO.Factory;
import com.wsoteam.diet.Recipes.POJO.GroupsHolder;
import com.wsoteam.diet.Recipes.POJO.ListRecipes;
import com.wsoteam.diet.Recipes.POJO.RecipeItem;

import java.util.ArrayList;
import java.util.List;

public class GroupsFragment extends Fragment {

    String TAG = "GroupsFragment";

    GroupsFragment groupsFragment;
    Context context = getContext();

    Button backButton;

    ViewGroup viewGroup;

    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Window window = getActivity().getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.parseColor("#9C5E21"));

        groupsFragment =this;
        viewGroup = container;

        View view = inflater.inflate(R.layout.fragment_groups_recipes, container, false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        backButton = view.findViewById(R.id.btnback5);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });


        recyclerView = view.findViewById(R.id.rvGroupsRecipe);

        recyclerView.setLayoutManager(layoutManager);
//        List<ListRecipes> listRecipes = new EatingGroupsRecipes(Factory.getlistRecipes()).getGroups();
//        GroupsAdapterNew groupsAdapterNew = new GroupsAdapterNew(listRecipes, groupsFragment);
//        recyclerView.setAdapter(groupsAdapterNew);
        updateUINew();
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

                ListRecipes listRecipes = Factory.getlistRecipes();
                EatingGroupsRecipes eatingGroupsRecipes = new EatingGroupsRecipes(listRecipes);
                Log.d(TAG, "updateUINew: " + eatingGroupsRecipes);

                GroupsAdapterNew groupsAdapterNew = new GroupsAdapterNew(eatingGroupsRecipes.getGroups(), groupsFragment, viewGroup.getId());
                recyclerView.setAdapter(new GroupsAdapter((ArrayList<ListOfRecipes>) ObjectHolder.getListOfGroupsRecipes().getListOfGroupsRecipes(), groupsFragment));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {



            }
        });
    }

    private void updateUINew() {

        ListRecipes listRecipes = Factory.getlistRecipes();
        EatingGroupsRecipes eatingGroupsRecipes = new EatingGroupsRecipes(listRecipes);
        GroupsHolder groupsHolder = new GroupsHolder();
        groupsHolder.bind(eatingGroupsRecipes);

        Log.d(TAG, "updateUINew: " + eatingGroupsRecipes);

        GroupsAdapterNew groupsAdapterNew = new GroupsAdapterNew(GroupsHolder.getGroupsRecipes().getGroups(), groupsFragment, viewGroup.getId());
        recyclerView.setAdapter(groupsAdapterNew);

        Log.d(TAG, "updateUINew: idd" + R.id.flFragmentContainer);

//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference myRef = database.getReference("TEST_FOR_PLANS");
//
//        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                ListRecipes listRecipes = dataSnapshot.getValue(ListRecipes.class);
////                RecipeItem recipeItem = dataSnapshot.getValue(RecipeItem.class);
//                Log.d(TAG, "onDataChange: ");
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Log.d(TAG, "onCancelled: ERRRRRRRRRR");
//            }
//        });
    }

}

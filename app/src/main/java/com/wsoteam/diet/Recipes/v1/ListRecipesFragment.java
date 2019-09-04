package com.wsoteam.diet.Recipes.v1;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.wsoteam.diet.ObjectHolder;
import com.wsoteam.diet.POJOS.ItemRecipes;
import com.wsoteam.diet.POJOS.ListOfGroupsRecipes;
import com.wsoteam.diet.R;
import com.wsoteam.diet.Recipes.POJO.GroupsHolder;
import com.wsoteam.diet.Recipes.POJO.GroupsRecipes;
import com.wsoteam.diet.Recipes.POJO.RecipeItem;

import java.util.ArrayList;
import java.util.List;

public class ListRecipesFragment extends Fragment {


    private RecyclerView recyclerView;
    private TextView textView;
    private Button backButton;
    private EditText etSearch;
    private int position;
    private RecyclerView.LayoutManager layoutManager;
    private ListRecipesAdapterNew adapter;
    private CardView cardView;

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
        backButton = view.findViewById(R.id.btnback5);
        etSearch = view.findViewById(R.id.etRecipeItem);
        cardView = view.findViewById(R.id.cvRecipeItem);

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

        recyclerView = view.findViewById(R.id.rvListRecipes);
        layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);

        if (Config.RELEASE){
            updateUI();
            cardView.setVisibility(View.GONE);
            textView.setText(ObjectHolder.getListOfGroupsRecipes().getListOfGroupsRecipes().get(position).getName());
        }else {
            updateUINew();
        }

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
        textView.setText(GroupsHolder.getGroupsRecipes().getGroups().get(position).getName());
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

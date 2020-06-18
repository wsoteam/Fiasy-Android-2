package com.losing.weight.Recipes.POJO;

import android.content.Context;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.core.util.Consumer;


public class GroupsHolder {

    private static GroupsRecipes groupsRecipes;
    private static Set<Observer> observersList = new LinkedHashSet<>();

    public GroupsHolder() {

    }

    public static void subscribe(Observer observer) {
        observersList.add(observer);
    }

    public static void unsubscribe(Observer observer) {
        observersList.remove(observer);
    }

    public static GroupsRecipes getGroupsRecipes() {
        return groupsRecipes;
    }

    public static void loadRecipes(Context context, Consumer<ListRecipes> callback) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef;
        switch (Locale.getDefault().getLanguage().toUpperCase()){
            case "EN":
            case "ES":
            case "PT":
            case "DE":{
                myRef = database.getReference(Locale.getDefault().getLanguage().toUpperCase() + "/recipes");
                break;
            }
            case "RU":{
                myRef = database.getReference("RECIPES_PLANS_NEW");
                break;
            }
            default:{
                myRef = database.getReference("EN/recipes");
            }
        }

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final ListRecipes groupsRecipes = dataSnapshot.getValue(ListRecipes.class);
                RecipesHolder.bind(groupsRecipes);

                final EatingGroupsRecipes eatingGroupsRecipes = new EatingGroupsRecipes(groupsRecipes,
                        context.getApplicationContext());
                GroupsHolder groupsHolder = new GroupsHolder();
                groupsHolder.bind(eatingGroupsRecipes);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("kkk", "onCancelled: " , databaseError.toException() );
            }
        });
    }

    private void update() {

        for (Observer observer :
                observersList) {
            observer.update(new Observable(), null);
        }
    }

    public void bind(GroupsRecipes groupsRecipes) {
        this.groupsRecipes = groupsRecipes;
        update();

    }
}

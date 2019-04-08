package com.wsoteam.diet.BranchOfRecipes.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.adjust.sdk.Adjust;
import com.adjust.sdk.AdjustEvent;
import com.amplitude.api.Amplitude;
import com.bumptech.glide.Glide;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wsoteam.diet.AmplitudaEvents;
import com.wsoteam.diet.BranchOfRecipes.ActivityGroupsOfRecipes;
import com.wsoteam.diet.BranchOfRecipes.ActivityRecipes;
import com.wsoteam.diet.Config;
import com.wsoteam.diet.EventsAdjust;
import com.wsoteam.diet.ObjectHolder;
import com.wsoteam.diet.POJOS.ListOfGroupsRecipes;
import com.wsoteam.diet.POJOS.ListOfRecipes;
import com.wsoteam.diet.R;
import com.yandex.metrica.YandexMetrica;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class FragmentListRecipes extends Fragment {

    @BindView(R.id.rvRecipesList) RecyclerView rvRecipesList;
    Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_recipes, container, false);
        unbinder = ButterKnife.bind(this, view);

        updateUI();

        YandexMetrica.reportEvent("Открыт экран: Список рецептов");
        Adjust.trackEvent(new AdjustEvent(EventsAdjust.view_all_recipes));
        Amplitude.getInstance().logEvent(AmplitudaEvents.view_all_recipes);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
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
                rvRecipesList.setLayoutManager(new LinearLayoutManager(getActivity()));
                rvRecipesList.setAdapter(new RecipeGroupAdapter((ArrayList<ListOfRecipes>) ObjectHolder
                        .getListOfGroupsRecipes().getListOfGroupsRecipes()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private class RecipeGroupHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvTitleGropeRecipe, tvSubtitle;
        ImageView ivImgRecipe;

        public RecipeGroupHolder(LayoutInflater layoutInflater, ViewGroup viewGroup) {
            super(layoutInflater.inflate(R.layout.item_groups_of_recipe, viewGroup, false));
            itemView.setOnClickListener(this);
            tvTitleGropeRecipe = itemView.findViewById(R.id.tv_itemGroupsOfRecipe);
            tvSubtitle = itemView.findViewById(R.id.tv_itemOfGroupRecipeSubtitle);
            ivImgRecipe = itemView.findViewById(R.id.iv_itemGroupeRecipe);
        }

        public void bind(ListOfRecipes title) {
            String[] arrTitles = title.getName().split("-");
            tvTitleGropeRecipe.setText(arrTitles[0]);
            tvSubtitle.setText(arrTitles[1]);
            //Picasso.with(ActivityMonoDiet.this).load(title.getUrl_title()).into(ivItem);
            Glide.with(getActivity()).load(title.getUrlGroup()).into(ivImgRecipe);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getActivity(), ActivityRecipes.class);
            intent.putExtra(Config.ID_OF_RECIPE_GROUPS, getAdapterPosition());
            startActivity(intent);
        }
    }

    private class RecipeGroupAdapter extends RecyclerView.Adapter<RecipeGroupHolder> {
        ArrayList<ListOfRecipes> listOfRecipesGroup;


        public RecipeGroupAdapter(ArrayList<ListOfRecipes> titles) {
            listOfRecipesGroup = titles;
        }


        @Override
        public RecipeGroupHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new RecipeGroupHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(RecipeGroupHolder holder, int position) {
            holder.bind(listOfRecipesGroup.get(position));
        }

        @Override
        public int getItemCount() {
            return listOfRecipesGroup.size();
        }
    }
}

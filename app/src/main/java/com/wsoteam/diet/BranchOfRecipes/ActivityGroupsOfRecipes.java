package com.wsoteam.diet.BranchOfRecipes;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wsoteam.diet.Config;
import com.wsoteam.diet.ObjectHolder;
import com.wsoteam.diet.POJOS.ListOfGroupsRecipes;
import com.wsoteam.diet.POJOS.ListOfRecipes;
import com.wsoteam.diet.R;
import com.yandex.metrica.YandexMetrica;

import java.util.ArrayList;

public class ActivityGroupsOfRecipes extends AppCompatActivity {

    private static RecyclerView rvList;
    private static ArrayList<ListOfRecipes> listOfGroupsRecipes;
    InterstitialAd interstitialAd;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (interstitialAd.isLoaded()) {
            interstitialAd.show();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);

        rvList = findViewById(R.id.rvRecipesList);
        rvList.setLayoutManager(new LinearLayoutManager(this));

        updateUI();

        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(getResources().getString(R.string.admob_interstitial));
        interstitialAd.loadAd(new AdRequest.Builder().build());

        YandexMetrica.reportEvent("Открыт экран: Список рецептов");
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
                rvList.setAdapter(new RecipeGroupAdapter((ArrayList<ListOfRecipes>) ObjectHolder.getListOfGroupsRecipes().getListOfGroupsRecipes()));
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
            Glide.with(ActivityGroupsOfRecipes.this).load(title.getUrlGroup()).into(ivImgRecipe);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(ActivityGroupsOfRecipes.this, ActivityRecipes.class);
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
            LayoutInflater layoutInflater = LayoutInflater.from(ActivityGroupsOfRecipes.this);
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

package com.wsoteam.diet.BranchOfRecipes;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amplitude.api.Amplitude;
import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.wsoteam.diet.AmplitudaEvents;
import com.wsoteam.diet.Config;
import com.wsoteam.diet.ObjectHolder;
import com.wsoteam.diet.POJOS.ItemRecipes;
import com.wsoteam.diet.R;
import com.yandex.metrica.YandexMetrica;


import java.util.ArrayList;

public class ActivityRecipes extends AppCompatActivity {

    private static RecyclerView rvList;
    private static ArrayList<ItemRecipes> listOfRecipes;
    InterstitialAd interstitialAd;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(interstitialAd.isLoaded()){
            interstitialAd.show();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);

        listOfRecipes = (ArrayList<ItemRecipes>) ObjectHolder.
                getListOfGroupsRecipes().
                getListOfGroupsRecipes().get((int)getIntent().getSerializableExtra(Config.ID_OF_RECIPE_GROUPS)).getListRecipes();


        rvList = (RecyclerView) findViewById(R.id.rvRecipesList);
        rvList.setLayoutManager(new LinearLayoutManager(this));
        rvList.setAdapter(new RecipeAdapter(listOfRecipes));

        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(getResources().getString(R.string.admob_interstitial));
        interstitialAd.loadAd(new AdRequest.Builder().build());

        YandexMetrica.reportEvent("Открыт экран: Список рецептов на день");
        Amplitude.getInstance().logEvent(AmplitudaEvents.view_recipe);

    }


    private class RecipeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvItemRecipe;
        TextView tvItemBodyRecipe;
        ImageView ivItemRecipe;

        public RecipeHolder(LayoutInflater layoutInflater, ViewGroup viewGroup) {
            super(layoutInflater.inflate(R.layout.item_of_recipe_list, viewGroup, false));
            itemView.setOnClickListener(this);
            tvItemRecipe = itemView.findViewById(R.id.tv_itemRecipe);
            tvItemBodyRecipe = itemView.findViewById(R.id.tv_bodyRecipe);
            ivItemRecipe = itemView.findViewById(R.id.iv_itemRecipe);
        }

        public void bind(ItemRecipes title) {
            tvItemRecipe.setText(title.getName());
            tvItemBodyRecipe.setText(Html.fromHtml(title.getBody()));
            //Picasso.with(ActivityMonoDiet.this).load(title.getUrl_title()).into(ivItem);
            Glide.with(ActivityRecipes.this).load(title.getUrl()).into(ivItemRecipe);
        }

        @Override
        public void onClick(View v) {

        }
    }

    private class RecipeAdapter extends RecyclerView.Adapter<RecipeHolder> {
        ArrayList<ItemRecipes> listOfRecipes;


        public RecipeAdapter(ArrayList<ItemRecipes> titles) {
            /*this.*/listOfRecipes = titles; // нахрена ключ слово this? это не ошибка но оно требуется когда у двух переменных одинаковые имена
        }


        @Override
        public RecipeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(ActivityRecipes.this);
            return new RecipeHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder( RecipeHolder holder, int position) {
            holder.bind(listOfRecipes.get(position));
        }

        @Override
        public int getItemCount() {
            return listOfRecipes.size();
        }
    }


}

package com.wsoteam.diet.BranchOfMonoDiets;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.wsoteam.diet.OtherActivity.ActivitySettings;
import com.wsoteam.diet.Config;
import com.wsoteam.diet.ObjectHolder;
import com.wsoteam.diet.POJOS.POJO;
import com.wsoteam.diet.R;
import com.wsoteam.diet.TestFillDB;
import com.yandex.metrica.YandexMetrica;

import java.util.ArrayList;

public class ActivityMonoDiet extends AppCompatActivity {
    private static RecyclerView rvList;
    private static ArrayList<POJO> listOfPOJOS;
    private static AdView adView;
    InterstitialAd interstitialAd;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (interstitialAd.isLoaded()) {
            interstitialAd.show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mono_diet);
        rvList = findViewById(R.id.rvMonoDiets);
        adView = findViewById(R.id.bannerMainActivity);
        listOfPOJOS = (ArrayList<POJO>) ObjectHolder.getGlobalObject().getListOfPOJO().getListOFPOJO();

        //TestFillDB.fiilDB(ObjectHolder.getGlobalObject().getListOfPOJO());

        rvList.setLayoutManager(new LinearLayoutManager(this));
        rvList.setAdapter(new ItemAdapter(listOfPOJOS));

        //ADMob
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(getResources().getString(R.string.admob_interstitial));
        interstitialAd.loadAd(new AdRequest.Builder().build());

        YandexMetrica.reportEvent("Открыт экран: Список моно диет");


    }

    private class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvItem;
        ImageView ivItem;

        public ItemHolder(LayoutInflater layoutInflater, ViewGroup viewGroup) {
            super(layoutInflater.inflate(R.layout.item_of_main_list, viewGroup, false));
            itemView.setOnClickListener(this);
            tvItem = itemView.findViewById(R.id.tv_item);
            ivItem = itemView.findViewById(R.id.iv_item);
        }

        public void bind(POJO title) {
            tvItem.setText(title.getName());
            //Picasso.with(ActivityMonoDiet.this).load(title.getUrl_title()).into(ivItem);
            Glide.with(ActivityMonoDiet.this).load(title.getUrl_title()).into(ivItem);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(ActivityMonoDiet.this, ActivityViewerOfBodyItem.class);
            intent.putExtra(Config.ID_OF_ITEM, listOfPOJOS.get(getAdapterPosition()));
            startActivity(intent);
        }
    }

    private class ItemAdapter extends RecyclerView.Adapter<ItemHolder> {
        ArrayList<POJO> listOfPOJOS;


        public ItemAdapter(ArrayList<POJO> titles) {
            this.listOfPOJOS = titles;
        }

        @NonNull
        @Override
        public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(ActivityMonoDiet.this);
            return new ItemHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
            holder.bind(listOfPOJOS.get(position));
        }

        @Override
        public int getItemCount() {
            return listOfPOJOS.size();
        }
    }


    public static boolean hasConnection(final Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiInfo != null && wifiInfo.isConnected()) {
            return true;
        }
        wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (wifiInfo != null && wifiInfo.isConnected()) {
            return true;
        }
        wifiInfo = cm.getActiveNetworkInfo();
        if (wifiInfo != null && wifiInfo.isConnected()) {
            return true;
        }
        return false;
    }


}

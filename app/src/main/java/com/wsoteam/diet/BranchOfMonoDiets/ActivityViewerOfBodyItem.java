package com.wsoteam.diet.BranchOfMonoDiets;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.wsoteam.diet.Config;
import com.wsoteam.diet.POJOS.POJO;
import com.wsoteam.diet.R;
import com.yandex.metrica.YandexMetrica;

public class ActivityViewerOfBodyItem extends AppCompatActivity {
    TextView tvTitle, tvBody;
    ImageView imageView;
    AdView adView;
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
        setContentView(R.layout.activity_viewer_of_body_item);
        getSupportActionBar().hide();

        tvTitle = findViewById(R.id.tvTitle);
        tvBody = findViewById(R.id.tvBody);
        imageView = findViewById(R.id.ivHeadOfViewer);
        adView = findViewById(R.id.bannerActivityViewerOfBodyItem);


        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(getResources().getString(R.string.admob_interstitial));
        interstitialAd.loadAd(new AdRequest.Builder().build());

        YandexMetrica.reportEvent("Открыт экран: Детализация моно диеты");





        POJO itemOb = (POJO) getIntent().getSerializableExtra(Config.ID_OF_ITEM);
        tvTitle.setText(Html.fromHtml(itemOb.getName()));
        tvBody.setText(Html.fromHtml(itemOb.getBodyOfText()));
        Glide.with(this).load(itemOb.getUrl_title()).into(imageView);
    }
}

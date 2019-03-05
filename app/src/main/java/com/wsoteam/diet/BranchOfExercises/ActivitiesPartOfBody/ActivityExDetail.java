package com.wsoteam.diet.BranchOfExercises.ActivitiesPartOfBody;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.wsoteam.diet.POJOSExercises.Ex;
import com.wsoteam.diet.R;


public class ActivityExDetail extends AppCompatActivity {

    private AdView banner;
    private InterstitialAd mInterstitialAd;
    private Ex ex;
    public static final String TAG = "ActivityExDetail";
    private TextView title, basic_muscle, additional_muscle, complexity, for_man, for_woman, detail, main_chips;
    private ImageView imageView, imageViewBackHead;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ex_activity_ex_detail);
        banner = findViewById(R.id.ex_bannerMainActivity);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(this.getResources().getString(R.string.admob_interstitial));
        AdRequest adRequest = new AdRequest.Builder().build();
        mInterstitialAd.loadAd(adRequest);
        banner.loadAd(adRequest);

        ex = (Ex) getIntent().getSerializableExtra(TAG);

        imageViewBackHead = findViewById(R.id.EX_ivDetailBack);
        imageView = findViewById(R.id.ex_ivDetailImage);
        title = findViewById(R.id.ex_tvTitleExDetail);
        basic_muscle = findViewById(R.id.ex_tvDetailBasicMuscle);
        additional_muscle = findViewById(R.id.ex_tvDetailAdditionalMuscle);
        complexity = findViewById(R.id.ex_tvDetailComplexity);
        for_man = findViewById(R.id.ex_tvDetailCountMan);
        for_woman = findViewById(R.id.ex_tvDetailCountWoman);
        detail = findViewById(R.id.ex_tvDetailText);
        main_chips = findViewById(R.id.ex_tvDetailChips);

        Glide.with(this).load(ex.getImg_url()).into(imageView);
        Glide.with(this).load(ex.getUrl_of_logo()).into(imageViewBackHead);

        title.setText(ex.getTitle());
        basic_muscle.setText(ex.getBasic_muscle());
        additional_muscle.setText(ex.getAdditional_muscle());
        complexity.setText(ex.getComplexity());
        for_man.setText(ex.getFor_man());
        for_woman.setText(ex.getFor_woman());
        detail.setText(Html.fromHtml(ex.getDetail()));
        main_chips.setText(Html.fromHtml(ex.getMain_chips()));

        title.setTypeface(Typeface.createFromAsset(ActivityExDetail.this.getAssets()
                , "asProgramMainScreen.ttf"));
    }

    @Override
    public void onBackPressed() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
        super.onBackPressed();
    }
}

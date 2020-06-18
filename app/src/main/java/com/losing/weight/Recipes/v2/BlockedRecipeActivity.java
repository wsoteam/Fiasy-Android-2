package com.losing.weight.Recipes.v2;

import android.content.Intent;
import android.graphics.Color;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.losing.weight.AmplitudaEvents;
import com.losing.weight.Authenticate.POJO.Box;
import com.losing.weight.Config;
import com.losing.weight.InApp.ActivitySubscription;
import com.losing.weight.R;
import com.losing.weight.ads.FiasyAds;
import com.losing.weight.ads.nativetemplates.NativeTemplateStyle;
import com.losing.weight.ads.nativetemplates.TemplateView;
import com.losing.weight.common.Analytics.EventProperties;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class BlockedRecipeActivity extends AppCompatActivity {

    private Window window;

    @BindView(R.id.my_template) TemplateView nativeAd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blocked_recipe_2);
        ButterKnife.bind(this);
        window = getWindow();
        window.getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        window.setStatusBarColor(Color.parseColor("#66000000"));

        findViewById(R.id.imageView6).setOnClickListener(view -> onBackPressed());

        FiasyAds.getLiveDataAdView().observe(this, ad -> {
            if (ad != null) {
                nativeAd.setVisibility(View.VISIBLE);
                nativeAd.setStyles(new NativeTemplateStyle.Builder().build());
                nativeAd.setNativeAd(ad);

            } else{
                nativeAd.setVisibility(View.GONE);
            }
        });
    }

    @OnClick(R.id.goPrem)
    public void onViewClicked(View view) {
        Box box = new Box();
        box.setSubscribe(false);
        box.setOpenFromPremPart(true);
        box.setOpenFromIntrodaction(false);
        box.setComeFrom(AmplitudaEvents.view_prem_recipe);
        box.setBuyFrom(EventProperties.trial_from_recipe);
        Intent intent = new Intent(this, ActivitySubscription.class).putExtra(Config.TAG_BOX, box);
        startActivity(intent);
    }

}

package com.wsoteam.diet.OtherActivity;

import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.wsoteam.diet.R;
import com.yandex.metrica.YandexMetrica;

public class  ActivityEmpty extends AppCompatActivity {
    private Animation movingFromRight;
    private FloatingActionButton floatingActionButton;
    private RewardedVideoAd mRewardedVideoAd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty);
        floatingActionButton = findViewById(R.id.fabADEmptyActivity);
        movingFromRight = AnimationUtils.loadAnimation(this, R.anim.moving_from_bottom);
        floatingActionButton.setVisibility(View.GONE);
        loadAd();
        mRewardedVideoAd.setRewardedVideoAdListener(new RewardedVideoAdListener() {
            @Override
            public void onRewardedVideoAdLoaded() {
                floatingActionButton.startAnimation(movingFromRight);
                floatingActionButton.setVisibility(View.VISIBLE);
            }

            @Override
            public void onRewardedVideoAdOpened() {

            }

            @Override
            public void onRewardedVideoStarted() {

            }

            @Override
            public void onRewardedVideoAdClosed() {

            }

            @Override
            public void onRewarded(RewardItem rewardItem) {

            }

            @Override
            public void onRewardedVideoAdLeftApplication() {

            }

            @Override
            public void onRewardedVideoAdFailedToLoad(int i) {

            }

            @Override
            public void onRewardedVideoCompleted() {

            }
        });
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mRewardedVideoAd.isLoaded()) {
                    mRewardedVideoAd.show();
                    loadAd();
                    YandexMetrica.reportEvent("Открыта видео-реклама: Письмо разработчиков");
                }else {
                    Toast.makeText(ActivityEmpty.this, "Ролик прогружается, нужно немного подождать)", Toast.LENGTH_SHORT).show();

                }
            }
        });
        YandexMetrica.reportEvent("Открыт экран: Письмо разработчиков");
    }

    private void loadAd() {
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        mRewardedVideoAd.loadAd(getResources().getString(R.string.admob_award),
                new AdRequest.Builder().build());
    }
}

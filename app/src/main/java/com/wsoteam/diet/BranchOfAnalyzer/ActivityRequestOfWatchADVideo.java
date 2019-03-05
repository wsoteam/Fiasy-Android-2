package com.wsoteam.diet.BranchOfAnalyzer;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.wsoteam.diet.POJOFoodItem.FoodItem;
import com.wsoteam.diet.POJOFoodItem.LockItemOfFoodBase;
import com.wsoteam.diet.R;
import com.yandex.metrica.YandexMetrica;

import java.util.List;

public class ActivityRequestOfWatchADVideo extends AppCompatActivity {
    private TextView tvTitleOfRequestAd, tvPropertiesOfGroupRequestAdWatch, tvCountInGroup, tvToastCompleteGift;
    private FoodItem foodItem;
    private FloatingActionButton fabMainImageAdRequest, fabCloseRequestAd;
    private CardView cvWatchAd;
    private ConstraintLayout llContainerWithImageAndTextAdButton;
    private ImageView ivToastCompleteGift;
    private View layoutForToast;

    private Animation animationMovingFromBottom, animationChangeAlpha;
    private RewardedVideoAd mRewardedVideoAd;

    private int idOfToastIcon;
    private boolean isWatchedAD = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_of_watch_advideo);

        loadAd();
        setAnimation();

        foodItem = (FoodItem) getIntent().getSerializableExtra("ActivityRequestOfWatchADVideo");

        idOfToastIcon = choiseIcon(foodItem.getNameOfGroup());

        tvTitleOfRequestAd = findViewById(R.id.tvTitleOfRequestAd);
        tvPropertiesOfGroupRequestAdWatch = findViewById(R.id.tvPropertiesOfGroupRequestAdWatch);
        tvCountInGroup = findViewById(R.id.tvCountInGroup);
        fabCloseRequestAd = findViewById(R.id.fabCloseRequestAd);
        cvWatchAd = findViewById(R.id.cvRequestAdWatchButton);
        llContainerWithImageAndTextAdButton = findViewById(R.id.llContainerWithImageAndTextAdButton);

        cvWatchAd.setVisibility(View.GONE);
        llContainerWithImageAndTextAdButton.setVisibility(View.GONE);

        tvTitleOfRequestAd.setText(foodItem.getNameOfGroup());
        tvCountInGroup.setText("Продуктов - " + String.valueOf(foodItem.getCountOfItemsInGroup()) + " шт.");
        tvPropertiesOfGroupRequestAdWatch.setText("Вы можете открыть доступ к этой группе посмотрев всего лишь один короткий рекламный ролик. Открыть его можно кнопкой снизу.");


        mRewardedVideoAd.setRewardedVideoAdListener(new RewardedVideoAdListener() {
            @Override
            public void onRewardedVideoAdLoaded() {
                cvWatchAd.startAnimation(animationMovingFromBottom);
                cvWatchAd.setVisibility(View.VISIBLE);
                llContainerWithImageAndTextAdButton.startAnimation(animationChangeAlpha);
                llContainerWithImageAndTextAdButton.setVisibility(View.VISIBLE);
            }

            @Override
            public void onRewardedVideoAdOpened() {

            }

            @Override
            public void onRewardedVideoStarted() {

            }

            @Override
            public void onRewardedVideoAdClosed() {
                cvWatchAd.setVisibility(View.GONE);
                if (isWatchedAD) {
                    unlockGroup();
                    Intent intent = new Intent();
                    intent.putExtra("nameOfGroup", foodItem.getNameOfGroup());
                    intent.putExtra("idOfToastIcon", idOfToastIcon);
                    setResult(RESULT_OK, intent);
                    YandexMetrica.reportEvent("Открыт экран: Реклама анлока досмотрена");
                    finish();
                } else {
                    Toast.makeText(ActivityRequestOfWatchADVideo.this, "Чтобы разблокировать группу нужно досмотреть ролик до конца", Toast.LENGTH_SHORT).show();
                    loadAd();
                }
            }

            @Override
            public void onRewarded(RewardItem rewardItem) {
                isWatchedAD = true;
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

        fabCloseRequestAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        cvWatchAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mRewardedVideoAd.isLoaded()) {
                    mRewardedVideoAd.show();
                } else {
                    Toast.makeText(ActivityRequestOfWatchADVideo.this, "Ролик прогружается, нужно немного подождать", Toast.LENGTH_SHORT).show();
                }
            }
        });

        YandexMetrica.reportEvent("Открыт экран: Анлок группы");
    }

    private void unlockGroup() {
        List<LockItemOfFoodBase> lockItems = LockItemOfFoodBase.listAll(LockItemOfFoodBase.class);
        LockItemOfFoodBase.deleteAll(LockItemOfFoodBase.class);
        for (int i = 0; i < lockItems.size(); i++) {
            if (!lockItems.get(i).getNameOfUnLockGroup().equals(foodItem.getNameOfGroup())){
                lockItems.get(i).save();
            }
        }
    }


    private void setAnimation() {
        animationMovingFromBottom = AnimationUtils.loadAnimation(this, R.anim.moving_from_bottom_and_change_scale);
        animationChangeAlpha = new AlphaAnimation(0, 1);
        animationChangeAlpha.setInterpolator(new DecelerateInterpolator());
        animationChangeAlpha.setStartOffset(1400);
        animationChangeAlpha.setDuration(400);
    }

    private int choiseIcon(String nameOfGroup) {
        String[] allNameOfLockGroups = getResources().getStringArray(R.array.lock_groups);
        int idOfDrawable = 0;

        if (nameOfGroup.equals(allNameOfLockGroups[0])) {
            idOfDrawable = R.drawable.ic_list_of_groups_mcdonalds;
        }
        if (nameOfGroup.equals(allNameOfLockGroups[1])) {
            idOfDrawable = R.drawable.ic_list_of_groups_sausage;
        }
        if (nameOfGroup.equals(allNameOfLockGroups[2])) {
            idOfDrawable = R.drawable.ic_list_of_groups_conditer;
        }
        if (nameOfGroup.equals(allNameOfLockGroups[3])) {
            idOfDrawable = R.drawable.ic_list_of_groups_porridge;
        }
        if (nameOfGroup.equals(allNameOfLockGroups[4])) {
            idOfDrawable = R.drawable.ic_list_of_groups_cereal;
        }
        if (nameOfGroup.equals(allNameOfLockGroups[5])) {
            idOfDrawable = R.drawable.ic_list_of_groups_alcohol;
        }
        if (nameOfGroup.equals(allNameOfLockGroups[6])) {
            idOfDrawable = R.drawable.ic_list_of_groups_fish;
        }
        if (nameOfGroup.equals(allNameOfLockGroups[7])) {
            idOfDrawable = R.drawable.ic_list_of_groups_cheese;
        }
        if (nameOfGroup.equals(allNameOfLockGroups[8])) {
            idOfDrawable = R.drawable.ic_list_of_groups_cake;
        }
        if (nameOfGroup.equals(allNameOfLockGroups[9])) {
            idOfDrawable = R.drawable.ic_list_of_groups_bread;
        }

        return idOfDrawable;

    }

    private void loadAd() {
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        mRewardedVideoAd.loadAd(getResources().getString(R.string.admob_award),
                new AdRequest.Builder().build());
    }
}

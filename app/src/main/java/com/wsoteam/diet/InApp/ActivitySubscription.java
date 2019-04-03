package com.wsoteam.diet.InApp;


import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;

import com.adjust.sdk.Adjust;
import com.adjust.sdk.AdjustEvent;
import com.amplitude.api.Amplitude;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.wsoteam.diet.Config;
import com.wsoteam.diet.EventsAdjust;
import com.wsoteam.diet.InApp.Fragments.PremiumSliderFragment;
import com.wsoteam.diet.OtherActivity.ActivitySplash;
import com.wsoteam.diet.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ActivitySubscription extends AppCompatActivity implements PurchasesUpdatedListener {
    @BindView(R.id.viewPager) ViewPager viewPager;
    @BindView(R.id.cvSub3mBack) CardView cvSub3mBack;
    @BindView(R.id.cvSub12mBack) CardView cvSub12mBack;
    @BindView(R.id.cvSub1mBack) CardView cvSub1mBack;
    private BillingClient billingClient;
    private static final String TAG = "inappbilling";
    private static final int COUNT_OF_PAGES = 4;
    private boolean isEnterFromMainActivity = false;
    private String sku = "basic_subscription_12m";

    private SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription);

        Amplitude.getInstance().logEvent(getIntent().getStringExtra(Config.AMPLITUDE_COME_FROM));
        Adjust.trackEvent(new AdjustEvent(getIntent().getStringExtra(Config.ADJUST_COME_FROM)));

        isEnterFromMainActivity = getIntent().getBooleanExtra(Config.ENTER_FROM_MAIN_ACTIVITY, false);
        ButterKnife.bind(this);
        billingClient = BillingClient.newBuilder(this).setListener((PurchasesUpdatedListener) this).build();
        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(int responseCode) {
                if (responseCode == BillingClient.BillingResponse.OK) {
                    Log.d(TAG, "onBillingSetupFinished: OK");
                    getSKU();
                }
            }

            @Override
            public void onBillingServiceDisconnected() {

            }
        });


        cvSub3mBack.setVisibility(View.GONE);
        cvSub1mBack.setVisibility(View.GONE);
        bindViewPager();
    }

    private void bindViewPager() {
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return PremiumSliderFragment.newInstance(position);
            }

            @Override
            public int getCount() {
                return COUNT_OF_PAGES;
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (isEnterFromMainActivity) {
            finish();
        } else {
            startActivity(new Intent(this, ActivitySplash.class));
            finish();
        }
        super.onBackPressed();
    }

    private void getSKU() {
        List<String> skuList = new ArrayList<>();
        skuList.add("basic_subscription_1m");
        skuList.add("basic_subscription_3m");
        skuList.add("basic_subscription_12m");

        SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
        params.setSkusList(skuList).setType(BillingClient.SkuType.SUBS);

        billingClient.querySkuDetailsAsync(params.build(), new SkuDetailsResponseListener() {
            @Override
            public void onSkuDetailsResponse(int responseCode, List<SkuDetails> skuDetailsList) {

                if (responseCode == BillingClient.BillingResponse.OK && skuDetailsList != null) {
                    Log.d(TAG, "onSkuDetailsResponse: OK");
//                    nameTextView.setText(skuDetailsList.get(0).getSku());
//                    priceTextView.setText(skuDetailsList.get(0).getPrice());


                } else {
                    Log.d(TAG, "onSkuDetailsResponse: FAIL");
                }

            }
        });
    }

    private void buy(String sku) {
        BillingFlowParams mParams = BillingFlowParams.newBuilder().
                setSku(sku).setType(BillingClient.SkuType.SUBS).build();

        billingClient.launchBillingFlow(this, mParams);
    }


    @Override
    public void onPurchasesUpdated(int responseCode, @Nullable List<Purchase> purchases) {
        if (responseCode == BillingClient.BillingResponse.OK && purchases != null){

            Amplitude.getInstance().logEvent(getIntent().getStringExtra(Config.AMPLITUDE_BUY_FROM));
            Adjust.trackEvent(new AdjustEvent(getIntent().getStringExtra(Config.ADJUST_BUY_FROM)));

            if (getIntent().getStringExtra(Config.ADJUST_COME_FROM) == Config.ADJUST_COME_FROM){
                Adjust.trackEvent(new AdjustEvent(EventsAdjust.buy_prem_onboarding));
            }

            sharedPreferences = getSharedPreferences(Config.ALERT_BUY_SUBSCRIPTION, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(Config.ALERT_BUY_SUBSCRIPTION, true);
            editor.commit();

            startActivity(new Intent(this, ActivitySplash.class));
            finish();
        }

    }

    @OnClick({R.id.cvSub1m, R.id.cvSub12m, R.id.imbtnCancel, R.id.cvSub3m, R.id.tvPrivacyPolicy, R.id.btnBuyPrem})
    public void onViewClicked(View view) {


        if (view.getId() == R.id.cvSub1m) {
            sku = "basic_subscription_1m";
            cvSub1mBack.setVisibility(View.VISIBLE);
            cvSub3mBack.setVisibility(View.GONE);
            cvSub12mBack.setVisibility(View.GONE);
        }
        if (view.getId() == R.id.cvSub12m) {
            sku = "basic_subscription_12m";
            cvSub12mBack.setVisibility(View.VISIBLE);
            cvSub3mBack.setVisibility(View.GONE);
            cvSub1mBack.setVisibility(View.GONE);
        }
        if (view.getId() == R.id.cvSub3m) {
            sku = "basic_subscription_3m";
            cvSub3mBack.setVisibility(View.VISIBLE);
            cvSub12mBack.setVisibility(View.GONE);
            cvSub1mBack.setVisibility(View.GONE);
        }
        if (view.getId() == R.id.btnBuyPrem) {
            buy(sku);
        }
        if (view.getId() == R.id.imbtnCancel) {
            if (isEnterFromMainActivity) {
                finish();
            } else {
                startActivity(new Intent(this, ActivitySplash.class));
                finish();
            }
        }
        if (view.getId() == R.id.tvPrivacyPolicy) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(getResources().getString(R.string.url_gdpr)));
            startActivity(intent);
        }

    }

}

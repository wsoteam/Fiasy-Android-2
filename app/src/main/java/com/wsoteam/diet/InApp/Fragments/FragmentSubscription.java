package com.wsoteam.diet.InApp.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

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
import com.wsoteam.diet.AmplitudaEvents;
import com.wsoteam.diet.Config;
import com.wsoteam.diet.EventsAdjust;
import com.wsoteam.diet.OtherActivity.ActivityPrivacyPolicy;
import com.wsoteam.diet.OtherActivity.ActivitySplash;
import com.wsoteam.diet.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.content.Context.MODE_PRIVATE;

public class FragmentSubscription extends Fragment implements PurchasesUpdatedListener {
    @BindView(R.id.viewPager) ViewPager viewPager;
    @BindView(R.id.cvSub3mBack) CardView cvSub3mBack;
    @BindView(R.id.cvSub12mBack) CardView cvSub12mBack;
    @BindView(R.id.cvSub1mBack) CardView cvSub1mBack;
    @BindView(R.id.imbtnCancel) ImageButton imbtnCancel;
    @BindView(R.id.ivFilter1sub) ImageView ivFilter1sub;
    @BindView(R.id.ivFilter12sub) ImageView ivFilter12sub;
    @BindView(R.id.ivFilter3sub) ImageView ivFilter3sub;
    @BindView(R.id.tlDotsIndicator) TabLayout tlDotsIndicator;
    private BillingClient billingClient;
    private static final String TAG = "inappbilling";
    private static final int COUNT_OF_PAGES = 4;
    private String sku = "basic_subscription_12m";

    private SharedPreferences sharedPreferences;

    Unbinder unbinder;
    private static final String AMPLITUDE_COME_FROM_TAG = "AMPLITUDE_COME_FROM_TAG",
            ADJUST_COME_FROM_TAG = "ADJUST_COME_FROM_TAG", ENTER_FROM_MAINACTIVITY_TAG = "ENTER_FROM_MAINACTIVITY_TAG",
            AMPLITUDE_BUY_FROM_TAG = "AMPLITUDE_BUY_FROM_TAG", ADJUST_BUY_FROM_TAG = "ADJUST_BUY_FROM_TAG";

    public static FragmentSubscription newInstance(boolean isEnterFromMainActivity, String amplitudeComeFrom,
                                                   String adjustComeFrom, String amplitudeBuyFrom, String adjustBuyFrom) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ENTER_FROM_MAINACTIVITY_TAG, isEnterFromMainActivity);
        bundle.putString(AMPLITUDE_COME_FROM_TAG, amplitudeComeFrom);
        bundle.putString(ADJUST_COME_FROM_TAG, adjustComeFrom);
        bundle.putString(AMPLITUDE_BUY_FROM_TAG, amplitudeBuyFrom);
        bundle.putString(ADJUST_BUY_FROM_TAG, adjustBuyFrom);

        FragmentSubscription fragmentSubscription = new FragmentSubscription();
        fragmentSubscription.setArguments(bundle);

        return fragmentSubscription;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_subscription, container, false);
        unbinder = ButterKnife.bind(this, view);

        String startFrom = getActivity().getIntent().getStringExtra(Config.START_FROM);
        Log.d("event", "String startFrom = " + startFrom);
        if (startFrom != null) {
            if (startFrom.equals(Config.FROM_ONBOARDING)) {
                Amplitude.getInstance().logEvent(AmplitudaEvents.buy_prem_onboarding);
                Adjust.trackEvent(new AdjustEvent(EventsAdjust.buy_prem_onboarding));
                Log.d("event", "onCreate: buy_prem_onboarding");
            }
        }


        Amplitude.getInstance().logEvent(getArguments().getString(AMPLITUDE_COME_FROM_TAG));
        Adjust.trackEvent(new AdjustEvent(getArguments().getString(ADJUST_COME_FROM_TAG)));

        if (getArguments().getBoolean(ENTER_FROM_MAINACTIVITY_TAG)) {
            imbtnCancel.setVisibility(View.GONE);
        }

        billingClient = BillingClient.newBuilder(getActivity()).setListener((PurchasesUpdatedListener) this).build();
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
        ivFilter1sub.setVisibility(View.GONE);
        ivFilter3sub.setVisibility(View.GONE);
        bindViewPager();
        tlDotsIndicator.setupWithViewPager(viewPager, true);
        return view;
    }

    private void bindViewPager() {
        viewPager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
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


                } else {
                    Log.d(TAG, "onSkuDetailsResponse: FAIL");
                }

            }
        });
    }

    private void buy(String sku) {
        BillingFlowParams mParams = BillingFlowParams.newBuilder().
                setSku(sku).setType(BillingClient.SkuType.SUBS).build();

        billingClient.launchBillingFlow(getActivity(), mParams);
    }

    @Override
    public void onPurchasesUpdated(int responseCode, @Nullable List<Purchase> purchases) {
        if (responseCode == BillingClient.BillingResponse.OK && purchases != null) {

            Amplitude.getInstance().logEvent(getArguments().getString(AMPLITUDE_BUY_FROM_TAG));
            Adjust.trackEvent(new AdjustEvent(getArguments().getString(ADJUST_BUY_FROM_TAG)));


            sharedPreferences = getActivity().getSharedPreferences(Config.ALERT_BUY_SUBSCRIPTION, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(Config.ALERT_BUY_SUBSCRIPTION, true);
            editor.commit();

            startActivity(new Intent(getActivity(), ActivitySplash.class));
            getActivity().finish();
        }

    }

    @OnClick({R.id.cvSub1m, R.id.cvSub12m, R.id.imbtnCancel, R.id.cvSub3m, R.id.tvPrivacyPolicy, R.id.btnBuyPrem})
    public void onViewClicked(View view) {


        if (view.getId() == R.id.cvSub1m) {
            sku = "basic_subscription_1m";
            cvSub1mBack.setVisibility(View.VISIBLE);
            ivFilter1sub.setVisibility(View.VISIBLE);
            cvSub3mBack.setVisibility(View.GONE);
            cvSub12mBack.setVisibility(View.GONE);
            ivFilter3sub.setVisibility(View.GONE);
            ivFilter12sub.setVisibility(View.GONE);
        }
        if (view.getId() == R.id.cvSub12m) {
            sku = "basic_subscription_12m";
            cvSub12mBack.setVisibility(View.VISIBLE);
            ivFilter12sub.setVisibility(View.VISIBLE);
            cvSub3mBack.setVisibility(View.GONE);
            cvSub1mBack.setVisibility(View.GONE);
            ivFilter3sub.setVisibility(View.GONE);
            ivFilter1sub.setVisibility(View.GONE);
        }
        if (view.getId() == R.id.cvSub3m) {
            sku = "basic_subscription_3m";
            cvSub3mBack.setVisibility(View.VISIBLE);
            ivFilter3sub.setVisibility(View.VISIBLE);
            cvSub12mBack.setVisibility(View.GONE);
            cvSub1mBack.setVisibility(View.GONE);
            ivFilter1sub.setVisibility(View.GONE);
            ivFilter12sub.setVisibility(View.GONE);
        }
        if (view.getId() == R.id.btnBuyPrem) {
            buy(sku);
        }
        if (view.getId() == R.id.imbtnCancel) {
            if (getActivity().getSharedPreferences(Config.FREE_USER, MODE_PRIVATE).getBoolean(Config.FREE_USER, true)) {
                getActivity().onBackPressed();
            } else {
                startActivity(new Intent(getActivity(), ActivitySplash.class));
                getActivity().finish();
            }

        }
        if (view.getId() == R.id.tvPrivacyPolicy) {
            Intent intent = new Intent(getActivity(), ActivityPrivacyPolicy.class);
            startActivity(intent);
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}

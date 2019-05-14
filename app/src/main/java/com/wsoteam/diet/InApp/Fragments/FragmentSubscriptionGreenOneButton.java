package com.wsoteam.diet.InApp.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.adjust.sdk.Adjust;
import com.adjust.sdk.AdjustEvent;
import com.amplitude.api.Amplitude;
import com.amplitude.api.Identify;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.facebook.appevents.AppEventsConstants;
import com.facebook.appevents.AppEventsLogger;
import com.wsoteam.diet.ABConfig;
import com.wsoteam.diet.AmplitudaEvents;
import com.wsoteam.diet.Authenticate.ActivityAuthMain;
import com.wsoteam.diet.Authenticate.POJO.Box;
import com.wsoteam.diet.Config;
import com.wsoteam.diet.EntryPoint.ActivitySplash;
import com.wsoteam.diet.EventsAdjust;
import com.wsoteam.diet.OtherActivity.ActivityPrivacyPolicy;
import com.wsoteam.diet.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.content.Context.MODE_PRIVATE;

public class FragmentSubscriptionGreenOneButton extends Fragment implements PurchasesUpdatedListener {
    @BindView(R.id.imbtnCancel) ImageButton imbtnCancel;
    private BillingClient billingClient;
    private static final String TAG = "inappbilling";
    private String currentSKU = Config.ONE_YEAR_PRICE_TRIAL, currentPrice = "99р";
    private SharedPreferences sharedPreferences;
    Unbinder unbinder;
    private static final String TAG_BOX = "TAG_BOX";
    private Box box;

    public static FragmentSubscriptionGreenOneButton newInstance(Box box) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(TAG_BOX, box);
        FragmentSubscriptionGreenOneButton fragmentSubscriptionGreenOneButton = new FragmentSubscriptionGreenOneButton();
        fragmentSubscriptionGreenOneButton.setArguments(bundle);
        return fragmentSubscriptionGreenOneButton;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_subscription_green_one_button, container, false);
        unbinder = ButterKnife.bind(this, view);
        box = (Box) getArguments().getSerializable(TAG_BOX);

        AmplitudaEvents.logEventViewPremium(box.getComeFrom(), ABConfig.green_P1M_one_button);

        if (!box.isOpenFromIntrodaction() && !box.isOpenFromPremPart()) {
            imbtnCancel.setVisibility(View.GONE);
        }

        billingClient = BillingClient.newBuilder(getActivity()).setListener((PurchasesUpdatedListener) this).build();
        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(int responseCode) {
                if (responseCode == BillingClient.BillingResponse.OK) {
                    getSKU();
                }
            }

            @Override
            public void onBillingServiceDisconnected() {

            }
        });
        return view;
    }

    private void getSKU() {
        List<String> skuList = new ArrayList<>();
        skuList.add(Config.ONE_YEAR_PRICE_TRIAL);

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
            Adjust.trackEvent(new AdjustEvent(EventsAdjust.buy_trial));
            Identify identify = new Identify();
            identify.set(AmplitudaEvents.PREM_STATUS, AmplitudaEvents.trial);
            identify.set(AmplitudaEvents.LONG_OF_PREM, currentSKU)
                    .set(AmplitudaEvents.PRICE_OF_PREM, currentPrice);
            Amplitude.getInstance().identify(identify);
            AmplitudaEvents.logEventBuyPremium(box.getBuyFrom(), ABConfig.green_P1M_one_button, currentSKU);
            logTrial();

            sharedPreferences = getActivity().getSharedPreferences(Config.ALERT_BUY_SUBSCRIPTION, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(Config.ALERT_BUY_SUBSCRIPTION, true);
            editor.commit();

            if (box.isOpenFromPremPart()) {
                startActivity(new Intent(getActivity(), ActivitySplash.class));
                getActivity().finish();
            } else if (box.isOpenFromIntrodaction()) {
                box.setSubscribe(true);
                startActivity(new Intent(getActivity(), ActivitySplash.class).
                        putExtra(Config.INTENT_PROFILE, box.getProfile()));
                getActivity().finish();
            }
        }

    }

    @OnClick({R.id.imbtnCancel, R.id.tvPrivacyPolicy, R.id.btnBuyPrem})
    public void onViewClicked(View view) {
        if (view.getId() == R.id.btnBuyPrem) {
            AmplitudaEvents.logEventClickBuy(currentSKU);
            buy(currentSKU);
        }
        if (view.getId() == R.id.imbtnCancel) {
            Amplitude.getInstance().logEvent(AmplitudaEvents.close_premium);
            if (box.isOpenFromIntrodaction()) {
                getActivity().getSharedPreferences(Config.IS_NEED_SHOW_GRADE_DIALOG, MODE_PRIVATE)
                        .edit().putBoolean(Config.IS_NEED_SHOW_GRADE_DIALOG, true)
                        .commit();
                startActivity(new Intent(getActivity(), ActivitySplash.class).
                        putExtra(Config.INTENT_PROFILE, box.getProfile()));
                getActivity().finish();
            }else {
                getActivity().onBackPressed();
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

    private void logTrial(){
        AppEventsLogger appEventsLogger = AppEventsLogger.newLogger(getActivity());
        Bundle params = new Bundle();
        params.putString(AppEventsConstants.EVENT_PARAM_CURRENCY, "RUB");
        appEventsLogger.logEvent(AppEventsConstants.EVENT_NAME_START_TRIAL, 990, params);
    }

}

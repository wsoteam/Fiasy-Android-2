package com.wsoteam.diet.InApp.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.adjust.sdk.Adjust;
import com.adjust.sdk.AdjustEvent;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.bumptech.glide.Glide;
import com.facebook.appevents.AppEventsConstants;
import com.facebook.appevents.AppEventsLogger;
import com.wsoteam.diet.Authenticate.POJO.Box;
import com.wsoteam.diet.BuildConfig;
import com.wsoteam.diet.Config;
import com.wsoteam.diet.EntryPoint.ActivitySplash;
import com.wsoteam.diet.EventsAdjust;
import com.wsoteam.diet.InApp.IDs;
import com.wsoteam.diet.InApp.properties.CheckAndSetPurchase;
import com.wsoteam.diet.InApp.properties.SingletonMakePurchase;
import com.wsoteam.diet.OtherActivity.ActivityPrivacyPolicy;
import com.wsoteam.diet.R;
import com.wsoteam.diet.common.Analytics.EventProperties;
import com.wsoteam.diet.common.Analytics.Events;
import com.wsoteam.diet.common.Analytics.SavedConst;
import com.wsoteam.diet.utils.IntentUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class FragmentC extends Fragment
        implements PurchasesUpdatedListener {

    @BindView(R.id.textView161)
    TextView textView;
    @BindView(R.id.btnBack)
    ImageView btnBack;
    @BindView(R.id.imgTitle)
    ImageView imgTitle;


    private BillingClient billingClient;
    private static final String TAG = "inappbilling";
    private String currentSKU = IDs.ID_ONE_YEAR_WITH_TRIAL, currentPrice = "99р";
    private SharedPreferences sharedPreferences;
    Unbinder unbinder;
    private static final String TAG_BOX = "TAG_BOX";
    private Box box;
    public static final String BUY_NOW = "BUY_NOW";

    public static FragmentC newInstance(Box box) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(TAG_BOX, box);
        FragmentC fragment =
                new FragmentC();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        //View view = inflater.inflate(R.layout.fragment_subscription_one_button_2, container, false);
        View view = inflater.inflate(R.layout.fragment_subscription_c, container, false);
        unbinder = ButterKnife.bind(this, view);
        Glide.with(getActivity()).load(R.drawable.subscription_one_button_title_img).into(imgTitle);

        box = (Box) getArguments().getSerializable(TAG_BOX);

        if (box.isOpenFromIntrodaction()) {
            btnBack.setVisibility(View.GONE);
            getActivity().getSharedPreferences(SavedConst.SEE_PREMIUM, Context.MODE_PRIVATE).edit().putBoolean(SavedConst.SEE_PREMIUM, true).commit();
        }

        billingClient = BillingClient.newBuilder(requireContext())
                .setListener(this)
                .build();
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
        skuList.add(IDs.ID_ONE_YEAR_WITH_TRIAL);

        SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
        params.setSkusList(skuList).setType(BillingClient.SkuType.SUBS);

        billingClient.querySkuDetailsAsync(params.build(), new SkuDetailsResponseListener() {
            @Override
            public void onSkuDetailsResponse(int responseCode, List<SkuDetails> skuDetailsList) {

                if (responseCode == BillingClient.BillingResponse.OK && skuDetailsList != null) {
                    Log.e("LOL", skuDetailsList.get(0).toString());
                    if (skuDetailsList.size() > 0) {
                        setPrice(skuDetailsList.get(0).getPrice());
                    }


                } else {
                    Log.d(TAG, "onSkuDetailsResponse: FAIL");
                }

            }
        });
    }

    private void setPrice(String price) {
        textView.setText(getActivity().getResources().getString(R.string.abt_bottom_prem, price));
    }

    private void buy(String sku) {
        BillingFlowParams mParams = BillingFlowParams.newBuilder()
                .setSku(sku)
                .setType(BillingClient.SkuType.SUBS)
                .build();

        billingClient.launchBillingFlow(getActivity(), mParams);
    }

    @Override
    public void onPurchasesUpdated(int responseCode, @Nullable List<Purchase> purchases) {
        if (responseCode != BillingClient.BillingResponse.OK) {
            Events.logBillingError(responseCode);
        }
        if (responseCode == BillingClient.BillingResponse.OK && purchases != null) {
            //send data about purchase into firebase (and prepareToSave into profile subInfo)
            SingletonMakePurchase.getInstance().setMakePurchaseNow(true);
            final Purchase p = purchases.get(0);

            if (BuildConfig.DEBUG) {
                Log.d("Fiasy", "Purchased, " + p.toString());
            }

            new CheckAndSetPurchase(getActivity()).execute(p.getSku(), p.getPurchaseToken(), p.getPackageName(), BUY_NOW);

            Adjust.trackEvent(new AdjustEvent(EventsAdjust.buy_trial));
            try {
                if (p.isAutoRenewing()) {
                    Events.logBuy(box.getBuyFrom(), EventProperties.auto_renewal_true);
                } else {
                    Events.logBuy(box.getBuyFrom(), EventProperties.auto_renewal_false);
                }
            } catch (Exception ex) {
                Events.logSetBuyError(ex.getMessage());
            }


            logTrial();

            requireContext().getSharedPreferences(Config.STATE_BILLING, Context.MODE_PRIVATE).
                    edit().
                    putBoolean(Config.STATE_BILLING, true).
                    apply();

            sharedPreferences = requireContext().getSharedPreferences(Config.ALERT_BUY_SUBSCRIPTION,
                    Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(Config.ALERT_BUY_SUBSCRIPTION, true);
            editor.apply();

            if (box.isOpenFromIntrodaction()) {
                box.setSubscribe(true);
                getActivity().getSharedPreferences(SavedConst.HOW_END, Context.MODE_PRIVATE).edit().putString(SavedConst.HOW_END, EventProperties.onboarding_success_trial).commit();
            }

            IntentUtils.openMainActivity(requireContext());
        }
    }


    @OnClick({R.id.btnBack, R.id.btnClose, R.id.btnBuyPrem, R.id.tvPrivacyPolicy})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnBuyPrem:
                Events.logPushButton(EventProperties.push_button_next, box.getBuyFrom());
                buy(currentSKU);
                break;
            case R.id.btnBack:
                Events.logPushButton(EventProperties.push_button_back, box.getBuyFrom());
                getActivity().onBackPressed();
                break;
            case R.id.btnClose: {
                Events.logPushButton(EventProperties.push_button_close, box.getBuyFrom());
                if (box.isOpenFromIntrodaction()) {
                    getActivity().getSharedPreferences(SavedConst.HOW_END, Context.MODE_PRIVATE).edit().putString(SavedConst.HOW_END, EventProperties.onboarding_success_close).commit();
                    final Intent intent = new Intent(getContext(), ActivitySplash.class);
                    startActivity(intent);
                    getActivity().finish();
                } else {
                    getActivity().onBackPressed();
                }
            }
            break;
            case R.id.tvPrivacyPolicy:
                Events.logPushButton(EventProperties.push_button_privacy, box.getBuyFrom());
                Intent intent = new Intent(getActivity(), ActivityPrivacyPolicy.class);
                startActivity(intent);
                break;

        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void logTrial() {
        AppEventsLogger appEventsLogger = AppEventsLogger.newLogger(getActivity());
        Bundle params = new Bundle();
        params.putString(AppEventsConstants.EVENT_PARAM_CURRENCY, "RUB");
        appEventsLogger.logEvent(AppEventsConstants.EVENT_NAME_START_TRIAL, 990, params);
    }
}

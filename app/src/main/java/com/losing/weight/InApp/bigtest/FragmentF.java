package com.losing.weight.InApp.bigtest;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import com.losing.weight.Authenticate.POJO.Box;
import com.losing.weight.BuildConfig;
import com.losing.weight.Config;
import com.losing.weight.EntryPoint.ActivitySplash;
import com.losing.weight.InApp.Fragments.slides.SlideFragment;
import com.losing.weight.InApp.Fragments.slides.TopSlideFragment;
import com.losing.weight.InApp.IDs;
import com.losing.weight.InApp.properties.CheckAndSetPurchase;
import com.losing.weight.InApp.properties.SingletonMakePurchase;
import com.losing.weight.OtherActivity.ActivityPrivacyPolicy;
import com.losing.weight.R;
import com.losing.weight.common.Analytics.AMRevenue;
import com.losing.weight.common.Analytics.EventProperties;
import com.losing.weight.common.Analytics.Events;
import com.losing.weight.common.Analytics.SavedConst;
import com.losing.weight.presentation.profile.questions.AfterQuestionsActivity;
import com.losing.weight.views.DotIndicatorView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class FragmentF extends Fragment
    implements PurchasesUpdatedListener {


  private BillingClient billingClient;
  private static final String TAG = "inappbilling";
  private String currentSKU = "trial_article_long_result_stop_3d_3m_2k", currentPrice = "99р";
  private SharedPreferences sharedPreferences;
  Unbinder unbinder;
  private static final String TAG_BOX = "TAG_BOX";
  private Box box;
  public static final String BUY_NOW = "BUY_NOW";

  public static FragmentF newInstance(Box box) {
    Bundle bundle = new Bundle();
    bundle.putSerializable(TAG_BOX, box);
    FragmentF fragment =
        new FragmentF();
    fragment.setArguments(bundle);
    return fragment;
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    //View view = inflater.inflate(R.layout.fragment_subscription_one_button_2, container, false);
    View view = inflater.inflate(R.layout.fragment_premium_f_bt
        , container, false);
    unbinder = ButterKnife.bind(this, view);

    box = (Box) getArguments().getSerializable(TAG_BOX);


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

    ViewPager viewPager = view.findViewById(R.id.viewPager);
    DotIndicatorView indicatorView = view.findViewById(R.id.dotsIndicator);
    indicatorView.setCirclesCount(4);
    indicatorView.setActiveIndex(0);

    viewPager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
      @NonNull
      @Override
      public Fragment getItem(int position) {
        return SlideFragment.newInstance(position);
      }

      @Override
      public int getCount() {
        return 4;
      }
    });

    viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
      @Override
      public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

      }

      @Override
      public void onPageSelected(int position) {
        indicatorView.setActiveIndex(position);
      }

      @Override
      public void onPageScrollStateChanged(int state) {

      }
    });

    ViewPager topViewPager = view.findViewById(R.id.topVp);
    DotIndicatorView indicatorViewTop = view.findViewById(R.id.dotsIndicatorTop);
    indicatorViewTop.setCirclesCount(2);
    indicatorViewTop.setActiveIndex(0);


    topViewPager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
      @NonNull
      @Override
      public Fragment getItem(int position) {
        return TopSlideFragment.newInstance(position);
      }

      @Override
      public int getCount() {
        return 2;
      }
    });

    topViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
      @Override
      public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

      }

      @Override
      public void onPageSelected(int position) {
        indicatorViewTop.setActiveIndex(position);
      }

      @Override
      public void onPageScrollStateChanged(int state) {

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

                /*if (responseCode == BillingClient.BillingResponse.OK && skuDetailsList != null) {
                    Log.e("LOL", skuDetailsList.get(0).toString());
                    try {
                        setPrice(skuDetailsList.get(0).getPrice());
                    }catch (Exception ex){
                        Log.d(TAG, "onSkuDetailsResponse: FAIL");
                    }


                } else {
                    Log.d(TAG, "onSkuDetailsResponse: FAIL");
                }*/

      }
    });
  }

  private void setPrice(String price) {

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


      try {
        if (p.isAutoRenewing()) {
          Events.logNewBuy(box.getBuyFrom(), EventProperties.auto_renewal_true, currentSKU);
        } else {
          Events.logNewBuy(box.getBuyFrom(), EventProperties.auto_renewal_false, currentSKU);
        }
      } catch (Exception ex) {
        Events.logSetBuyError(ex.getMessage());
      }


      logTrial();
      try {
        AMRevenue.Companion.trackRevenue(purchases.get(0));
      }catch (Exception ex){
        Log.e("LOL", "YM revenue error");
      }
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
      Intent intent;
      if (box.isOpenFromIntrodaction()) {
        intent = new Intent(getContext(), AfterQuestionsActivity.class);
        if (box.getProfile() != null) {
          intent.putExtra(Config.INTENT_PROFILE, box.getProfile());
        }
        startActivity(intent);
        getActivity().finish();
      } else {
        intent = new Intent(getContext(), ActivitySplash.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
      }

    }
  }


  @OnClick({R.id.btnClose, R.id.btnBuyPrem, R.id.tvPrivacyPolicy})
  public void onViewClicked(View view) {
    switch (view.getId()) {
      case R.id.btnBuyPrem:
        Events.logPushButton(EventProperties.push_button_next, box.getBuyFrom());
        buy(currentSKU);
        break;
            /*case R.id.btnBack:
                Events.logPushButton(EventProperties.push_button_back, box.getBuyFrom());
                getActivity().onBackPressed();
                break;*/
      case R.id.btnClose: {
        Events.logPushButton(EventProperties.push_button_close, box.getBuyFrom());
        if (box.isOpenFromIntrodaction()){
          Intent intent = new Intent(getContext(), AfterQuestionsActivity.class);
          if (box.getProfile() != null) {
            intent.putExtra(Config.INTENT_PROFILE, box.getProfile());
          }
          startActivity(intent);
        }
        getActivity().finish();
      }
      break;
      case R.id.tvPrivacyPolicy:
        Events.logPushButton(EventProperties.push_button_privacy, box.getBuyFrom());
        Intent intent = new Intent(getActivity(), ActivityPrivacyPolicy.class);
        startActivity(intent);
        break;

    }
  }

  private boolean isNeedGoNext() {
    return getActivity().getSharedPreferences(Config.AFTER_PREM_ROAD, Context.MODE_PRIVATE).getBoolean(Config.AFTER_PREM_ROAD, false);
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

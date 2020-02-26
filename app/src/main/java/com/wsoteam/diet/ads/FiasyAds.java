package com.wsoteam.diet.ads;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.wsoteam.diet.R;
import com.wsoteam.diet.utils.Subscription;



import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class FiasyAds {

    public static final int NATIVE_STEP_IN_RECYCLER = 6;

    private static final String ADMOB_AD_UNIT_ID = "ca-app-pub-3940256099942544/2247696110";

    private static InterstitialAd mInterstitialAd;
//    private static UnifiedNativeAdView adView;
    private static UnifiedNativeAd nativeAd;

    private static boolean isRefreshLocked = false;


    private static MutableLiveData<UnifiedNativeAd> nativeAdView = new MutableLiveData<>();


    public static LiveData<UnifiedNativeAd> getLiveDataAdView(){
        return nativeAdView;
    }

    public static void init(Context context){
        if (Subscription.check(context)) return;

        MobileAds.initialize(context.getApplicationContext(), initializationStatus -> {
            Log.d("kkk", initializationStatus.toString());
            initInterstitial(context);
            refreshAd(context);
        });
    }

    public static void refreshAd(Context context) {
        if (Subscription.check(context)) return;

        isRefreshLocked = true;

        AdLoader.Builder builder = new AdLoader.Builder(context.getApplicationContext(), ADMOB_AD_UNIT_ID /*context.getString(R.string.admob_native)*/);

        // OnUnifiedNativeAdLoadedListener implementation.
        builder.forUnifiedNativeAd(unifiedNativeAd -> {
            // You must call destroy on old ads when you are done with them,
            // otherwise you will have a memory leak.
            if (nativeAd != null) {
                nativeAd.destroy();
            }
            nativeAd = unifiedNativeAd;
            Log.d("kkk", "++" + unifiedNativeAd);
            nativeAdView.setValue(unifiedNativeAd);
            isRefreshLocked = false;

        });

        VideoOptions videoOptions = new VideoOptions.Builder()
                .setStartMuted(false)
                .build();

        NativeAdOptions adOptions = new NativeAdOptions.Builder()
                .setVideoOptions(videoOptions)
                .build();

        builder.withNativeAdOptions(adOptions);

        AdLoader adLoader = builder.withAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(int errorCode) {
                isRefreshLocked = false;
                Toast.makeText(context, "Failed to load native ad: "
                        + errorCode, Toast.LENGTH_SHORT).show();
            }
        }).build();

        adLoader.loadAd(new AdRequest.Builder().build());

    }


    private static void initInterstitial(Context context){
        mInterstitialAd = new InterstitialAd(context.getApplicationContext());
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
    }

    public static void openInter(){

        if (mInterstitialAd == null) return;

        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            mInterstitialAd.loadAd(new AdRequest.Builder().build());
            Log.d("TAG", "The interstitial wasn't loaded yet.");
        }

    }

}

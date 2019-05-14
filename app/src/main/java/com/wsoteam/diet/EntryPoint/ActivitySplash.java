package com.wsoteam.diet.EntryPoint;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.adjust.sdk.Adjust;
import com.adjust.sdk.AdjustEvent;
import com.amplitude.api.Amplitude;
import com.amplitude.api.Identify;
import com.amplitude.api.Revenue;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.bumptech.glide.Glide;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.wsoteam.diet.ABConfig;
import com.wsoteam.diet.AmplitudaEvents;
import com.wsoteam.diet.Amplitude.AmplitudeUserProperties;
import com.wsoteam.diet.Articles.ItemArticleActivity;
import com.wsoteam.diet.Authenticate.ActivityAuthenticate;
import com.wsoteam.diet.BranchProfile.ActivityEditProfile;
import com.wsoteam.diet.Config;
import com.wsoteam.diet.EventsAdjust;
import com.wsoteam.diet.FirebaseUserProperties;
import com.wsoteam.diet.MainScreen.MainActivity;
import com.wsoteam.diet.R;
import com.wsoteam.diet.Sync.POJO.UserData;
import com.wsoteam.diet.Sync.UserDataHolder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActivitySplash extends Activity {
    @BindView(R.id.auth_first_iv_image) ImageView authFirstIvImage;
    @BindView(R.id.tvSplashText) ImageView tvSplashText;
    private FirebaseUser user;

    private BillingClient mBillingClient;
    private SharedPreferences isBuyPrem, freeUser;
    private final String TAG_FIRST_RUN = "TAG_FIRST_RUN",
            ONE_MONTH_SKU = "basic_subscription_1m", THREE_MONTH_SKU = "basic_subscription_3m",
            ONE_YEAR_SKU = "basic_subscription_12m", ONE_YEAR_TRIAL_SKU = "basic_subscription_12m_trial";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ButterKnife.bind(this);
        Glide.with(this).load(R.drawable.fiasy_text_load).into(tvSplashText);
        Glide.with(this).load(R.drawable.logo_for_onboard).into(authFirstIvImage);
        if (!hasConnection(this)) {
            Toast.makeText(this, R.string.check_internet_connection, Toast.LENGTH_SHORT).show();
        }

        startActivity(new Intent(this, ItemArticleActivity.class));
//        FacebookSdk.sdkInitialize(getApplicationContext());
//        AppEventsLogger.activateApp(this);
//        checkFirstLaunch();
//        checkBilling();
//        checkRegistrationAndRun();
    }

    private void checkRegistrationAndRun() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            FirebaseAnalytics.getInstance(this).setUserProperty(FirebaseUserProperties.REG_STATUS, FirebaseUserProperties.reg);
            AmplitudeUserProperties.setUserProperties(AmplitudaEvents.REG_STATUS, AmplitudaEvents.registered);
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference(Config.NAME_OF_USER_DATA_LIST_ENTITY).
                    child(FirebaseAuth.getInstance().getCurrentUser().getUid());

            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    new UserDataHolder().bindObjectWithHolder(dataSnapshot.getValue(UserData.class));

                    boolean isPrem = getSharedPreferences(Config.STATE_BILLING, MODE_PRIVATE).getBoolean(Config.STATE_BILLING, false);
                    Intent intent;
                    if (isPrem) {
                        intent = new Intent(ActivitySplash.this, MainActivity.class);
                    } else {
                        intent = new Intent(ActivitySplash.this, MainActivity.class);
                    }
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        } else {
            FirebaseAnalytics.getInstance(this).setUserProperty(FirebaseUserProperties.REG_STATUS, FirebaseUserProperties.un_reg);
            AmplitudeUserProperties.setUserProperties(AmplitudaEvents.REG_STATUS, AmplitudaEvents.unRegistered);
            if (getSharedPreferences(Config.IS_NEED_SHOW_ONBOARD, MODE_PRIVATE).getBoolean(Config.IS_NEED_SHOW_ONBOARD, false)) {
                Amplitude.getInstance().logEvent(AmplitudaEvents.free_enter);
                startActivity(new Intent(this, ActivityEditProfile.class).putExtra("registration", true));
            } else {
                startActivity(new Intent(ActivitySplash.this, ActivityAuthenticate.class));
            }
            finish();


        }
    }

    private void checkBilling() {
        mBillingClient = BillingClient.newBuilder(this).setListener(new PurchasesUpdatedListener() {
            @Override
            public void onPurchasesUpdated(int responseCode, @Nullable List<Purchase> purchases) {
                if (responseCode == BillingClient.BillingResponse.OK && purchases != null) {
                    //сюда мы попадем когда будет осуществлена покупка

                }
            }
        }).build();
        mBillingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(@BillingClient.BillingResponse int billingResponseCode) {
                if (billingResponseCode == BillingClient.BillingResponse.OK) {
                    //здесь мы можем запросить информацию о товарах и покупках
                    List<Purchase> purchasesList = queryPurchases(); //запрос о покупках
                    if (purchasesList.size() > 0) {
                        Log.e("LOL", purchasesList.get(0).getOriginalJson());
                        isTrial(purchasesList.get(0).getOriginalJson());
                        for (int i = 0; i < purchasesList.size(); i++) {
                            if (purchasesList.get(i).getSku().equals(ONE_MONTH_SKU)) {
                                setPremStatus(ONE_MONTH_SKU, AmplitudaEvents.ONE_MONTH_PRICE,
                                        false, isApproved(purchasesList.get(i).getOriginalJson()));

                            } else if (purchasesList.get(i).getSku().equals(THREE_MONTH_SKU)) {
                                setPremStatus(THREE_MONTH_SKU, AmplitudaEvents.THREE_MONTH_PRICE, false, true);

                            } else if (purchasesList.get(i).getSku().equals(ONE_YEAR_SKU)) {
                                setPremStatus(ONE_YEAR_SKU, AmplitudaEvents.ONE_YEAR_PRICE, false, true);

                            } else if (purchasesList.get(i).getSku().equals(ONE_YEAR_TRIAL_SKU)) {
                                setPremStatus(ONE_YEAR_SKU, AmplitudaEvents.ONE_YEAR_PRICE,
                                        isTrial(purchasesList.get(i).getOriginalJson()), true);

                            } else {
                                deletePremStatus();
                            }
                        }
                    } else {
                        deletePremStatus();
                    }
                }
            }

            @Override
            public void onBillingServiceDisconnected() {
                //сюда мы попадем если что-то пойдет не так
            }
        });
    }

    private void getABVersion() {
        FirebaseRemoteConfig firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        firebaseRemoteConfig.setDefaults(R.xml.default_config);

        firebaseRemoteConfig.fetch(3600).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    firebaseRemoteConfig.activateFetched();
                    Amplitude.getInstance().logEvent("norm_ab");
                } else {
                    Amplitude.getInstance().logEvent("crash_ab");
                }
                setABTestConfig(firebaseRemoteConfig.getString(ABConfig.REQUEST_STRING));
            }
        });
    }

    private boolean isTrial(String json) {
        Calendar currentTime = Calendar.getInstance();
        long longTrialMilisec = 259200000l;
        long currentMili = currentTime.getTimeInMillis();

        String nameFieldTime = "purchaseTime";
        try {
            JSONObject jsonObject = new JSONObject(json);
            long purchaseMilisec = Long.decode(jsonObject.getString(nameFieldTime));
            if (currentMili - purchaseMilisec >= longTrialMilisec) {
                return false;
            } else {
                return true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return true;
        }
    }

    private boolean isApproved(String json) {
        Calendar currentTime = Calendar.getInstance();
        long longNotApproved = 1200000l;
        long currentMili = currentTime.getTimeInMillis();

        String nameFieldTime = "purchaseTime";
        try {
            JSONObject jsonObject = new JSONObject(json);
            long purchaseMilisec = Long.decode(jsonObject.getString(nameFieldTime));
            if (currentMili - purchaseMilisec >= longNotApproved) {
                return true;
            } else {
                return false;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void setABTestConfig(String responseString) {
        Identify abStatus = new Identify().set(ABConfig.AB_VERSION, responseString);
        Amplitude.getInstance().identify(abStatus);
        getSharedPreferences(ABConfig.KEY_FOR_SAVE_STATE, MODE_PRIVATE).
                edit().putString(ABConfig.KEY_FOR_SAVE_STATE, responseString).
                commit();
    }

    private void checkFirstLaunch() {
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);

        if (!sharedPreferences.getBoolean(TAG_FIRST_RUN, false)) {
            Calendar calendar = Calendar.getInstance();
            String day = String.valueOf(calendar.get(Calendar.DAY_OF_YEAR));
            String week = String.valueOf(calendar.get(Calendar.WEEK_OF_YEAR));
            String month = String.valueOf(calendar.get(Calendar.MONTH) + 1);

            Identify date = new Identify().set(AmplitudaEvents.FIRST_DAY, day)
                    .set(AmplitudaEvents.FIRST_WEEK, week).set(AmplitudaEvents.FIRST_MONTH, month);
            Amplitude.getInstance().identify(date);
            Amplitude.getInstance().logEvent(AmplitudaEvents.first_launch);

            getSharedPreferences(Config.IS_NEED_SHOW_ONBOARD, MODE_PRIVATE).
                    edit().putBoolean(Config.IS_NEED_SHOW_ONBOARD, true).
                    commit();

            getSharedPreferences(Config.STARTING_POINT, MODE_PRIVATE).edit().putLong(Config.STARTING_POINT, getTime()).commit();

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(TAG_FIRST_RUN, true);
            editor.commit();
        }
    }

    private long getTime() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getTimeInMillis();
    }

    private boolean hasConnection(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiInfo != null && wifiInfo.isConnected()) {
            return true;
        }
        wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (wifiInfo != null && wifiInfo.isConnected()) {
            return true;
        }
        wifiInfo = cm.getActiveNetworkInfo();
        if (wifiInfo != null && wifiInfo.isConnected()) {
            return true;
        }
        return false;
    }

    private void speakAboutButAfterTrial() {
        if (getSharedPreferences(Config.IS_SPEAK_ABOUT_BUY, MODE_PRIVATE).getInt(Config.IS_SPEAK_ABOUT_BUY, -1) == 0) {
            getSharedPreferences(Config.IS_SPEAK_ABOUT_BUY, MODE_PRIVATE).edit().putInt(Config.IS_SPEAK_ABOUT_BUY, 1).commit();

            Revenue revenue = new Revenue().setProductId(getPackageName()).setQuantity(1).setPrice(990);
            Amplitude.getInstance().logRevenueV2(revenue);
            Adjust.trackEvent(new AdjustEvent(EventsAdjust.buy_after_trial));
        }
    }

    private void setPremStatus(String durationPrem, String pricePrem, boolean isTrial, boolean isApproved) {
        isBuyPrem = getSharedPreferences(Config.STATE_BILLING, MODE_PRIVATE);
        SharedPreferences.Editor editor = isBuyPrem.edit();
        editor.putBoolean(Config.STATE_BILLING, true);
        editor.commit();

        Identify premStatus = new Identify().set(AmplitudaEvents.LONG_OF_PREM, durationPrem)
                .set(AmplitudaEvents.PRICE_OF_PREM, pricePrem);

        if (durationPrem.equals(ONE_MONTH_SKU)) {
            premStatus.set(AmplitudaEvents.PREM_STATUS, AmplitudaEvents.buy);
            if (isApproved) {
                speakAboutApprovedBuy();
            } else {
                getSharedPreferences(Config.IS_SPEAK_ABOUT_APPROVED_BUY, MODE_PRIVATE).
                        edit().putInt(Config.IS_SPEAK_ABOUT_APPROVED_BUY, 0).
                        commit();
            }
        } else {
            if (isTrial) {
                premStatus.set(AmplitudaEvents.PREM_STATUS, AmplitudaEvents.trial);
                getSharedPreferences(Config.IS_SPEAK_ABOUT_BUY, MODE_PRIVATE).edit().putInt(Config.IS_SPEAK_ABOUT_BUY, 0).commit();
            } else {
                speakAboutButAfterTrial();
                premStatus.set(AmplitudaEvents.PREM_STATUS, AmplitudaEvents.buy);
            }
        }

        Amplitude.getInstance().identify(premStatus);
        FirebaseAnalytics.getInstance(this).setUserProperty(FirebaseUserProperties.PREM_STATUS, FirebaseUserProperties.buy);
    }

    private void speakAboutApprovedBuy() {
        if (getSharedPreferences(Config.IS_SPEAK_ABOUT_APPROVED_BUY, MODE_PRIVATE).
                getInt(Config.IS_SPEAK_ABOUT_APPROVED_BUY, -1) == 0) {
            Revenue revenue = new Revenue().setProductId(getPackageName()).setPrice(299).setQuantity(1);
            Amplitude.getInstance().logRevenueV2(revenue);
        }
    }

    private void deletePremStatus() {
        isBuyPrem = getSharedPreferences(Config.STATE_BILLING, MODE_PRIVATE);
        SharedPreferences.Editor editor = isBuyPrem.edit();
        editor.putBoolean(Config.STATE_BILLING, false);
        editor.commit();
        Identify deletePremStatus = new Identify().set(AmplitudaEvents.PREM_STATUS, AmplitudaEvents.not_buy)
                .unset(AmplitudaEvents.LONG_OF_PREM)
                .unset(AmplitudaEvents.PRICE_OF_PREM);
        Amplitude.getInstance().identify(deletePremStatus);
        FirebaseAnalytics.getInstance(this).setUserProperty(FirebaseUserProperties.PREM_STATUS, FirebaseUserProperties.un_buy);
    }


    private List<Purchase> queryPurchases() {
        Purchase.PurchasesResult purchasesResult = mBillingClient.queryPurchases(BillingClient.SkuType.SUBS);
        return purchasesResult.getPurchasesList();
    }
}
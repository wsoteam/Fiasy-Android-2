package com.wsoteam.diet.OtherActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.WindowManager;

import android.widget.Toast;

import com.adjust.sdk.Adjust;
import com.adjust.sdk.AdjustEvent;
import com.amplitude.api.Amplitude;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wsoteam.diet.AmplitudaEvents;
import com.wsoteam.diet.Authenticate.ActivityAuthenticate;
import com.wsoteam.diet.Config;
import com.wsoteam.diet.EventsAdjust;
import com.wsoteam.diet.MainScreen.MainActivity;
import com.wsoteam.diet.Onboarding.OnboardingActivity;
import com.wsoteam.diet.R;
import com.wsoteam.diet.Sync.POJO.UserData;
import com.wsoteam.diet.Sync.UserDataHolder;
import com.wsoteam.diet.tvoytrener.PortionSize;

import java.util.List;

public class ActivitySplash extends AppCompatActivity {

    private String TAG = "splash";
    private FirebaseUser user;

    private BillingClient mBillingClient;
    private SharedPreferences countOfRun;
    private final String TAG_FIRST_RUN = "TAG_FIRST_RUN";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Amplitude.getInstance().initialize(this, "b148a2e64cc862b4efb10865dfd4d579")
                .enableForegroundTracking(getApplication());

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        checkFirstLaunch();

        new PortionSize();

        if (!hasConnection(this)) {
            Toast.makeText(this, R.string.check_internet_connection, Toast.LENGTH_SHORT).show();
        }


        user = FirebaseAuth.getInstance().getCurrentUser();


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

                    checkSub("basic_subscription_1m", purchasesList);
                    checkSub("basic_subscription_3m", purchasesList);
                    checkSub("basic_subscription_4m", purchasesList);


                }
            }

            @Override
            public void onBillingServiceDisconnected() {
                //сюда мы попадем если что-то пойдет не так
            }
        });


        if (user != null) {
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
//                       intent = new Intent(ActivitySplash.this, ActivitySubscription.class);
                    }
                    startActivity(intent);
                    finish();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } else {
//            startActivity(new Intent(ActivitySplash.this, OnboardingActivity.class));
            startActivity(new Intent(ActivitySplash.this, ActivityAuthenticate.class));
            finish();
        }


    }

    private void checkFirstLaunch() {
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);

        if (sharedPreferences.getBoolean(TAG_FIRST_RUN, false)) {
            Adjust.trackEvent(new AdjustEvent(EventsAdjust.first_launch));
            Amplitude.getInstance().logEvent(AmplitudaEvents.first_launch);

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(TAG_FIRST_RUN, true);
            editor.commit();
        }

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

    private void payComplete() {
        countOfRun = getSharedPreferences(Config.STATE_BILLING, MODE_PRIVATE);
        SharedPreferences.Editor editor = countOfRun.edit();
        editor.putBoolean(Config.STATE_BILLING, true);
        editor.commit();
    }


    private List<Purchase> queryPurchases() {
        Purchase.PurchasesResult purchasesResult = mBillingClient.queryPurchases(BillingClient.SkuType.SUBS);
        return purchasesResult.getPurchasesList();
    }

    private void checkSub(String mSkuId, List<Purchase> purchasesList) {

        //если товар уже куплен, предоставить его пользователю
        for (int i = 0; i < purchasesList.size(); i++) {
            String purchaseId = purchasesList.get(i).getSku();
            if (TextUtils.equals(mSkuId, purchaseId)) {
                payComplete();
                Log.d(TAG, "checkSub: payComplete");
            }
        }
    }


}

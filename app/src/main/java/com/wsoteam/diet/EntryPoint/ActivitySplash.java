package com.wsoteam.diet.EntryPoint;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.adjust.sdk.Adjust;
import com.adjust.sdk.AdjustAttribution;
import com.amplitude.api.Amplitude;
import com.amplitude.api.Identify;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.Purchase;
import com.bumptech.glide.Glide;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
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
import com.wsoteam.diet.Amplitude.SetUserProperties;
import com.wsoteam.diet.Config;
import com.wsoteam.diet.FirebaseUserProperties;
import com.wsoteam.diet.InApp.IDs;
import com.wsoteam.diet.InApp.properties.CheckAndSetPurchase;
import com.wsoteam.diet.InApp.properties.EmptySubInfo;
import com.wsoteam.diet.InApp.properties.SingletonMakePurchase;
import com.wsoteam.diet.MainScreen.MainActivity;
import com.wsoteam.diet.POJOProfile.SubInfo;
import com.wsoteam.diet.POJOProfile.TrackInfo;
import com.wsoteam.diet.R;
import com.wsoteam.diet.Sync.POJO.UserData;
import com.wsoteam.diet.Sync.UserDataHolder;
import com.wsoteam.diet.Sync.WorkWithFirebaseDB;
import com.wsoteam.diet.presentation.global.BaseActivity;
import com.wsoteam.diet.presentation.intro.IntroActivity;
import com.wsoteam.diet.presentation.profile.edit.EditProfileActivity;

import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActivitySplash extends BaseActivity {
    @BindView(R.id.auth_first_iv_image) ImageView authFirstIvImage;
    @BindView(R.id.tvSplashText) ImageView tvSplashText;
    private FirebaseUser user;

    private BillingClient mBillingClient;
    private SharedPreferences isBuyPrem;
    private final String TAG_FIRST_RUN = "TAG_FIRST_RUN";

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


//        startActivity(new Intent(this, ForTestFragmentActivity.class));
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        checkFirstLaunch();
        checkRegistrationAndRun();
    }

    private void checkRegistrationAndRun() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            SetUserProperties.setUserProperties(Adjust.getAttribution());
            FirebaseAnalytics.getInstance(this).setUserProperty(FirebaseUserProperties.REG_STATUS, FirebaseUserProperties.reg);
            AmplitudeUserProperties.setUserProperties(AmplitudaEvents.REG_STATUS, AmplitudaEvents.registered);
            setTrackInfoInDatabase(Adjust.getAttribution());
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference(Config.NAME_OF_USER_DATA_LIST_ENTITY).
                    child(FirebaseAuth.getInstance().getCurrentUser().getUid());

            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    new UserDataHolder().bindObjectWithHolder(dataSnapshot.getValue(UserData.class));
                    checkBilling();
                    startActivity(new Intent(ActivitySplash.this, MainActivity.class));
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
                startActivity(new Intent(this, EditProfileActivity.class).putExtra("registration", true));
            } else {
                startActivity(new Intent(ActivitySplash.this, IntroActivity.class));
            }
            finish();
        }
    }

    private void setTrackInfoInDatabase(AdjustAttribution aa) {
        TrackInfo trackInfo = new TrackInfo();
        trackInfo.setTt(aa.trackerToken);
        trackInfo.setTn(aa.trackerName);
        trackInfo.setNet(aa.network);
        trackInfo.setCam(aa.campaign);
        trackInfo.setCre(aa.creative);
        trackInfo.setCl(aa.clickLabel);
        trackInfo.setAdid(aa.adid);
        trackInfo.setAdg(aa.adgroup);
        WorkWithFirebaseDB.setTrackInfo(trackInfo);
    }

    private void checkBilling() {
        if (SingletonMakePurchase.getInstance().isMakePurchaseNow()) {
            changePremStatus(true);
            AmplitudeUserProperties.setUserProperties(AmplitudaEvents.PREM_STATUS, AmplitudaEvents.buy);
        } else if (UserDataHolder.getUserData() != null && UserDataHolder.getUserData().getSubInfo() == null) {
            //unknown status premium or new user
            setSubInfoWithGooglePlayInfo();
        } else if (UserDataHolder.getUserData() != null && UserDataHolder.getUserData().getSubInfo() != null
                && !UserDataHolder.getUserData().getSubInfo().getProductId().equals(IDs.EMPTY_SUB)) {
            //user have premium status, check time of premium
            SubInfo subInfo = UserDataHolder.getUserData().getSubInfo();
            if (subInfo.getPaymentState() == 0) {
                changePremStatus(false);
                AmplitudeUserProperties.setUserProperties(AmplitudaEvents.PREM_STATUS, AmplitudaEvents.preferential);
                new CheckAndSetPurchase().execute(subInfo.getProductId(), subInfo.getPurchaseToken(), subInfo.getPackageName());
            } else if (subInfo.getPaymentState() != 0) {
                compareTime(subInfo);
            }
        } else if (UserDataHolder.getUserData() != null
                && UserDataHolder.getUserData().getSubInfo() != null
                && UserDataHolder.getUserData().getSubInfo().getProductId().equals(IDs.EMPTY_SUB)) {
            AmplitudeUserProperties.setUserProperties(AmplitudaEvents.PREM_STATUS, AmplitudaEvents.not_buy);
            changePremStatus(false);
        }

    }

    private void compareTime(SubInfo subInfo) {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime > subInfo.getExpiryTimeMillis()) {
            new CheckAndSetPurchase().execute(subInfo.getProductId(), subInfo.getPurchaseToken(), subInfo.getPackageName());
        } else {
            if (subInfo.getPaymentState() == 1) {
                AmplitudeUserProperties.setUserProperties(AmplitudaEvents.PREM_STATUS, AmplitudaEvents.buy);
            }else if (subInfo.getPaymentState() == 2){
                AmplitudeUserProperties.setUserProperties(AmplitudaEvents.PREM_STATUS, AmplitudaEvents.trial);
            }
            changePremStatus(true);
        }
    }

    private void setSubInfoWithGooglePlayInfo() {
        mBillingClient = BillingClient.newBuilder(this).setListener((responseCode, purchases) -> {
            if (responseCode == BillingClient.BillingResponse.OK && purchases != null) {
                //сюда мы попадем когда будет осуществлена покупка

            }
        }).build();
        mBillingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(@BillingClient.BillingResponse int billingResponseCode) {
                if (billingResponseCode == BillingClient.BillingResponse.OK) {
                    List<Purchase> purchasesList = queryPurchases();
                    if (purchasesList.size() > 0) {
                        changePremStatus(true);
                        setSubInfo(purchasesList.get(0));
                    } else {
                        AmplitudeUserProperties.setUserProperties(AmplitudaEvents.PREM_STATUS, AmplitudaEvents.not_buy);
                        EmptySubInfo.setEmptySubInfo();
                        changePremStatus(false);
                    }
                }
            }

            @Override
            public void onBillingServiceDisconnected() {
                //сюда мы попадем если что-то пойдет не так
            }
        });
    }

    private void setSubInfo(Purchase purchase) {
        new CheckAndSetPurchase().execute(purchase.getSku(), purchase.getPurchaseToken(), purchase.getPackageName());
    }

    private void getABVersion() {
        FirebaseRemoteConfig firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        firebaseRemoteConfig.setDefaults(R.xml.default_config);

        firebaseRemoteConfig.fetch(3600).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                firebaseRemoteConfig.activateFetched();
                Amplitude.getInstance().logEvent("norm_ab");
            } else {
                Amplitude.getInstance().logEvent("crash_ab");
            }
            setABTestConfig(firebaseRemoteConfig.getString(ABConfig.REQUEST_STRING));
        });
    }


    private void setABTestConfig(String responseString) {
        Identify abStatus = new Identify().set(ABConfig.AB_VERSION, responseString);
        Amplitude.getInstance().identify(abStatus);
        getSharedPreferences(ABConfig.KEY_FOR_SAVE_STATE, MODE_PRIVATE).
                edit().putString(ABConfig.KEY_FOR_SAVE_STATE, responseString).
                apply();
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
                    apply();

            getSharedPreferences(Config.STARTING_POINT, MODE_PRIVATE).edit().putLong(Config.STARTING_POINT, getTime()).apply();

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(TAG_FIRST_RUN, true);
            editor.apply();
        }
    }

    private long getTime() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getTimeInMillis();
    }

    private List<Purchase> queryPurchases() {
        Purchase.PurchasesResult purchasesResult = mBillingClient.queryPurchases(BillingClient.SkuType.SUBS);
        return purchasesResult.getPurchasesList();
    }

    private void changePremStatus(boolean isPremUser) {
        getSharedPreferences(Config.STATE_BILLING, MODE_PRIVATE).edit().putBoolean(Config.STATE_BILLING, isPremUser).apply();
    }
}
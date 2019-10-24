package com.wsoteam.diet;

import android.app.Activity;
import android.os.Bundle;

import androidx.multidex.MultiDexApplication;
import androidx.room.Room;

import com.adjust.sdk.Adjust;
import com.adjust.sdk.AdjustConfig;
import com.amplitude.api.Amplitude;
import com.bugsee.library.Bugsee;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;
import com.onesignal.OneSignal;
import com.orm.SugarContext;
import com.wsoteam.diet.BranchOfAnalyzer.POJOFoodSQL.FoodDatabase;

import com.wsoteam.diet.Sync.WorkWithFirebaseDB;
import io.intercom.android.sdk.Intercom;

import static com.adjust.sdk.AdjustConfig.ENVIRONMENT_PRODUCTION;
import static com.google.firebase.database.Logger.Level.INFO;
import static com.wsoteam.diet.EventsAdjust.app_token;

public class App extends MultiDexApplication {
    public static App instance;

    private FoodDatabase foodDatabase;
    private boolean setupOnDemand = true;

    public long now;

    public static App getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        now = System.currentTimeMillis();

        FirebaseApp.initializeApp(getApplicationContext());
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        WorkWithFirebaseDB.setFirebaseStateListener();

        instance = this;

        // OneSignal Initialization
        OneSignal.startInit(this)
            .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
            .unsubscribeWhenNotificationsAreDisabled(true)
            .init();

        Amplitude.getInstance().trackSessionEvents(true);
        Amplitude.getInstance().initialize(this, "b148a2e64cc862b4efb10865dfd4d579")
            .enableForegroundTracking(this);

        Intercom.initialize(this, "android_sdk-bceadc40bc17510359f5ad43a72281735676eea2", "dr8zfmz4");

        //SetUserProperties.setUserProperties(Adjust.getAttribution());
    }

    public void setupOnDemand(){
        if (setupOnDemand) {
            setupOnDemand = false;

            SugarContext.init(this);
            Bugsee.launch(this, "b9f4ece5-898c-48fe-9938-ef42d8593a95");

            Adjust.onCreate(new AdjustConfig(this, app_token, ENVIRONMENT_PRODUCTION));
            registerActivityLifecycleCallbacks(new AdjustLifecycleCallbacks());
        }
    }

    public FoodDatabase getFoodDatabase() {
        if (foodDatabase == null) {
            foodDatabase = Room.databaseBuilder(this, FoodDatabase.class, "foodDB.db")
                .build();
        }
        return foodDatabase;
    }

    private static final class AdjustLifecycleCallbacks implements ActivityLifecycleCallbacks {
        @Override
        public void onActivityCreated(Activity activity, Bundle bundle) {

        }

        @Override
        public void onActivityStarted(Activity activity) {

        }

        @Override
        public void onActivityResumed(Activity activity) {
            Adjust.onResume();
        }

        @Override
        public void onActivityPaused(Activity activity) {
            Adjust.onPause();
        }

        @Override
        public void onActivityStopped(Activity activity) {
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {

        }
    }

}
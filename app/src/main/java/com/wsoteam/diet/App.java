package com.wsoteam.diet;

import android.content.Context;
import android.util.Log;

import com.amplitude.api.Amplitude;
import com.bugsee.library.Bugsee;
import com.google.firebase.FirebaseApp;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.onesignal.OneSignal;
import com.wsoteam.diet.BranchOfAnalyzer.POJOFoodSQL.FoodDatabase;
import com.yandex.metrica.YandexMetrica;
import com.yandex.metrica.YandexMetricaConfig;

import java.util.HashMap;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.multidex.MultiDexApplication;
import androidx.room.Room;


public class App extends MultiDexApplication {
    public static App instance;
    private static FirebaseAnalytics analytics;

    private FoodDatabase foodDatabase;

    public long now;
    private static Context context;

    public static App getInstance() {
        return instance;
    }

    public static FirebaseAnalytics getFAInstance(){
        return analytics;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseApp.initializeApp(this);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        Bugsee.launch(this, "b9f4ece5-898c-48fe-9938-ef42d8593a95");
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        FirebaseRemoteConfig.getInstance().setDefaults(new HashMap<String, Object>() {{
            put("premium_theme", "dark");
            put("premium_with_trial", true);
        }});

        FirebaseRemoteConfig.getInstance().fetchAndActivate();

        Amplitude.getInstance().trackSessionEvents(true);
        Amplitude.getInstance().initialize(this, "b148a2e64cc862b4efb10865dfd4d579")
                .enableForegroundTracking(this);
        instance = this;
        analytics = FirebaseAnalytics.getInstance(this);
        foodDatabase = Room.databaseBuilder(this, FoodDatabase.class, "foodDB.db")
                .fallbackToDestructiveMigration()
                .build();

        // OneSignal Initialization
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();

        if (BuildConfig.DEBUG) {
            OneSignal.idsAvailable((userId, registrationId) -> {
                Log.d("OneSignalId", "user_id=" + userId);
            });
        }

        context = this;

        YandexMetricaConfig config = YandexMetricaConfig.newConfigBuilder("29344c16-aee8-4945-b2bb-df19320f37e5").build();
        YandexMetrica.activate(getApplicationContext(), config);
        YandexMetrica.enableActivityAutoTracking(this);
    }


    public static Context getContext() {
        return context;
    }

    public FoodDatabase getFoodDatabase() {
        return foodDatabase;
    }




}
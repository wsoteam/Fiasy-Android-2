package com.wsoteam.diet;

import android.app.Activity;
import android.app.Application;
import android.arch.persistence.room.Room;
import android.os.Bundle;
import android.support.multidex.MultiDexApplication;
import android.support.v4.app.Fragment;

import com.adjust.sdk.Adjust;
import com.adjust.sdk.AdjustConfig;
import com.amplitude.api.Amplitude;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;
import com.orm.SugarContext;
import com.wsoteam.diet.BranchOfAnalyzer.POJOFoodSQL.FoodDatabase;
import com.wsoteam.diet.di.AppComponent;
import com.wsoteam.diet.di.DaggerAppComponent;
import com.yandex.metrica.YandexMetrica;
import com.yandex.metrica.YandexMetricaConfig;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import dagger.android.HasFragmentInjector;
import dagger.android.support.HasSupportFragmentInjector;
import io.intercom.android.sdk.Intercom;

public class App extends Application
        implements HasActivityInjector, HasSupportFragmentInjector {
    public static App instance;
    @Inject
    DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;
    @Inject
    DispatchingAndroidInjector<Fragment> fragmentInjector;
    private FoodDatabase foodDatabase;
    private AppComponent mComponent;

    public static App getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        SugarContext.init(this);
        YandexMetricaConfig.Builder configBuilder = YandexMetricaConfig.newConfigBuilder(Config.YANDEX_API_KEY);
        YandexMetrica.activate(getApplicationContext(), configBuilder.build());
        YandexMetrica.enableActivityAutoTracking(this);
        FirebaseApp.initializeApp(getApplicationContext());
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
//        Bugsee.launch(this, "b9f4ece5-898c-48fe-9938-ef42d8593a95");
        Adjust.onCreate(new AdjustConfig(this, EventsAdjust.app_token, AdjustConfig.ENVIRONMENT_PRODUCTION));
        registerActivityLifecycleCallbacks(new AdjustLifecycleCallbacks());
        Amplitude.getInstance().trackSessionEvents(true);
        Amplitude.getInstance().initialize(this, "b148a2e64cc862b4efb10865dfd4d579")
                .enableForegroundTracking(this);
        Intercom.initialize(this, "android_sdk-bceadc40bc17510359f5ad43a72281735676eea2", "dr8zfmz4");
        instance = this;
        foodDatabase = Room.databaseBuilder(this, FoodDatabase.class, "foodDB.db").build();

        //SetUserProperties.setUserProperties(Adjust.getAttribution());

        mComponent = DaggerAppComponent.builder().application(this).build();
        mComponent.inject(this);
    }

    @Override
    public AndroidInjector<android.support.v4.app.Fragment> supportFragmentInjector() {
        return fragmentInjector;
    }

    //
    @Override
    public AndroidInjector<Activity> activityInjector() {
        return dispatchingAndroidInjector;
    }

    public FoodDatabase getFoodDatabase() {
        return foodDatabase;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        SugarContext.terminate();
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
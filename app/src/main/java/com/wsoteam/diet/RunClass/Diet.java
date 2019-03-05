package com.wsoteam.diet.RunClass;

import android.app.Application;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.orm.SugarContext;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKAccessTokenTracker;
import com.vk.sdk.VKSdk;
import com.wsoteam.diet.Config;
import com.wsoteam.diet.MainScreen.MainActivity;
import com.yandex.metrica.YandexMetrica;
import com.yandex.metrica.YandexMetricaConfig;

public class Diet extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SugarContext.init(this);
        YandexMetricaConfig.Builder configBuilder = YandexMetricaConfig.newConfigBuilder(Config.YANDEX_API_KEY);
        YandexMetrica.activate(getApplicationContext(), configBuilder.build());
        YandexMetrica.enableActivityAutoTracking(this);

        vkAccessTokenTracker.startTracking();
        VKSdk.initialize(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        SugarContext.terminate();
    }

    VKAccessTokenTracker vkAccessTokenTracker = new VKAccessTokenTracker() {
        @Override
        public void onVKAccessTokenChanged(@Nullable VKAccessToken oldToken, @Nullable VKAccessToken newToken) {
            if (newToken == null) {
                Intent intent = new Intent(Diet.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }
    };
}

package com.wsoteam.diet.di;

import android.content.Context;

import com.wsoteam.diet.App;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    @Provides
    @Singleton
    public static Context context(App app) {
        return app.getApplicationContext();
    }

}

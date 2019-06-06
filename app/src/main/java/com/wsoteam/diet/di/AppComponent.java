package com.wsoteam.diet.di;

import android.app.Application;

import com.wsoteam.diet.App;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

@Component(modules = {
        AppModule.class,
        CiceroneModule.class,
        ActivityBuilder.class,
        AndroidSupportInjectionModule.class})
@Singleton
public interface AppComponent extends AndroidInjector<App> {

    @Component.Builder
    interface Builder {

        @BindsInstance
        Builder application(Application application);

        AppComponent build();
    }
}

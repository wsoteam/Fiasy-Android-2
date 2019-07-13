package com.wsoteam.diet.di;

import android.app.Application;

import com.wsoteam.diet.App;
import com.wsoteam.diet.presentation.profile.section.ProfileFragment;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

@Component(modules = {
        AppModule.class,
        CiceroneModule.class,
        ActivityBuilder.class,
        FragmentBuilder.class,
        PresentersModule.class,
        AndroidSupportInjectionModule.class
        })
@Singleton
public interface AppComponent extends AndroidInjector<App> {

    @Component.Builder
    interface Builder {

        @BindsInstance
        Builder application(Application application);

        AppComponent build();
    }

    @Override
    void inject(App app);
}

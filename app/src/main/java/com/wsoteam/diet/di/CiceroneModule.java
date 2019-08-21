package com.wsoteam.diet.di;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.terrakok.cicerone.Cicerone;
import ru.terrakok.cicerone.NavigatorHolder;
import ru.terrakok.cicerone.Router;

@Module
public class CiceroneModule {

    @Provides
    @Singleton
    Cicerone<Router> provideModule() {
        return CiceroneInject.instance();
    }

    @Provides
    @Singleton
    NavigatorHolder providesNavigationHolder(Cicerone<Router> cicerone) {
        return CiceroneInject.navigator();
    }

    @Singleton
    @Provides
    Router providesRouter(Cicerone<Router> cicerone) {
        return CiceroneInject.router();
    }
}

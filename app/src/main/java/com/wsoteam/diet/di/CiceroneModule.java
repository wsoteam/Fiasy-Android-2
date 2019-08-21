package com.wsoteam.diet.di;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.terrakok.cicerone.Cicerone;
import ru.terrakok.cicerone.NavigatorHolder;
import ru.terrakok.cicerone.Router;

@Module
public class CiceroneModule {


    private static Cicerone<Router> cicerone = Cicerone.create();

    public static Cicerone instance() {
        return cicerone;
    }

    public static NavigatorHolder navigator() {
        return cicerone.getNavigatorHolder();
    }

    public static Router router() {
        return cicerone.getRouter();
    }

    @Provides
    @Singleton
    Cicerone<Router> provideModule() {
        return Cicerone.create();
    }

    @Provides
    @Singleton
    NavigatorHolder providesNavigationHolder(Cicerone<Router> cicerone) {
        return cicerone.getNavigatorHolder();
    }

    @Singleton
    @Provides
    Router providesRouter(Cicerone<Router> cicerone) {
        return cicerone.getRouter();
    }
}

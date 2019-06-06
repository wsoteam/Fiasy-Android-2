package com.wsoteam.diet.di.intro;

import com.wsoteam.diet.presentation.intro.IntroPresenter;

import dagger.Module;
import dagger.Provides;
import ru.terrakok.cicerone.Router;

@Module
public class IntroModule {

    @Provides
    IntroPresenter provideIntroPresenter(Router router) {
        return new IntroPresenter(router);
    }
}
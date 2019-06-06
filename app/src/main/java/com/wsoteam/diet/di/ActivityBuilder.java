package com.wsoteam.diet.di;

import com.wsoteam.diet.di.intro.IntroModule;
import com.wsoteam.diet.presentation.intro.IntroActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
@SuppressWarnings({"WeakerAccess", "unused"})
public abstract class ActivityBuilder {

    @ContributesAndroidInjector(modules = IntroModule.class)
    abstract IntroActivity bindIntroActivity();

}

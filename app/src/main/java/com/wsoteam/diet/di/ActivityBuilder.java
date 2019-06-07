package com.wsoteam.diet.di;

import com.wsoteam.diet.presentation.intro.IntroActivity;
import com.wsoteam.diet.presentation.profile.edit.EditProfileActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
@SuppressWarnings({"WeakerAccess", "unused"})
public abstract class ActivityBuilder {

    @ContributesAndroidInjector
    abstract IntroActivity bindIntroActivity();

    @PerActivity
    @ContributesAndroidInjector
    abstract EditProfileActivity bindEditProfileActivity();

}

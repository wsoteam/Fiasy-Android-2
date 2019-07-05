package com.wsoteam.diet.di;

import com.wsoteam.diet.EntryPoint.ActivitySplash;
import com.wsoteam.diet.presentation.auth.main.MainAuthActivity;
import com.wsoteam.diet.presentation.auth.restore.ActivityForgotPassword;
import com.wsoteam.diet.presentation.food.template.create.CreateFoodTemplateActivity;
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

    @ContributesAndroidInjector
    abstract MainAuthActivity bindMainAuthActivity();

    @ContributesAndroidInjector
    abstract ActivitySplash bindActivitySplash();

    @ContributesAndroidInjector
    abstract ActivityForgotPassword bindActivityForgotPassword();

    @ContributesAndroidInjector
    abstract CreateFoodTemplateActivity bindCreateFoodTemplateActivity();
}

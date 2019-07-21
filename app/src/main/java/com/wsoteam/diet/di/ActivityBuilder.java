package com.wsoteam.diet.di;

import com.wsoteam.diet.EntryPoint.ActivitySplash;
import com.wsoteam.diet.presentation.auth.main.MainAuthActivity;
import com.wsoteam.diet.presentation.auth.restore.ActivityForgotPassword;
import com.wsoteam.diet.presentation.intro.IntroActivity;
import com.wsoteam.diet.presentation.main.water.WaterActivity;
import com.wsoteam.diet.presentation.plans.detail.DetailPlansActivity;
import com.wsoteam.diet.presentation.plans.detail.blocked.BlockedDetailPlansActivity;
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
    abstract WaterActivity bindWaterActivity();

    @ContributesAndroidInjector
    abstract BlockedDetailPlansActivity bindBlockedDetailPlansActivity();

    @ContributesAndroidInjector
    abstract DetailPlansActivity bindDetailPlansActivity();
}

package com.wsoteam.diet.di;

import com.wsoteam.diet.presentation.profile.section.ProfileFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
@SuppressWarnings({"WeakerAccess", "unused"})
public abstract class FragmentBuilder {

    @ContributesAndroidInjector
    abstract ProfileFragment bindProfileFragment();
}

package com.wsoteam.diet.di;

import com.wsoteam.diet.presentation.profile.section.ProfileFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class FragmentBuilder {

    @ContributesAndroidInjector
    abstract ProfileFragment bindContactsFragment();
}

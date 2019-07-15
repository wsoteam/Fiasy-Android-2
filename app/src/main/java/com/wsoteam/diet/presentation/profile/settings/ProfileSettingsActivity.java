package com.wsoteam.diet.presentation.profile.settings;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.wsoteam.diet.R;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class ProfileSettingsActivity extends MvpAppCompatActivity implements ProfileSettingsView {
    @Inject
    @InjectPresenter
    ProfileSettingsPresenter profileSettingsPresenter;



    @ProvidePresenter
    ProfileSettingsPresenter providePresenter() {
        return profileSettingsPresenter;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings);

    }
}

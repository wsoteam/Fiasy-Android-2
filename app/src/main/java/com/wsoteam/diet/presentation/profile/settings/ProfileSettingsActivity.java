package com.wsoteam.diet.presentation.profile.settings;

import android.os.Bundle;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.wsoteam.diet.Config;
import com.wsoteam.diet.R;
import com.wsoteam.diet.common.Analytics.Events;
import com.wsoteam.diet.presentation.profile.settings.controller.ItemsAdapterKt;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ProfileSettingsActivity extends MvpAppCompatActivity implements ProfileSettingsView {
    ProfileSettingsPresenter profileSettingsPresenter;
    @BindView(R.id.rvSettingsItems) RecyclerView rvSettingsItems;
    @BindView(R.id.mlParent) MotionLayout mlParent;
    ImageButton  ibBack;


    @ProvidePresenter
    ProfileSettingsPresenter providePresenter() {
        return profileSettingsPresenter;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings);
        ButterKnife.bind(this);
        Events.logViewSettings();


        ibBack = findViewById(R.id.ibBack);

        profileSettingsPresenter = new ProfileSettingsPresenter();
        profileSettingsPresenter.attachView(this);
        rvSettingsItems.setLayoutManager(new LinearLayoutManager(this));
        rvSettingsItems.setAdapter(new ItemsAdapterKt(this, !getSharedPreferences(Config.STATE_BILLING, MODE_PRIVATE)
                .getBoolean(Config.STATE_BILLING, false)));

        ibBack.setOnClickListener(view -> onBackPressed());
    }
}

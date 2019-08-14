package com.wsoteam.diet.presentation.profile.settings;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.util.Log;
import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.wsoteam.diet.Config;
import com.wsoteam.diet.R;
import com.wsoteam.diet.presentation.profile.settings.controller.ItemsAdapter;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.AndroidInjection;

public class ProfileSettingsActivity extends MvpAppCompatActivity implements ProfileSettingsView {
    @Inject
    @InjectPresenter
    ProfileSettingsPresenter profileSettingsPresenter;
    @BindView(R.id.rvSettingsItems) RecyclerView rvSettingsItems;


    @ProvidePresenter
    ProfileSettingsPresenter providePresenter() {
        return profileSettingsPresenter;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings);
        ButterKnife.bind(this);
        rvSettingsItems.setLayoutManager(new LinearLayoutManager(this));
        rvSettingsItems.setAdapter(new ItemsAdapter(this, !getSharedPreferences(Config.STATE_BILLING, MODE_PRIVATE).getBoolean(Config.STATE_BILLING, false)));
    }

    @OnClick(R.id.ibBack)
    public void onViewClicked() {
        onBackPressed();
    }
}

package com.wsoteam.diet.presentation.profile.settings;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.wsoteam.diet.Config;
import com.wsoteam.diet.R;
import com.wsoteam.diet.presentation.profile.settings.controller.ItemsAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProfileSettingsActivity extends MvpAppCompatActivity implements ProfileSettingsView {
    ProfileSettingsPresenter profileSettingsPresenter;
    @BindView(R.id.rvSettingsItems) RecyclerView rvSettingsItems;
    @BindView(R.id.cvPremium) CardView cvPremium;


    @ProvidePresenter
    ProfileSettingsPresenter providePresenter() {
        return profileSettingsPresenter;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings);
        ButterKnife.bind(this);

        profileSettingsPresenter = new ProfileSettingsPresenter();
        profileSettingsPresenter.attachView(this);

        rvSettingsItems.setLayoutManager(new LinearLayoutManager(this));
        rvSettingsItems.setAdapter(new ItemsAdapter(this, !getSharedPreferences(Config.STATE_BILLING, MODE_PRIVATE)
                .getBoolean(Config.STATE_BILLING, false)));
        cvPremium.setBackgroundResource(R.drawable.shape_prem_settings);
    }

    @OnClick(R.id.ibBack)
    public void onViewClicked() {
        onBackPressed();
    }
}

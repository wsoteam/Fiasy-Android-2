package com.wsoteam.diet.presentation.profile.settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.wsoteam.diet.AmplitudaEvents;
import com.wsoteam.diet.Authenticate.POJO.Box;
import com.wsoteam.diet.Config;
import com.wsoteam.diet.InApp.ActivitySubscription;
import com.wsoteam.diet.R;
import com.wsoteam.diet.common.Analytics.EventProperties;
import com.wsoteam.diet.common.settings.PremiumCloseStateSingleton;
import com.wsoteam.diet.common.Analytics.Events;
import com.wsoteam.diet.presentation.profile.settings.controller.ItemsAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ProfileSettingsActivity extends MvpAppCompatActivity implements ProfileSettingsView {
    ProfileSettingsPresenter profileSettingsPresenter;
    @BindView(R.id.rvSettingsItems) RecyclerView rvSettingsItems;
    CardView cvPremium;
    @BindView(R.id.mlParent) MotionLayout mlParent;
    ImageButton ibPremClose, ibBack;
    Button btnSettingsPrem;


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

        cvPremium = findViewById(R.id.cvPremium);
        ibPremClose = findViewById(R.id.ibSettingsPremClose);
        ibBack = findViewById(R.id.ibBack);
        btnSettingsPrem = findViewById(R.id.btnSettingsPrem);
        profileSettingsPresenter = new ProfileSettingsPresenter();
        profileSettingsPresenter.attachView(this);
        rvSettingsItems.setLayoutManager(new LinearLayoutManager(this));
        rvSettingsItems.setAdapter(new ItemsAdapter(this, !getSharedPreferences(Config.STATE_BILLING, MODE_PRIVATE)
                .getBoolean(Config.STATE_BILLING, false)));
        cvPremium.setBackgroundResource(R.drawable.shape_prem_settings);

        if (getSharedPreferences(Config.STATE_BILLING, MODE_PRIVATE)
                .getBoolean(Config.STATE_BILLING, false) || PremiumCloseStateSingleton.getInstance().isClosePremium()) {
            cvPremium.setVisibility(View.GONE);
        }
        ibPremClose.setOnTouchListener((view, motionEvent) -> {
            PremiumCloseStateSingleton.getInstance().setClosePremium(true);
            return false;
        });
        ibBack.setOnClickListener(view -> onBackPressed());
        btnSettingsPrem.setOnClickListener(view -> {
            Intent intent = new Intent(ProfileSettingsActivity.this, ActivitySubscription.class);
            Box box = new Box();
            box.setComeFrom(AmplitudaEvents.view_prem_settings);
            box.setBuyFrom(EventProperties.trial_from_settings);
            box.setOpenFromPremPart(true);
            box.setOpenFromIntrodaction(false);
            intent.putExtra(Config.TAG_BOX, box);
            startActivity(intent);
        });
    }
}

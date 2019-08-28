package com.wsoteam.diet.presentation.profile.settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

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
import com.wsoteam.diet.presentation.profile.settings.controller.ItemsAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ProfileSettingsActivity extends MvpAppCompatActivity implements ProfileSettingsView {
    ProfileSettingsPresenter profileSettingsPresenter;
    @BindView(R.id.rvSettingsItems) RecyclerView rvSettingsItems;
    CardView cvPremium;
    @BindView(R.id.mlParent) MotionLayout mlParent;


    @ProvidePresenter
    ProfileSettingsPresenter providePresenter() {
        return profileSettingsPresenter;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings);
        ButterKnife.bind(this);

        cvPremium = findViewById(R.id.cvPremium);
        //cvPremium.setVisibility(VISIBLE);
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
    }



    @OnClick({R.id.btnSettingsPrem, R.id.ibSettingsPremClose, R.id.ibBack})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnSettingsPrem:
                Intent intent = new Intent(this, ActivitySubscription.class);
                Box box = new Box();
                box.setComeFrom(AmplitudaEvents.view_prem_settings);
                box.setBuyFrom(EventProperties.trial_from_settings);
                box.setOpenFromPremPart(true);
                box.setOpenFromIntrodaction(false);
                intent.putExtra(Config.TAG_BOX, box);
                startActivity(intent);
                break;
            case R.id.ibSettingsPremClose:
                cvPremium.setVisibility(View.GONE);
                PremiumCloseStateSingleton.getInstance().setClosePremium(true);
                break;
            case R.id.ibBack:
                //onBackPressed();
                cvPremium.setVisibility(View.VISIBLE);
                break;
        }
    }
}

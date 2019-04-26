package com.wsoteam.diet.InApp;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.adjust.sdk.Adjust;
import com.adjust.sdk.AdjustEvent;
import com.amplitude.api.Amplitude;
import com.wsoteam.diet.ABConfig;
import com.wsoteam.diet.AmplitudaEvents;
import com.wsoteam.diet.Config;
import com.wsoteam.diet.EventsAdjust;
import com.wsoteam.diet.InApp.Fragments.FragmentSubscription;
import com.wsoteam.diet.InApp.Fragments.FragmentSubscriptionGreen;
import com.wsoteam.diet.InApp.Fragments.FragmentSubscriptionWhite;
import com.wsoteam.diet.R;


public class ActivitySubscription extends AppCompatActivity {
    private String ABVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription);

        ABVersion = getSharedPreferences(ABConfig.KEY_FOR_SAVE_STATE, MODE_PRIVATE).
                getString(ABConfig.KEY_FOR_SAVE_STATE, ABConfig.A_VERSION);


        if (ABVersion.equals(ABConfig.A_VERSION)) {
            getSupportFragmentManager().beginTransaction().add(R.id.clContainer,
                    FragmentSubscriptionWhite.newInstance(getIntent().getBooleanExtra(Config.ENTER_FROM_MAIN_ACTIVITY, false),
                            getIntent().getStringExtra(Config.AMPLITUDE_COME_FROM), getIntent().getStringExtra(Config.ADJUST_COME_FROM),
                            getIntent().getStringExtra(Config.AMPLITUDE_BUY_FROM),
                            getIntent().getStringExtra(Config.ADJUST_BUY_FROM),
                            getIntent().getBooleanExtra(Config.OPEN_PREM_FROM_INTRODACTION, false))).commit();
        } else {
            getSupportFragmentManager().beginTransaction().add(R.id.clContainer,
                    FragmentSubscriptionGreen.newInstance(getIntent().getBooleanExtra(Config.ENTER_FROM_MAIN_ACTIVITY, false),
                            getIntent().getStringExtra(Config.AMPLITUDE_COME_FROM), getIntent().getStringExtra(Config.ADJUST_COME_FROM),
                            getIntent().getStringExtra(Config.AMPLITUDE_BUY_FROM),
                            getIntent().getStringExtra(Config.ADJUST_BUY_FROM),
                            getIntent().getBooleanExtra(Config.OPEN_PREM_FROM_INTRODACTION, false))).commit();
        }
    }
}

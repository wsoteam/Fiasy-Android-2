package com.wsoteam.diet.InApp;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.adjust.sdk.Adjust;
import com.adjust.sdk.AdjustEvent;
import com.amplitude.api.Amplitude;
import com.wsoteam.diet.AmplitudaEvents;
import com.wsoteam.diet.Config;
import com.wsoteam.diet.EventsAdjust;
import com.wsoteam.diet.InApp.Fragments.FragmentSubscription;
import com.wsoteam.diet.R;


public class ActivitySubscription extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription);

        if (getIntent().getStringExtra(Config.START_FROM).equals(Config.FROM_ONBOARDING)){
            Amplitude.getInstance().logEvent(AmplitudaEvents.buy_prem_onboarding);
            Adjust.trackEvent(new AdjustEvent(EventsAdjust.buy_prem_onboarding));
            Log.d("event", "onCreate: buy_prem_onboarding" );
        }

        getSupportFragmentManager().beginTransaction().add(R.id.clContainer,
                FragmentSubscription.newInstance(getIntent().getBooleanExtra(Config.ENTER_FROM_MAIN_ACTIVITY, false),
                        getIntent().getStringExtra(Config.AMPLITUDE_COME_FROM), getIntent().getStringExtra(Config.ADJUST_COME_FROM),
                        getIntent().getStringExtra(Config.AMPLITUDE_BUY_FROM),
                        getIntent().getStringExtra(Config.ADJUST_BUY_FROM))).commit();
    }
}

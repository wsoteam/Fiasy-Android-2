package com.wsoteam.diet.InApp;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.wsoteam.diet.Config;
import com.wsoteam.diet.InApp.Fragments.FragmentSubscriptionGreen;
import com.wsoteam.diet.R;


public class ActivitySubscription extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription);
        getSupportFragmentManager().beginTransaction().add(R.id.clContainer,
                FragmentSubscriptionGreen.newInstance(getIntent().getBooleanExtra(Config.ENTER_FROM_MAIN_ACTIVITY, false),
                        getIntent().getStringExtra(Config.AMPLITUDE_COME_FROM), getIntent().getStringExtra(Config.ADJUST_COME_FROM),
                        getIntent().getStringExtra(Config.AMPLITUDE_BUY_FROM),
                        getIntent().getStringExtra(Config.ADJUST_BUY_FROM),
                        getIntent().getBooleanExtra(Config.OPEN_PREM_FROM_INTRODACTION, false))).commit();

    }
}

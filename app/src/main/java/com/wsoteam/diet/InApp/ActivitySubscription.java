package com.wsoteam.diet.InApp;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;

import com.wsoteam.diet.ABConfig;
import com.wsoteam.diet.Authenticate.POJO.Box;
import com.wsoteam.diet.Config;
import com.wsoteam.diet.InApp.Fragments.FragmentSubscriptionGreen;
import com.wsoteam.diet.InApp.Fragments.FragmentSubscriptionGreenOneButton;
import com.wsoteam.diet.InApp.Fragments.FragmentSubscriptionGreenUA;
import com.wsoteam.diet.R;


public class ActivitySubscription extends AppCompatActivity {
    private String abVersion;
    private Box box;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription);
        abVersion = getSharedPreferences(ABConfig.KEY_FOR_SAVE_STATE, MODE_PRIVATE).
                getString(ABConfig.KEY_FOR_SAVE_STATE, "default");
        box = (Box) getIntent().getSerializableExtra(Config.TAG_BOX);

        if (abVersion.equals(ABConfig.A_VERSION)) {
            getSupportFragmentManager().beginTransaction().add(R.id.clContainer,
                    FragmentSubscriptionGreen.newInstance(box)).commit();
        } else {
            if (abVersion.equals(ABConfig.C_VERSION)) {
                getSupportFragmentManager().beginTransaction().add(R.id.clContainer,
                        FragmentSubscriptionGreenUA.newInstance(box)).commit();
            } else {
                getSupportFragmentManager().beginTransaction().add(R.id.clContainer,
                        FragmentSubscriptionGreenOneButton.newInstance(box)).commit();
            }
        }


    }
}

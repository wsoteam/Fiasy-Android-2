package com.wsoteam.diet.presentation.profile.norm.choise.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.wsoteam.diet.R;
import com.wsoteam.diet.presentation.profile.norm.Config;

public class ActivActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activ);
        getSupportFragmentManager().beginTransaction().replace(R.id.clContainer, ActivFragment.newInstance(getIntent().getStringExtra(Config.ACTIVITY))).commit();
    }
}

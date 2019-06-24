package com.wsoteam.diet.presentation.main.water;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.SeekBar;

import com.wsoteam.diet.R;
import com.wsoteam.diet.presentation.global.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.AndroidInjection;

public class WaterActivity extends BaseActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.pbWater) SeekBar pbWater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water);
        ButterKnife.bind(this);

        toolbar.setTitle(R.string.water_screen_toolbar_title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @OnClick({R.id.btnDefault})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnDefault:
                pbWater.setProgress(0);
                break;
        }
    }

}

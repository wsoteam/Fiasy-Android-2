package com.wsoteam.diet.presentation.main.water;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.wsoteam.diet.R;
import com.wsoteam.diet.presentation.global.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.AndroidInjection;

public class WaterActivity extends BaseActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.pbWater) SeekBar pbWater;
    @BindView(R.id.tvWater) TextView tvWater;
    @BindView(R.id.rbGlass) RadioButton rbGlass;
    @BindView(R.id.rbBottle) RadioButton rbBottle;

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

        pbWater.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvWater.setText(((float) progress / 10 + 1) + " л");
                double percent = progress / (double) seekBar.getMax();
                int offset = seekBar.getThumbOffset();
                int val = (int) Math.round(percent * (seekBar.getWidth() - 2 * offset));
                tvWater.setX(offset + seekBar.getX() + val - Math.round(percent * offset) - Math.round(percent * tvWater.getWidth() / 3));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        pbWater.setMax(30);
        pbWater.setProgress(1);
        pbWater.setProgress(0);
    }

    @OnClick({R.id.btnDefault, R.id.rbGlass, R.id.rbBottle})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnDefault:
                pbWater.setMax(30);
                pbWater.setProgress(1);
                pbWater.setProgress(0);
                break;
            case R.id.rbGlass:
                rbBottle.setChecked(false);
                break;
            case R.id.rbBottle:
                rbGlass.setChecked(false);
                break;
        }
    }

}

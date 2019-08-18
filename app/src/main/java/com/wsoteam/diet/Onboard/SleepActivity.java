package com.wsoteam.diet.Onboard;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import androidx.appcompat.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.amplitude.api.Amplitude;
import com.wsoteam.diet.AmplitudaEvents;
import com.wsoteam.diet.Config;
import com.wsoteam.diet.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SleepActivity extends AppCompatActivity {

    @BindView(R.id.ivLoadingCircle) ImageView ivLoadingCircle;
    private Animation animationRotate;
    private final int TIME_SLEEP = 7000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep);
        ButterKnife.bind(this);

        animationRotate = AnimationUtils.loadAnimation(this, R.anim.animation_rotate);
        ivLoadingCircle.startAnimation(animationRotate);

        new CountDownTimer(TIME_SLEEP, 100){
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                startActivity(new Intent(SleepActivity.this, TotalAmountActivity.class).
                        putExtra(Config.INTENT_PROFILE, getIntent().getSerializableExtra(Config.INTENT_PROFILE)));
                finish();
            }
        }.start();


    }
}

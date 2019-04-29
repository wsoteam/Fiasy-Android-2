package com.wsoteam.diet.Onboard;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.wsoteam.diet.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SleepActivity extends AppCompatActivity {

    @BindView(R.id.ivLoadingCircle) ImageView ivLoadingCircle;
    private Animation animationRotate;
    private final int TIME_SLEEP = 3000;

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
                startActivity(new Intent(SleepActivity.this, TotalAmountActivity.class));
                finish();
            }
        }.start();


    }
}

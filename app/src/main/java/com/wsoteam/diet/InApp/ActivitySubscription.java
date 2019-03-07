package com.wsoteam.diet.InApp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.wsoteam.diet.MainScreen.MainActivity;
import com.wsoteam.diet.R;

public class ActivitySubscription extends AppCompatActivity implements View.OnClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription);

        findViewById(R.id.subsc_btn).setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }


    @Override
    public void onClick(View view) {

    }
}

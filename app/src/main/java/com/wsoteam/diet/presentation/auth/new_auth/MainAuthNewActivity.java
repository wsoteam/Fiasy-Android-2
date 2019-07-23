package com.wsoteam.diet.presentation.auth.new_auth;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.wsoteam.diet.R;
import com.wsoteam.diet.presentation.auth.new_auth.fragments.AuthFirstFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainAuthNewActivity extends AppCompatActivity {

    @BindView(R.id.container)
    FrameLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_auth_new);
        ButterKnife.bind(this);

        Fragment newFragment = new AuthFirstFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, newFragment).commit();
    }
}

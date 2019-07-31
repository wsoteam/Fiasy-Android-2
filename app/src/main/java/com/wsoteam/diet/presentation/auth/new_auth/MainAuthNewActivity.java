package com.wsoteam.diet.presentation.auth.new_auth;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.wsoteam.diet.R;
import com.wsoteam.diet.presentation.auth.new_auth.fragments.AuthFirstFragment;

public class MainAuthNewActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main_auth_new);

    getSupportFragmentManager()
        .beginTransaction()
        .add(R.id.container, new AuthFirstFragment())
        .commit();
  }
}

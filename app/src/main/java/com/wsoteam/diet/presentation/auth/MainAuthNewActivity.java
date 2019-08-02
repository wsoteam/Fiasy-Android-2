package com.wsoteam.diet.presentation.auth;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.wsoteam.diet.R;

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

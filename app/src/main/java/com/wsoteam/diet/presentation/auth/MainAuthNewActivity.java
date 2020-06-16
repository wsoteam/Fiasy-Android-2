package com.wsoteam.diet.presentation.auth;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.wsoteam.diet.Config;
import com.wsoteam.diet.POJOProfile.Profile;
import com.wsoteam.diet.R;
import com.wsoteam.diet.utils.DynamicUnitUtils;

public class MainAuthNewActivity extends AppCompatActivity implements InternetBad {


  public static Intent getIntent(Context context){
    return new Intent(context, MainAuthNewActivity.class).putExtra(Config.CREATE_PROFILE, true)
            .putExtra(Config.INTENT_PROFILE, new Profile());
  }

  private View internetBad;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main_auth_new);

    internetBad = findViewById(R.id.internetBad);

    getSupportFragmentManager()
        .beginTransaction()
        .add(R.id.container, new AuthFirstFragment())
        .commit();
  }

  @Override
  public void show() {

    long delay = 2000;

    ObjectAnimator animation = ObjectAnimator.ofFloat(internetBad, "translationY", DynamicUnitUtils.convertDpToPixels(0));
    animation.setDuration(400);
    animation.start();
    new Handler().postDelayed(this::hide, delay);
  }


  private void hide() {
    ObjectAnimator animation = ObjectAnimator.ofFloat(internetBad, "translationY", DynamicUnitUtils.convertDpToPixels(-100));
    animation.setDuration(400);
    animation.start();
  }

}

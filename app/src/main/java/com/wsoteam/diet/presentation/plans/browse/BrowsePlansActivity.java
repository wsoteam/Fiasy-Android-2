package com.wsoteam.diet.presentation.plans.browse;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.wsoteam.diet.R;

public class BrowsePlansActivity extends AppCompatActivity {

  FragmentTransaction transaction;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_browse_plans);

    transaction = getSupportFragmentManager().beginTransaction();
    transaction.add(R.id.fragmentContainer, new BrowsePlansFragment()).commit();
  }
}

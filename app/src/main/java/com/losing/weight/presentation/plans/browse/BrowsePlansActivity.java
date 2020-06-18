package com.losing.weight.presentation.plans.browse;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.losing.weight.R;

public class BrowsePlansActivity extends AppCompatActivity {

  FragmentTransaction transaction;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_browse_plans);

    final Bundle args = new Bundle();
    args.putBoolean("addBackButton", true);

    final Fragment f = new BrowsePlansFragment();
    f.setArguments(args);

    transaction = getSupportFragmentManager().beginTransaction();
    transaction.add(R.id.fragmentContainer, f).commit();
  }
}

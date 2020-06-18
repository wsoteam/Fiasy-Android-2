package com.losing.weight.InApp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.losing.weight.ABConfig;
import com.losing.weight.Authenticate.POJO.Box;
import com.losing.weight.Config;
import com.losing.weight.InApp.Fragments.FragmentG;
import com.losing.weight.R;

public class ActivitySubscription extends AppCompatActivity {
  private String abVersion;
  private Box box;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_subscription);


    abVersion = getSharedPreferences(ABConfig.KEY_FOR_SAVE_STATE, MODE_PRIVATE).
        getString(ABConfig.KEY_FOR_SAVE_STATE, "default");
    box = (Box) getIntent().getSerializableExtra(Config.TAG_BOX);

    getSupportFragmentManager().beginTransaction().add(R.id.clContainer,
            FragmentG.newInstance(box)).commit();

//    switch (abVersion) {
//      default:
//      case ABConfig.A:
//        getSupportFragmentManager().beginTransaction().add(R.id.clContainer,
//            FragmentA.newInstance(box)).commit();
//        break;
//      case ABConfig.B:
//        getSupportFragmentManager().beginTransaction().add(R.id.clContainer,
//            FragmentB.newInstance(box)).commit();
//        break;
//      case ABConfig.C:
//        getSupportFragmentManager().beginTransaction().add(R.id.clContainer,
//            FragmentC.newInstance(box)).commit();
//        break;
//      case ABConfig.D:
//        getSupportFragmentManager().beginTransaction().add(R.id.clContainer,
//            FragmentD.newInstance(box)).commit();
//        break;
//      case ABConfig.E:
//        getSupportFragmentManager().beginTransaction().add(R.id.clContainer,
//            FragmentE.newInstance(box)).commit();
//        break;
//      case ABConfig.F:
//        getSupportFragmentManager().beginTransaction().add(R.id.clContainer,
//            FragmentF.newInstance(box)).commit();
//        break;
//      case ABConfig.G:
//        getSupportFragmentManager().beginTransaction().add(R.id.clContainer,
//            FragmentG.newInstance(box)).commit();
//        break;
//      case ABConfig.H:
//        getSupportFragmentManager().beginTransaction().add(R.id.clContainer,
//            FragmentH.newInstance(box)).commit();
//        break;
//      case ABConfig.I:
//        getSupportFragmentManager().beginTransaction().add(R.id.clContainer,
//                FragmentI.newInstance(box)).commit();
//        break;
//    }

  }

  @Override public void onBackPressed() {
    if (isStopVersion()) {
      finishAffinity();
    } else {
      super.onBackPressed();
    }
  }

  private boolean isStopVersion() {
    String version = getSharedPreferences(ABConfig.KEY_FOR_SAVE_STATE, MODE_PRIVATE).
        getString(ABConfig.KEY_FOR_SAVE_STATE, "default");
    return version.equals(ABConfig.C) // 3
        || version.equals(ABConfig.D) // 4
        || version.equals(ABConfig.F) // 6
        || version.equals(ABConfig.H); // 8
  }
}


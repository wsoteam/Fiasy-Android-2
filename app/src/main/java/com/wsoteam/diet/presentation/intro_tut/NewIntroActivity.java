package com.wsoteam.diet.presentation.intro_tut;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.google.android.material.tabs.TabLayout;
import com.wsoteam.diet.AmplitudaEvents;
import com.wsoteam.diet.Authenticate.POJO.Box;
import com.wsoteam.diet.Config;
import com.wsoteam.diet.POJOProfile.Profile;
import com.wsoteam.diet.R;
import com.wsoteam.diet.common.Analytics.Events;
import com.wsoteam.diet.presentation.auth.MainAuthNewActivity;

public class NewIntroActivity extends AppCompatActivity {

  private final static String KEY_ONBOARD_SHOWN = "app:on_board_has_been_shown";

  @BindView(R.id.pager)
  ViewPager viewPager;
  @BindView(R.id.tabDots)
  TabLayout tabLayout;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_new_intro);
    ButterKnife.bind(this);

    final SharedPreferences pm = PreferenceManager.getDefaultSharedPreferences(this);

    if (!pm.getBoolean(KEY_ONBOARD_SHOWN, false)) {
      pm.edit().putBoolean(KEY_ONBOARD_SHOWN, true).apply();

      viewPager.setAdapter(new IntroSlidesAdapter(getSupportFragmentManager()));
      tabLayout.setupWithViewPager(viewPager, true);
    } else {
      final Box box = new Box();
      box.setBuyFrom(AmplitudaEvents.buy_prem_onboarding);
      box.setComeFrom(AmplitudaEvents.view_prem_free_onboard);
      box.setOpenFromIntrodaction(true);
      box.setOpenFromPremPart(false);

      final Intent intent = new Intent(this, MainAuthNewActivity.class);
      intent.putExtra(Config.CREATE_PROFILE, true)
          .putExtra(Config.INTENT_PROFILE, new Profile());

      startActivity(intent);
      finish();
    }
  }

  @OnClick({ R.id.btnNext, R.id.btnSkip })
  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.btnNext:
        if (viewPager.getCurrentItem() <= viewPager.getChildCount() - 1) {
          viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
          Events.logMoveOnboard(viewPager.getCurrentItem() + 1);
        } else
        //startActivity(new Intent(this, QuestionsActivity.class).putExtra(Config.CREATE_PROFILE, true));
        {
          Intent intent = new Intent(this, MainAuthNewActivity.class);
          Box box = new Box();
          box.setBuyFrom(AmplitudaEvents.buy_prem_onboarding);
          box.setComeFrom(AmplitudaEvents.view_prem_free_onboard);
          box.setOpenFromIntrodaction(true);
          box.setOpenFromPremPart(false);
          intent.putExtra(Config.CREATE_PROFILE, true)
              .putExtra(Config.INTENT_PROFILE, new Profile());
          startActivity(intent);
          finish();
        }
        break;
      case R.id.btnSkip:
        //startActivity(new Intent(this, QuestionsActivity.class).putExtra(Config.CREATE_PROFILE, true));
      {
        Events.logSkipOnboard(viewPager.getCurrentItem() + 1);
        Intent intent = new Intent(this, MainAuthNewActivity.class);
        Box box = new Box();
        box.setBuyFrom(AmplitudaEvents.buy_prem_onboarding);
        box.setComeFrom(AmplitudaEvents.view_prem_free_onboard);
        box.setOpenFromIntrodaction(true);
        box.setOpenFromPremPart(false);
        intent.putExtra(Config.CREATE_PROFILE, true)
            .putExtra(Config.INTENT_PROFILE, new Profile());
        startActivity(intent);
        finish();
      }
      break;
    }
  }
}

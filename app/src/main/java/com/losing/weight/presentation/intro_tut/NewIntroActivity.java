package com.losing.weight.presentation.intro_tut;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.google.android.material.tabs.TabLayout;
import com.losing.weight.AmplitudaEvents;
import com.losing.weight.Authenticate.POJO.Box;
import com.losing.weight.Config;
import com.losing.weight.POJOProfile.Profile;
import com.losing.weight.R;
import com.losing.weight.common.Analytics.EventProperties;
import com.losing.weight.common.Analytics.Events;
import com.losing.weight.presentation.auth.MainAuthNewActivity;
import com.losing.weight.views.CustomizedViewPager;

public class NewIntroActivity extends AppCompatActivity {

  private final static String KEY_ONBOARD_SHOWN = "app:on_board_has_been_shown";

  @BindView(R.id.pager)
  CustomizedViewPager viewPager;
  @BindView(R.id.tabDots)
  TabLayout tabLayout;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_new_intro);
    ButterKnife.bind(this);


    final SharedPreferences pm = PreferenceManager.getDefaultSharedPreferences(this);

    if (!pm.getBoolean(KEY_ONBOARD_SHOWN, false)) {
      viewPager.setHandleTouchEvents(true);
      viewPager.setAdapter(new IntroSlidesAdapter(getSupportFragmentManager()));
      tabLayout.setupWithViewPager(viewPager, true);
      Events.logMoveOnboard(viewPager.getCurrentItem() + 1);
    } else {
      final Box box = new Box();
      box.setBuyFrom(EventProperties.trial_from_onboard);
      box.setComeFrom(AmplitudaEvents.view_prem_free_onboard);
      box.setOpenFromIntrodaction(true);
      box.setOpenFromPremPart(false);

      final Intent intent = new Intent(this, MainAuthNewActivity.class);
      intent.putExtra(Config.CREATE_PROFILE, true)
          .putExtra(Config.INTENT_PROFILE, new Profile());
      Events.logMoveOnboard(EventProperties.go_onboard_reg);
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
          Events.logMoveOnboard(EventProperties.go_onboard_reg);
          finishSlides();
        }
        break;
      case R.id.btnSkip:
        //startActivity(new Intent(this, QuestionsActivity.class).putExtra(Config.CREATE_PROFILE, true));
      {
        Events.logSkipOnboard(viewPager.getCurrentItem() + 1);
        finishSlides();
      }
      break;
    }
  }

  protected void finishSlides(){
    PreferenceManager.getDefaultSharedPreferences(this)
        .edit()
        .putBoolean(KEY_ONBOARD_SHOWN, true)
        .apply();

    Intent intent = new Intent(this, MainAuthNewActivity.class);
    Box box = new Box();
    box.setBuyFrom(EventProperties.trial_from_onboard);
    box.setComeFrom(AmplitudaEvents.view_prem_free_onboard);
    box.setOpenFromIntrodaction(true);
    box.setOpenFromPremPart(false);
    intent.putExtra(Config.CREATE_PROFILE, true)
        .putExtra(Config.INTENT_PROFILE, new Profile());
    startActivity(intent);
    finish();
  }

}

package com.wsoteam.diet.presentation.profile.questions;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import com.wsoteam.diet.Authenticate.POJO.Box;
import com.wsoteam.diet.Config;
import com.wsoteam.diet.InApp.Fragments.FragmentSubscriptionOrangeOneButton;
import com.wsoteam.diet.common.Analytics.EventProperties;
import com.wsoteam.diet.common.Analytics.Events;
import com.wsoteam.diet.presentation.profile.questions.fragments.IndividualPlanFragments;
import com.wsoteam.diet.presentation.profile.questions.fragments.WeOfferFragments;

public class AfterQuestionsPagerAdapter extends FragmentPagerAdapter {

  private static int NUM_ITEMS = 3;

  public AfterQuestionsPagerAdapter(FragmentManager fm) {
    super(fm);
  }

  @Override public Fragment getItem(int position) {
    switch (position) {
      case 0:
        return IndividualPlanFragments.newInstance();
      case 1:
        return WeOfferFragments.newInstance();
      case 2:
        Box box = new Box();
        box.setOpenFromIntrodaction(true);
        Events.logMoveQuestions(EventProperties.question_premium);
        return FragmentSubscriptionOrangeOneButton.newInstance(box);
      default:
        return null;
    }
  }

  @Override public int getCount() {
    return NUM_ITEMS;
  }

  @Override
  public CharSequence getPageTitle(int position) {
    return "";
  }
}

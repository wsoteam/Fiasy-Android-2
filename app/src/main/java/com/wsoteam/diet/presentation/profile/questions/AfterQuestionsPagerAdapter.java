package com.wsoteam.diet.presentation.profile.questions;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import com.wsoteam.diet.presentation.profile.questions.fragments.IndividualPlanFragments;
import com.wsoteam.diet.presentation.profile.questions.fragments.WeOfferFragments;

public class AfterQuestionsPagerAdapter extends FragmentPagerAdapter {

  private static int NUM_ITEMS = 2;

  public AfterQuestionsPagerAdapter(FragmentManager fm) {
    super(fm);
  }

  @Override public Fragment getItem(int position) {
    switch (position) {
      case 0:
        return IndividualPlanFragments.newInstance();
      case 1:
        return WeOfferFragments.newInstance();
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

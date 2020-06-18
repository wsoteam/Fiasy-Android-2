package com.losing.weight.presentation.profile.questions;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import com.losing.weight.Authenticate.POJO.Box;
import com.losing.weight.InApp.Fragments.FragmentA;
import com.losing.weight.common.Analytics.EventProperties;
import com.losing.weight.presentation.profile.questions.fragments.IndividualPlanFragments;

public class AfterQuestionsPagerAdapter extends FragmentPagerAdapter {

  private static int NUM_ITEMS = 1;

  public AfterQuestionsPagerAdapter(FragmentManager fm) {
    super(fm);
  }

  @Override public Fragment getItem(int position) {
    switch (position) {
      case 0:
        return IndividualPlanFragments.newInstance();
      case 1:
        //return WeOfferFragments.newInstance();
        Box box = new Box();
        box.setOpenFromIntrodaction(true);
        box.setBuyFrom(EventProperties.trial_from_onboard);
        return FragmentA.newInstance(box);
      //case 2:

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

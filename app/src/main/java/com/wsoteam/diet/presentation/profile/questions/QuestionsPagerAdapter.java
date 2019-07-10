package com.wsoteam.diet.presentation.profile.questions;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.wsoteam.diet.presentation.profile.questions.fragments.QuestionPurposeFragments;
import com.wsoteam.diet.presentation.profile.questions.fragments.QuestionSexFragments;

public class QuestionsPagerAdapter extends FragmentPagerAdapter {
    private static int NUM_ITEMS = 2;

    public QuestionsPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    // Returns total number of pages
    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    // Returns the fragment to display for that page
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return QuestionSexFragments.newInstance(0, "Page # 1");
            case 1:
                return QuestionPurposeFragments.newInstance(0, "Page # 1");
            default:
                return null;
        }
    }

    // Returns the page title for the top indicator
    @Override
    public CharSequence getPageTitle(int position) {
        return "";
    }

}
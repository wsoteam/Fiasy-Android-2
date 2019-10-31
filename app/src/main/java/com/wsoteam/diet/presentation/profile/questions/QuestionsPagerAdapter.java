package com.wsoteam.diet.presentation.profile.questions;

import
    androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import com.wsoteam.diet.presentation.profile.questions.fragments.QuestionActivityFragments;
import com.wsoteam.diet.presentation.profile.questions.fragments.QuestionBirthdayFragments;
import com.wsoteam.diet.presentation.profile.questions.fragments.QuestionHeightFragments;
import com.wsoteam.diet.presentation.profile.questions.fragments.QuestionNameFragments;
import com.wsoteam.diet.presentation.profile.questions.fragments.QuestionPurposeFragments;
import com.wsoteam.diet.presentation.profile.questions.fragments.QuestionSexFragments;
import com.wsoteam.diet.presentation.profile.questions.fragments.QuestionWeightFragments;

public class QuestionsPagerAdapter extends FragmentPagerAdapter {
    private static int NUM_ITEMS = 7;

    public QuestionsPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return QuestionNameFragments.newInstance();
            case 1:
                return QuestionSexFragments.newInstance();
            case 2:
                return QuestionHeightFragments.newInstance();
            case 3:
                return QuestionWeightFragments.newInstance();
            case 4:
                return QuestionBirthdayFragments.newInstance();
            case 5:
                return QuestionActivityFragments.newInstance();
            case 6:
                return QuestionPurposeFragments.newInstance();
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "";
    }

}
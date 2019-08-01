package com.wsoteam.diet.presentation.intro_tut;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.wsoteam.diet.presentation.intro_tut.fragments.IntroOneFragments;
import com.wsoteam.diet.presentation.intro_tut.fragments.IntroThreeFragments;
import com.wsoteam.diet.presentation.intro_tut.fragments.IntroTwoFragments;

public class IntroPagerAdapter extends FragmentPagerAdapter {
    private static int NUM_ITEMS = 3;

    public IntroPagerAdapter(FragmentManager fragmentManager) {
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
                return new IntroOneFragments();
            case 1:
                return new IntroTwoFragments();
            case 2:
                return new IntroThreeFragments();
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "";
    }

}
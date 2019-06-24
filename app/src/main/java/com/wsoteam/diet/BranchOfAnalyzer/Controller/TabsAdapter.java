package com.wsoteam.diet.BranchOfAnalyzer.Controller;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.wsoteam.diet.BranchOfAnalyzer.Fragments.FragmentFavorites;
import com.wsoteam.diet.BranchOfAnalyzer.Fragments.FragmentSearch;

import java.util.ArrayList;
import java.util.List;

public class TabsAdapter extends FragmentPagerAdapter {
    List<Fragment> fragments;

    public TabsAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int i) {
        return fragments.get(i);

    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}

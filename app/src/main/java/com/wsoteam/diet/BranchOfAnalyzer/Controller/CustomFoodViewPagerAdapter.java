package com.wsoteam.diet.BranchOfAnalyzer.Controller;

import android.util.Log;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.wsoteam.diet.BranchOfAnalyzer.TabsFragment;

import java.util.List;

public class CustomFoodViewPagerAdapter extends FragmentPagerAdapter {
    List<Fragment> fragments;

    public CustomFoodViewPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
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
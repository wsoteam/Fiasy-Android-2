package com.wsoteam.diet.Authenticate.Onboarding.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;

public class FragmentSlide extends Fragment {
    private static final String TAG = "FragmentSlide";

    public static FragmentSlide newInstance(int position) {
        Bundle bundle = new Bundle();
        bundle.putInt(TAG, position);
        FragmentSlide fragmentSlide = new FragmentSlide();
        fragmentSlide.setArguments(bundle);
        return fragmentSlide;
    }
}

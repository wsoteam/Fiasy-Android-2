package com.wsoteam.diet.presentation.intro_tut.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wsoteam.diet.R;

import butterknife.ButterKnife;

public class IntroTwoFragments extends Fragment {


    public static IntroTwoFragments newInstance() {
        return new IntroTwoFragments();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_intro_two, container, false);
        ButterKnife.bind(this, view);


        return view;
    }

}
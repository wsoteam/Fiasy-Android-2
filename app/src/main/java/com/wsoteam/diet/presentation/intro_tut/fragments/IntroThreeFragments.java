package com.wsoteam.diet.presentation.intro_tut.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wsoteam.diet.R;

import butterknife.ButterKnife;

public class IntroThreeFragments extends Fragment {


    public static IntroThreeFragments newInstance() {
        return new IntroThreeFragments();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_intro_three, container, false);
        ButterKnife.bind(this, view);


        return view;
    }

}
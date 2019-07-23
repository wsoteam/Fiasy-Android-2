package com.wsoteam.diet.presentation.auth.new_auth.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wsoteam.diet.R;

import butterknife.ButterKnife;

public class AuthFirstFragment extends Fragment {


    public static AuthFirstFragment newInstance() {
        return new AuthFirstFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_auth_first, container, false);
        ButterKnife.bind(this, view);


        return view;
    }

}
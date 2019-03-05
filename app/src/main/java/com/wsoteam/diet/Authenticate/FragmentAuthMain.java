package com.wsoteam.diet.Authenticate;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.wsoteam.diet.R;

public class FragmentAuthMain extends Fragment  {
    private static final String TAG = "";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.auth_fragment_main, null);

        view.findViewById(R.id.auth_main_btn_google).setOnClickListener((View.OnClickListener) getActivity());
        view.findViewById(R.id.auth_main_btn_signin).setOnClickListener((View.OnClickListener) getActivity());
        view.findViewById(R.id.auth_main_btn_create).setOnClickListener((View.OnClickListener) getActivity());
        view.findViewById(R.id.auth_main_btn_facebook).setOnClickListener((View.OnClickListener) getActivity());


        return view;
    }


}

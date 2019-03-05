package com.wsoteam.diet.Authenticate;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wsoteam.diet.R;

public class FragmentAuthFerst extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.auth_fragment_ferst, null);

//        Button
        view.findViewById(R.id.auth_first_btn_registration).setOnClickListener((View.OnClickListener) getActivity());
        view.findViewById(R.id.auth_first_btn_signin).setOnClickListener((View.OnClickListener) getActivity());

        return view;
    }
}

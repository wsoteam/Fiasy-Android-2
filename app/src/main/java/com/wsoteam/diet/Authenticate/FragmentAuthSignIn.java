package com.wsoteam.diet.Authenticate;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wsoteam.diet.R;

public class FragmentAuthSignIn extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.auth_fragment_signin, null);

//        Button
            view.findViewById(R.id.auth_signin_btn_signin).setOnClickListener((View.OnClickListener) getActivity());
            view.findViewById(R.id.auth_signin_btn_signout).setOnClickListener((View.OnClickListener) getActivity());
            view.findViewById(R.id.auth_signin_btn_google).setOnClickListener((View.OnClickListener) getActivity());

        return view;
    }
}

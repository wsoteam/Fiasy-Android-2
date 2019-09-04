package com.wsoteam.diet.Authenticate;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wsoteam.diet.R;

public class FragmentAuthMission extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.auth_fragment_mission, null);

        view.findViewById(R.id.auth_mission_btn_lose).setOnClickListener((View.OnClickListener) getActivity());
        view.findViewById(R.id.auth_mission_btn_get).setOnClickListener((View.OnClickListener) getActivity());
        view.findViewById(R.id.auth_mission_btn_save).setOnClickListener((View.OnClickListener) getActivity());

        return view;
    }
}

package com.wsoteam.diet.InApp.bigtest.slides;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wsoteam.diet.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class InAppSlideFragment extends Fragment {
    private static final String TAG = "InAppSlideFragment";
    private final int THREE_MONTH = 0, ONE_YEAR = 1, ONE_MONTH = 2;

    public static InAppSlideFragment newInstance(int subId){
        Bundle bundle = new Bundle();
        bundle.putInt(TAG, subId);
        InAppSlideFragment slideFragment = new InAppSlideFragment();
        slideFragment.setArguments(bundle);
        return slideFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        int subId = getArguments().getInt(TAG);
        View view = new View(getActivity());
        switch (subId){
            case THREE_MONTH: view = inflater.inflate(R.layout.slide_three_month, container, false);
            break;
            case ONE_YEAR: view = inflater.inflate(R.layout.slide_one_year, container, false);
            break;
            case ONE_MONTH: view = inflater.inflate(R.layout.slide_one_month, container, false);
            break;
        }

        return view;
    }
}

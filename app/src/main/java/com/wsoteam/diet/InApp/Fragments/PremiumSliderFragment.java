package com.wsoteam.diet.InApp.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wsoteam.diet.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class PremiumSliderFragment extends Fragment {
    private static final String TAG_OF_BUNDLE = "PremiumSliderFragment";
    @BindView(R.id.ivPremImage) ImageView ivPremImage;
    @BindView(R.id.tvPremOnboardingText) TextView tvPremOnboardingText;
    private Unbinder unbinder;
    private ImageView ivCounter;
    private int[] drawablesForSlider = new int[] {R.drawable.prem_0, R.drawable.prem_1, R.drawable.prem_2, R.drawable.prem_3};
    private String[] arrayOfTexts;
    private int[] drawablesForBottomCounter = new int[] {R.drawable.frame_0, R.drawable.frame_1,
            R.drawable.frame_2, R.drawable.frame_3};

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isResumed()) {
            fillCounter(getArguments().getInt(TAG_OF_BUNDLE));
        }
    }

    private void fillCounter(int positon) {
        Log.e("LOL", String.valueOf(positon));
        Glide.with(getActivity()).load(drawablesForBottomCounter[positon]).into(ivCounter);
    }


    @Override
    public void onResume() {
        super.onResume();
        if (getUserVisibleHint()) {
            setUserVisibleHint(true);
        }
    }


    public static PremiumSliderFragment newInstance(int position) {
        Bundle bundle = new Bundle();
        bundle.putInt(TAG_OF_BUNDLE, position);
        PremiumSliderFragment premiumSliderFragment = new PremiumSliderFragment();
        premiumSliderFragment.setArguments(bundle);
        return premiumSliderFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.premium_slider_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        ivCounter = getActivity().findViewById(R.id.ivFrameCounter);
        arrayOfTexts = getActivity().getResources().getStringArray(R.array.premium_slider_texts);
        fillViews(getArguments().getInt(TAG_OF_BUNDLE));
        return view;
    }

    private void fillViews(int position) {
        Glide.with(getActivity()).load(drawablesForSlider[position]).into(ivPremImage);
        tvPremOnboardingText.setText(arrayOfTexts[position]);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}

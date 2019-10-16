package com.wsoteam.diet.InApp.Fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.wsoteam.diet.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class PremiumSliderFragment extends Fragment {
    private static final String TAG_OF_BUNDLE = "PremiumSliderFragment";
    @BindView(R.id.ivPremImage) ImageView ivPremImage;
    @BindView(R.id.tvPremOnboardingText) TextView tvPremOnboardingText;
    private Unbinder unbinder;
    private int[] drawablesForSlider = new int[] {};
    private String[] arrayOfTexts;


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
        arrayOfTexts = getActivity().getResources().getStringArray(R.array.premium_slider_texts);
        fillViews(getArguments().getInt(TAG_OF_BUNDLE));
        return view;
    }

    private void fillViews(int position) {
        Picasso.get().load(drawablesForSlider[position]).into(ivPremImage);
        tvPremOnboardingText.setText(arrayOfTexts[position]);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}

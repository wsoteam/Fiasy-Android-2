package com.wsoteam.diet.BranchProfile.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.adjust.sdk.Adjust;
import com.adjust.sdk.AdjustEvent;
import com.amplitude.api.Amplitude;
import com.bumptech.glide.Glide;
import com.wsoteam.diet.AmplitudaEvents;
import com.wsoteam.diet.BranchProfile.ActivityEditProfile;
import com.wsoteam.diet.EventsAdjust;
import com.wsoteam.diet.POJOProfile.Profile;
import com.wsoteam.diet.R;
import com.wsoteam.diet.Sync.UserDataHolder;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;

public class FragmentProfile extends Fragment {
    @BindView(R.id.ibSettings) ImageButton ibProfileEdit;
    @BindView(R.id.tvProfileOld) TextView tvProfileOld;
    @BindView(R.id.tvProfileLevel) TextView tvProfileLevel;
    @BindView(R.id.civProfile) CircleImageView civProfile;
    Unbinder unbinder;
    @BindView(R.id.tvUserName) TextView tvUserName;
    @BindView(R.id.tvDateRegistration) TextView tvDateRegistration;
    @BindView(R.id.tvKcalMax) TextView tvKcalMax;
    @BindView(R.id.tvWaterMax) TextView tvWaterMax;
    @BindView(R.id.tvCarboCount) TextView tvCarboCount;
    @BindView(R.id.tvFatCount) TextView tvFatCount;
    @BindView(R.id.tvProtCount) TextView tvProtCount;


    @Override
    public void onResume() {
        super.onResume();
        if (UserDataHolder.getUserData().getProfile() != null) {
            Profile profile = UserDataHolder.getUserData().getProfile();
            fillViewsIfProfileNotNull(profile);
        }
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_profile, container, false);

        Adjust.trackEvent(new AdjustEvent(EventsAdjust.view_profile));
        Amplitude.getInstance().logEvent(AmplitudaEvents.view_profile);


        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    /*@OnClick({R.id.ibSettings})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ibSettings:
                Intent intent = new Intent(getActivity(), ActivityEditProfile.class);
                startActivity(intent);
                break;
           *//* case R.id.ibProfileLogout:
                FirebaseAuth.getInstance().signOut();
                LoginManager.getInstance().logOut();
                UserDataHolder.clearObject();
                getActivity().finish();
                startActivity(new Intent(getActivity(), ActivitySplash.class));
                break;*//*
        }
    }*/

    private void fillViewsIfProfileNotNull(Profile profile) {
        String day = "0", month = "0";

        tvProfileOld.setText(getActivity().getResources().getQuantityString(R.plurals.years, profile.getAge(), profile.getAge()));
        tvProfileLevel.setText(profile.getDifficultyLevel());

        if (profile.getNumberOfDay() < 10) {
            day = "0" + String.valueOf(profile.getNumberOfDay());
        } else {
            day = String.valueOf(profile.getNumberOfDay());
        }
        if ((profile.getMonth() + 1) < 10) {
            month = "0" + String.valueOf(profile.getMonth() + 1);
        } else {
            month = String.valueOf(profile.getMonth() + 1);
        }

        tvDateRegistration.setText("Зарегестрирован(а) с %1.%d.2019");

        if (profile.getDifficultyLevel().equals(getString(R.string.dif_level_easy))) {
            tvProfileLevel.setTextColor(getResources().getColor(R.color.level_easy));
        } else {
            if (profile.getDifficultyLevel().equals(getString(R.string.dif_level_normal))) {
                tvProfileLevel.setTextColor(getResources().getColor(R.color.level_normal));
            } else {
                tvProfileLevel.setTextColor(getResources().getColor(R.color.level_hard));
            }
        }
        if (!profile.getPhotoUrl().equals("default")) {
            Uri uri = Uri.parse(profile.getPhotoUrl());
            Glide.with(this).load(uri).into(civProfile);
        }
    }


    @OnClick({R.id.ibSettings, R.id.civProfile, R.id.tvUserName, R.id.ibEditName, R.id.tvEditParams})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ibSettings:
                break;
            case R.id.civProfile:
                break;
            case R.id.tvUserName:
                break;
            case R.id.ibEditName:
                break;
            case R.id.tvEditParams:
                break;
        }
    }
}

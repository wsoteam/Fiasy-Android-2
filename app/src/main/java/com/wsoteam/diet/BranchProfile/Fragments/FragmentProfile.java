package com.wsoteam.diet.BranchProfile.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
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
    @BindView(R.id.ibProfileEdit) ImageButton ibProfileEdit;
    @BindView(R.id.ibProfileBack) ImageButton ibProfileBack;
    @BindView(R.id.tvProfileName) TextView tvProfileName;
    @BindView(R.id.tvProfileOld) TextView tvProfileOld;
    @BindView(R.id.tvProfileGender) TextView tvProfileGender;
    @BindView(R.id.tvProfileLifestyle) TextView tvProfileLifestyle;
    @BindView(R.id.tvProfileLevel) TextView tvProfileLevel;
    @BindView(R.id.tvProfileWeight) TextView tvProfileWeight;
    @BindView(R.id.tvProfileHeight) TextView tvProfileHeight;
    @BindView(R.id.civProfile) CircleImageView civProfile;
    @BindView(R.id.rvProfileMainParams) RecyclerView rvProfileMainParams;
    Unbinder unbinder;


    private ItemAdapter itemAdapter;

    private int[] arrayOfDrawabaleArrowForChangeWeight = new int[]{R.drawable.ic_decrease_weight, R.drawable.ic_increase_weight};
    private int[] arrayOfBackgroundDrawables = new int[]{R.drawable.background_item_profile_kcal,
            R.drawable.background_item_profile_water, R.drawable.background_item_profile_fat,
            R.drawable.background_item_profile_carbo, R.drawable.background_item_profile_prot};
    private int[] arrayOfIcon = new int[]{R.drawable.ic_item_profile_kcal,
            R.drawable.ic_item_profile_water, R.drawable.ic_item_profile_fat,
            R.drawable.ic_item_profile_carbo, R.drawable.ic_item_profile_protein};
    private int[] arrayOfGradients = new int[]{R.drawable.gradient_filter_profile_kcal,
            R.drawable.gradient_filter_profile_water, R.drawable.gradient_filter_profile_fat,
            R.drawable.gradient_filter_profile_carbo, R.drawable.gradient_filter_profile_prot};

    @Override
    public void onResume() {
        super.onResume();
        if (UserDataHolder.getUserData().getProfile() != null) {
            Profile profile = UserDataHolder.getUserData().getProfile();
            updateUIOfList(profile);
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

    private void updateUIOfList(Profile profile) {
        itemAdapter = new ItemAdapter(profile);
        rvProfileMainParams.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        rvProfileMainParams.setAdapter(itemAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.ibProfileEdit, R.id.ibProfileBack})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ibProfileEdit:
                Intent intent = new Intent(getActivity(), ActivityEditProfile.class);
                startActivity(intent);
                break;
            case R.id.ibProfileBack:
                getActivity().onBackPressed();
                break;
        }
    }

    private void fillViewsIfProfileNotNull(Profile profile) {
        String day = "0", month = "0";

        tvProfileName.setText(profile.getFirstName() + " " + profile.getLastName());
        tvProfileOld.setText(String.valueOf(profile.getAge()));
        if (profile.isFemale()) {
            tvProfileGender.setText("Женщина");
        } else {
            tvProfileGender.setText("Мужчина");
        }
        tvProfileLifestyle.setText(profile.getExerciseStress());
        tvProfileWeight.setText(String.valueOf(profile.getWeight()) + " " + getString(R.string.kg));
        tvProfileLevel.setText(profile.getDifficultyLevel());
        tvProfileHeight.setText(String.valueOf(profile.getHeight()) + " " + getString(R.string.cm));
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

    private class ItemHolder extends RecyclerView.ViewHolder {
        private ImageView ivProfileItemIcon, ivProfileItemBackground, ivProfileItemFilter;
        private TextView tvProfileItemName, tvProfileItemSubstring;


        public ItemHolder(LayoutInflater layoutInflater, ViewGroup viewGroup) {
            super(layoutInflater.inflate(R.layout.item_main_params_profile, viewGroup, false));
            ivProfileItemIcon = itemView.findViewById(R.id.ivProfileItemIcon);
            ivProfileItemFilter = itemView.findViewById(R.id.ivProfileItemFilter);
            ivProfileItemBackground = itemView.findViewById(R.id.ivProfileItemBackground);
            tvProfileItemName = itemView.findViewById(R.id.tvProfileItemName);
            tvProfileItemSubstring = itemView.findViewById(R.id.tvProfileItemSubstring);
        }

        public void bind(String nameOfParam, int countOfMainParam, String afterMainParam,
                         int idBackgroud, int idFilter, int idIcon) {
            Glide.with(getActivity()).load(idFilter).into(ivProfileItemFilter);
            tvProfileItemName.setText(nameOfParam);
            tvProfileItemSubstring.setText(String.valueOf(countOfMainParam) + " " + afterMainParam);
            Glide.with(getActivity()).load(idBackgroud).into(ivProfileItemBackground);
            Glide.with(getActivity()).load(idIcon).into(ivProfileItemIcon);
        }
    }

    private class ItemAdapter extends RecyclerView.Adapter<ItemHolder> {
        Profile profile;

        public ItemAdapter(Profile profile) {
            this.profile = profile;
        }

        @NonNull
        @Override
        public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new ItemHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
            int mainParam = 0;
            switch (position) {
                case 0:
                    mainParam = profile.getMaxKcal();
                    break;
                case 1:
                    mainParam = profile.getWaterCount();
                    break;
                case 2:
                    mainParam = profile.getMaxFat();
                    break;
                case 3:
                    mainParam = profile.getMaxCarbo();
                    break;
                case 4:
                    mainParam = profile.getMaxProt();
                    break;
            }
            holder.bind(getResources().getStringArray(R.array.namesOfMainParam)[position],
                    mainParam, getResources().getStringArray(R.array.afterMainParam)[position],
                    arrayOfBackgroundDrawables[position], arrayOfGradients[position], arrayOfIcon[position]);
        }

        @Override
        public int getItemCount() {
            return 5;
        }
    }
}

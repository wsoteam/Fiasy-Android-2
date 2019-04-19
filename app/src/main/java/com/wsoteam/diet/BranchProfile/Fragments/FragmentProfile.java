package com.wsoteam.diet.BranchProfile.Fragments;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.adjust.sdk.Adjust;
import com.adjust.sdk.AdjustEvent;
import com.amplitude.api.Amplitude;
import com.bumptech.glide.Glide;
import com.wsoteam.diet.AmplitudaEvents;
import com.wsoteam.diet.BranchProfile.ActivityEditCompletedProfile;
import com.wsoteam.diet.BranchProfile.ActivityEditProfile;
import com.wsoteam.diet.EventsAdjust;
import com.wsoteam.diet.OtherActivity.ActivitySettings;
import com.wsoteam.diet.POJOProfile.Profile;
import com.wsoteam.diet.R;
import com.wsoteam.diet.Sync.UserDataHolder;
import com.wsoteam.diet.Sync.WorkWithFirebaseDB;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

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

        Amplitude.getInstance().logEvent(AmplitudaEvents.view_profile);


        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

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

        if (profile.isFemale()) {
            tvDateRegistration.setText("Зарегистрирована с " + day + "." + month + "." + String.valueOf(profile.getYear()));
        } else {
            tvDateRegistration.setText("Зарегистрирован с " + day + "." + month + "." + String.valueOf(profile.getYear()));

        }
        tvKcalMax.setText(String.valueOf(profile.getMaxKcal()));
        tvWaterMax.setText(String.valueOf(profile.getWaterCount()) + " мл");
        tvCarboCount.setText(String.valueOf(profile.getMaxCarbo()) + " г");
        tvFatCount.setText(String.valueOf(profile.getMaxFat()) + " г");
        tvProtCount.setText(String.valueOf(profile.getMaxProt()) + " г");

        if (profile.getFirstName().equals("default")) {
            tvUserName.setText("Введите Ваше имя");
        } else {
            tvUserName.setText(profile.getFirstName() + " " + profile.getLastName());
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
        if (profile.isFemale()) {
            Glide.with(this).load(R.drawable.female_avatar).into(civProfile);
        }else {
            Glide.with(this).load(R.drawable.male_avatar).into(civProfile);
        }
    }


    @OnClick({R.id.ibSettings, R.id.civProfile, R.id.tvUserName, R.id.ibEditName, R.id.tvEditParams, R.id.cvDifficulty})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ibSettings:
                startActivity(new Intent(getActivity(), ActivitySettings.class));
                break;
            case R.id.civProfile:
                /*Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 1);*/
                break;
            case R.id.tvUserName:
                startActivity(new Intent(getActivity(), ActivityEditCompletedProfile.class));
                break;
            case R.id.ibEditName:
                startActivity(new Intent(getActivity(), ActivityEditCompletedProfile.class));
                break;
            case R.id.tvEditParams:
                startActivity(new Intent(getActivity(), ActivityEditCompletedProfile.class));
                break;
            case R.id.cvDifficulty:
                selectHardLevel();
                break;
        }
    }

    private void calculate(String hardLevel) {
        Profile currentProfile = UserDataHolder.getUserData().getProfile();

        double SPK = 0, upLineSPK = 0, downLineSPK = 0;
        final int WATER_ON_KG_FEMALE = 30;
        final int WATER_ON_KG_MALE = 40;

        String levelNone = getString(R.string.level_none);
        double BOO = 0, SDD = 0.1;
        double rateNone = 1.2, rateEasy = 1.375, rateMedium = 1.4625, rateHard = 1.55,
                rateUpHard = 1.6375, rateSuper = 1.725, rateUpSuper = 1.9;
        int maxWater;
        double forCountUpLine = 300, forCountDownLine = 500;
        double fat, protein, carbohydrate;


        if (currentProfile.isFemale()) {
            BOO = (9.99 * currentProfile.getWeight() + 6.25 * ((double) currentProfile.getHeight()) - 4.92 * currentProfile.getAge() - 161) * 1.1;
        } else {
            BOO = (9.99 * currentProfile.getWeight() + 6.25 * ((double) currentProfile.getHeight()) - 4.92 * currentProfile.getAge() + 5) * 1.1;
        }

        /*Check level load*/
        if (currentProfile.getExerciseStress().equals(getString(R.string.level_none))) {
            SPK = BOO * rateNone;
        }
        if (currentProfile.getExerciseStress().equals(getString(R.string.level_easy))) {
            SPK = BOO * rateEasy;
        }
        if (currentProfile.getExerciseStress().equals(getString(R.string.level_medium))) {
            SPK = BOO * rateMedium;
        }
        if (currentProfile.getExerciseStress().equals(getString(R.string.level_hard))) {
            SPK = BOO * rateHard;
        }
        if (currentProfile.getExerciseStress().equals(getString(R.string.level_up_hard))) {
            SPK = BOO * rateUpHard;
        }
        if (currentProfile.getExerciseStress().equals(getString(R.string.level_super))) {
            SPK = BOO * rateSuper;
        }
        if (currentProfile.getExerciseStress().equals(getString(R.string.level_up_super))) {
            SPK = BOO * rateUpSuper;
        }

        upLineSPK = SPK - forCountUpLine;
        downLineSPK = SPK - forCountDownLine;

        fat = upLineSPK * 0.2 / 9;
        protein = upLineSPK * 0.3 / 4;
        carbohydrate = upLineSPK * 0.5 / 3.75;

        if (currentProfile.isFemale()) {
            maxWater = WATER_ON_KG_FEMALE * (int) currentProfile.getWeight();
        } else {
            maxWater = WATER_ON_KG_MALE * (int) currentProfile.getWeight();
        }


        Profile profile = new Profile(currentProfile.getFirstName(), currentProfile.getLastName(),
                currentProfile.isFemale(), currentProfile.getAge(), currentProfile.getHeight(), currentProfile.getWeight(), 0,
                currentProfile.getExerciseStress(), currentProfile.getPhotoUrl(), maxWater, 0, (int) protein,
                (int) fat, (int) carbohydrate, hardLevel, currentProfile.getNumberOfDay(), currentProfile.getMonth(), currentProfile.getYear());

        Log.e("LOL", profile.toString());

        if (hardLevel.equals(getString(R.string.dif_level_easy))) {
            profile.setMaxKcal((int) SPK);
            WorkWithFirebaseDB.putProfileValue(profile);
            fillViewsIfProfileNotNull(profile);



        } else if (hardLevel.equals(getString(R.string.dif_level_normal))) {
            profile.setMaxKcal((int) upLineSPK);
            WorkWithFirebaseDB.putProfileValue(profile);
            fillViewsIfProfileNotNull(profile);

        } else if (hardLevel.equals(getString(R.string.dif_level_hard))) {
            profile.setMaxKcal((int) downLineSPK);
            WorkWithFirebaseDB.putProfileValue(profile);
            fillViewsIfProfileNotNull(profile);

        }


    }

    private void selectHardLevel() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final AlertDialog alertDialog = builder.create();
        View view = View.inflate(getActivity(), R.layout.alert_dialog_choise_difficulty_level, null);
        CardView cvADChoiseDiffLevelHard = view.findViewById(R.id.cvADChoiseDiffLevelHard);
        CardView cvADChoiseDiffLevelNormal = view.findViewById(R.id.cvADChoiseDiffLevelNormal);
        CardView cvADChoiseDiffLevelEasy = view.findViewById(R.id.cvADChoiseDiffLevelEasy);

        cvADChoiseDiffLevelEasy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calculate(getActivity().getResources().getString(R.string.dif_level_easy));
                alertDialog.cancel();

            }
        });
        cvADChoiseDiffLevelNormal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calculate(getActivity().getResources().getString(R.string.dif_level_normal));

                alertDialog.cancel();
            }
        });
        cvADChoiseDiffLevelHard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calculate(getActivity().getResources().getString(R.string.dif_level_hard));
                alertDialog.cancel();
            }
        });
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        alertDialog.setView(view);
        alertDialog.show();


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Uri urlOfImage = data.getData();
            Glide.with(this).load(urlOfImage).into(civProfile);
            Profile profile = UserDataHolder.getUserData().getProfile();
            profile.setPhotoUrl(String.valueOf(urlOfImage));
            WorkWithFirebaseDB.putProfileValue(profile);
        }
    }
}

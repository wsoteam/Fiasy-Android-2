package com.wsoteam.diet.presentation.profile.section;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.bumptech.glide.Glide;
import com.github.lzyzsd.circleprogress.DonutProgress;
import com.wsoteam.diet.BranchOfAnalyzer.POJOFoodSQL.Food;
import com.wsoteam.diet.POJOProfile.Profile;
import com.wsoteam.diet.R;
import com.wsoteam.diet.presentation.profile.settings.ProfileSettingsActivity;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import dagger.android.support.AndroidSupportInjection;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ProfileFragment extends MvpAppCompatFragment implements ProfileView {
    @BindView(R.id.ibSettings) ImageButton ibProfileEdit;
    @BindView(R.id.civProfile) CircleImageView civProfile;
    @BindView(R.id.tvUserName) TextView tvUserName;
    @BindView(R.id.tvKcalMax) TextView tvKcalMax;
    @BindView(R.id.tvCarboCount) TextView tvCarboCount;
    @BindView(R.id.tvFatCount) TextView tvFatCount;
    @BindView(R.id.tvProtCount) TextView tvProtCount;
    private static final int CAMERA_REQUEST = 1888;
    Unbinder unbinder;
    @Inject
    @InjectPresenter
    ProfilePresenter profilePresenter;
    @BindViews({R.id.tvCarboCount, R.id.tvFatCount, R.id.tvProtCount, R.id.tvLabelProt, R.id.tvLabelCarbo, R.id.tvLabelFats})
    List<View> viewListExpandable;
    @BindView(R.id.ibExpandable) ImageButton ibExpandable;
    @BindView(R.id.donutProgress) DonutProgress donutProgress;
    private boolean isOpen = false;
    private float MAX_PROGRESS = 100;
    private long time = 500, periodTick = 5, countTick = 100;
    private CountDownTimer countDownTimer;

    @Override
    public void bindCircleProgressBar(float progress) {
        if (progress <= 100) {
            animFillProgressBar(progress, false);
        } else {
            animFillProgressBar(MAX_PROGRESS, true);
        }
    }

    private void animFillProgressBar(float progress, boolean isMax) {
        countDownTimer =  new CountDownTimer(time, periodTick) {
            @Override
            public void onTick(long millisUntilFinished) {
                int stepNumber = (int) ((time - millisUntilFinished) / periodTick);
                double stepValue = ((double) progress) / ((double) countTick);
                donutProgress.setProgress(((float) stepValue * stepNumber));

            }

            @Override
            public void onFinish() {
                if (isMax) {
                    donutProgress.setProgress(MAX_PROGRESS);
                    donutProgress.setFinishedStrokeColor(getResources().getColor(R.color.main_calories_left_over));
                }
            }
        }.start();

    }

    @Override
    public void onPause() {
        if (countDownTimer != null){
            countDownTimer.cancel();
        }
        super.onPause();
    }

    @Override
    public void fillViewsIfProfileNotNull(Profile profile) {
        tvKcalMax.setText(String.valueOf(profile.getMaxKcal()) + " ккал");
        tvCarboCount.setText(String.valueOf(profile.getMaxCarbo()) + " г");
        tvFatCount.setText(String.valueOf(profile.getMaxFat()) + " г");
        tvProtCount.setText(String.valueOf(profile.getMaxProt()) + " г");
        if (profile.getFirstName().equals("default")) {
            tvUserName.setText("Введите Ваше имя");
        } else {
            tvUserName.setText(profile.getFirstName() + " " + profile.getLastName());
        }
        setPhoto(profile);
    }

    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    @ProvidePresenter
    ProfilePresenter providePresenter() {
        return profilePresenter;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_profile, container, false);
        unbinder = ButterKnife.bind(this, view);
        profilePresenter.attachPresenter();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void setPhoto(Profile profile) {
        if (profile.getPhotoUrl() != null && !profile.getPhotoUrl().equals("default")) {
            Glide.with(this).load(profile.getPhotoUrl()).into(civProfile);
        } else {
            if (profile.isFemale()) {
                Glide.with(this).load(R.drawable.female_avatar).into(civProfile);
            } else {
                Glide.with(this).load(R.drawable.male_avatar).into(civProfile);
            }
        }
    }


    @OnClick({R.id.ibSettings, R.id.ibExpandable})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ibSettings:
                startActivity(new Intent(getActivity(), ProfileSettingsActivity.class));
                break;
            case R.id.ibExpandable:
                openParams();
                break;
           /* case R.id.civProfile:
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.CAMERA}, 1);
                } else {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }
                break;
            case R.id.tvUserName:
                startActivity(new Intent(getActivity(), ActivityEditCompletedProfile.class));
                break;*/
        }
    }

    private void openParams() {
        if (isOpen) {
            ButterKnife.apply(viewListExpandable, (view, value, index) -> view.setVisibility(value), View.GONE);
            isOpen = false;
            Glide.with(getActivity()).load(R.drawable.ic_open_detail_profile).into(ibExpandable);
        } else {
            ButterKnife.apply(viewListExpandable, (view, value, index) -> view.setVisibility(value), View.VISIBLE);
            isOpen = true;
            Glide.with(getActivity()).load(R.drawable.ic_close_detail_profile).into(ibExpandable);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            profilePresenter.uploadPhoto((Bitmap) data.getExtras().get("data"));
        }
    }
}

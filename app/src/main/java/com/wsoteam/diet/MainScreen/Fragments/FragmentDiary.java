package com.wsoteam.diet.MainScreen.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.adjust.sdk.Adjust;
import com.adjust.sdk.AdjustEvent;
import com.amplitude.api.Amplitude;
import com.bumptech.glide.Glide;
import com.github.lzyzsd.circleprogress.ArcProgress;
import com.wsoteam.diet.AmplitudaEvents;
import com.wsoteam.diet.Config;
import com.wsoteam.diet.EventsAdjust;
import com.wsoteam.diet.MainScreen.Support.AlertDialogChoiseEating;
import com.wsoteam.diet.POJOProfile.Profile;
import com.wsoteam.diet.R;
import com.wsoteam.diet.Sync.UserDataHolder;
import com.wsoteam.diet.Sync.WorkWithFirebaseDB;
import com.yandex.metrica.YandexMetrica;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.content.Context.MODE_PRIVATE;

public class FragmentDiary extends Fragment {
    @BindView(R.id.ibOpenYesterday) ImageButton ibOpenYesterday;
    @BindView(R.id.ibOpenTomorrow) ImageButton ibOpenTomorrow;
    private Unbinder unbinder;
    @BindView(R.id.tvCircleProgressProt) TextView tvCircleProgressProt;
    @BindView(R.id.apCollapsingKcal) ArcProgress apCollapsingKcal;
    @BindView(R.id.apCollapsingProt) ArcProgress apCollapsingProt;
    @BindView(R.id.apCollapsingCarbo) ArcProgress apCollapsingCarbo;
    @BindView(R.id.apCollapsingFat) ArcProgress apCollapsingFat;
    @BindView(R.id.ivCollapsingMainCompleteWater) ImageView ivCollapsingMainCompleteWater;
    @BindView(R.id.tvCircleProgressCarbo) TextView tvCircleProgressCarbo;
    @BindView(R.id.tvCircleProgressFat) TextView tvCircleProgressFat;
    @BindView(R.id.mainappbar) AppBarLayout mainappbar;
    @BindView(R.id.collapsingToolbarLayout) CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.vpEatingTimeLine) ViewPager vpEatingTimeLine;
    @BindView(R.id.tvDateForMainScreen) TextView tvDateForMainScreen;

    private Profile profile;

    private int COUNT_OF_RUN = 0;
    private final String TAG_COUNT_OF_RUN_FOR_ALERT_DIALOG = "COUNT_OF_RUN";
    private SharedPreferences countOfRun;
    private boolean isFiveStarSend = false;

    private AlertDialog alertDialogBuyInfo;
    private SharedPreferences sharedPreferences, freeUser;

    @Override
    public void onResume() {
        super.onResume();
        if (UserDataHolder.getUserData() != null && UserDataHolder.getUserData().getProfile() != null) {
            profile = UserDataHolder.getUserData().getProfile();
            setMaxParamsInProgressBars(profile);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.activity_main, container, false);
        unbinder = ButterKnife.bind(this, mainView);
        getActivity().setTitle("");
        Amplitude.getInstance().logEvent(AmplitudaEvents.view_diary);
        /** on your logout method:**/
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("com.wsoteam.diet.ACTION_LOGOUT");
        getActivity().sendBroadcast(broadcastIntent);

        boolean isPremAlert = getActivity().getSharedPreferences(Config.ALERT_BUY_SUBSCRIPTION, MODE_PRIVATE)
                .getBoolean(Config.ALERT_BUY_SUBSCRIPTION, false);

        if (isPremAlert) {
            Log.d("prem", "onCreate: ");

            View view = getLayoutInflater().inflate(R.layout.alert_dialog_buy_sub_info, null);
            Button button = view.findViewById(R.id.alerd_buy_info_btn);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (alertDialogBuyInfo != null) {
                        alertDialogBuyInfo.dismiss();
                    }
                }
            });

            alertDialogBuyInfo = new AlertDialog.Builder(getActivity())
                    .setView(view).create();

            alertDialogBuyInfo.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            alertDialogBuyInfo.show();

            sharedPreferences = getActivity().getSharedPreferences(Config.ALERT_BUY_SUBSCRIPTION, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(Config.ALERT_BUY_SUBSCRIPTION, false);
            editor.commit();
        }



        //showThankToast();
        //additionOneToSharedPreference();
        //checkFirstRun();
        bindViewPager();

        if (!isFreeUser()){
            WorkWithFirebaseDB.setFirebaseStateListener();
        }

        return mainView;
    }

    private boolean isFreeUser() {
        freeUser = getActivity().getSharedPreferences(Config.FREE_USER, MODE_PRIVATE);
        return freeUser.getBoolean(Config.FREE_USER, true);
    }

    private void bindViewPager() {
        vpEatingTimeLine.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return FragmentEatingScroll.newInstance(position);
            }

            @Override
            public int getCount() {
                return Config.COUNT_PAGE + 1;
            }
        });
        vpEatingTimeLine.setCurrentItem(Config.COUNT_PAGE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void showThankToast() {
        if (isFiveStarSend) {
            isFiveStarSend = false;
            TextView tvToastCompleteGift;
            ImageView ivToastCompleteGift;
            LayoutInflater toastInflater = getLayoutInflater();
            View toastLayout = toastInflater.inflate(R.layout.toast_complete_gift, null, false);
            tvToastCompleteGift = toastLayout.findViewById(R.id.tvToastCompleteGift);
            ivToastCompleteGift = toastLayout.findViewById(R.id.ivToastCompleteGift);
            tvToastCompleteGift.setText("Спасибо за отзыв!");

            Glide.with(getActivity()).load(R.drawable.icon_toast_thank_for_grade).into(ivToastCompleteGift);

            Toast toast = new Toast(getActivity());
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(toastLayout);
            toast.show();
        }
    }

    private void setMaxParamsInProgressBars(Profile profile) {
        apCollapsingKcal.setMax(profile.getMaxKcal());
        apCollapsingProt.setMax(profile.getMaxProt());
        apCollapsingCarbo.setMax(profile.getMaxCarbo());
        apCollapsingFat.setMax(profile.getMaxFat());
    }

    private void additionOneToSharedPreference() {
        countOfRun = getActivity().getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = countOfRun.edit();
        editor.putInt(TAG_COUNT_OF_RUN_FOR_ALERT_DIALOG, countOfRun.getInt(TAG_COUNT_OF_RUN_FOR_ALERT_DIALOG, COUNT_OF_RUN) + 1);
        editor.commit();

    }

    private void checkFirstRun() {
        if (countOfRun.getInt(TAG_COUNT_OF_RUN_FOR_ALERT_DIALOG, COUNT_OF_RUN) == 3) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            AlertDialog alertDialog = builder.create();
            View view = getLayoutInflater().inflate(R.layout.alert_dialog_grade, null);

            Animation movFromLeft = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_moving_from_left);
            Animation movOutToRight = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_moving_out_to_right);
            Animation movFromRight = AnimationUtils.loadAnimation(getActivity(), R.anim.moving_from_right);

            RatingBar ratingBar = view.findViewById(R.id.ratingBar);
            EditText edtReport = view.findViewById(R.id.edtRatingReport);
            TextView tvThank = view.findViewById(R.id.tvForGrade);
            Button btnGradeClose = view.findViewById(R.id.btnGradeClose);
            Button btnGradeLate = view.findViewById(R.id.btnGradeLate);
            Button btnGradeSend = view.findViewById(R.id.btnGradeSend);

            btnGradeClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    YandexMetrica.reportEvent("Отказ в оценке");
                    alertDialog.cancel();
                }
            });

            btnGradeLate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    countOfRun = getActivity().getPreferences(MODE_PRIVATE);
                    SharedPreferences.Editor editor = countOfRun.edit();
                    editor.putInt(TAG_COUNT_OF_RUN_FOR_ALERT_DIALOG, 0);
                    editor.commit();
                    YandexMetrica.reportEvent("Оценка отложена");
                    alertDialog.cancel();
                }
            });

            btnGradeSend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "sav@wsoteam.com", null));
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Отзыв");
                    intent.putExtra(Intent.EXTRA_TEXT, edtReport.getText().toString());
                    startActivity(Intent.createChooser(intent, "Send Email"));
                    alertDialog.cancel();
                }
            });

            btnGradeSend.setVisibility(View.GONE);
            tvThank.setVisibility(View.GONE);
            edtReport.setVisibility(View.GONE);
            ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                    if (v >= 4) {
                        YandexMetrica.reportEvent("Переход в ГП для оценки");
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse("market://details?id=" + getActivity().getPackageName()));
                        startActivity(intent);
                        alertDialog.cancel();
                        isFiveStarSend = true;
                    } else {
                        if (edtReport.getVisibility() == View.GONE) {
                            edtReport.setVisibility(View.VISIBLE);
                            edtReport.startAnimation(movFromLeft);
                            ratingBar.startAnimation(movOutToRight);
                            ratingBar.setVisibility(View.GONE);
                            tvThank.setVisibility(View.VISIBLE);
                            tvThank.startAnimation(movFromLeft);
                            btnGradeSend.setVisibility(View.VISIBLE);
                            btnGradeSend.startAnimation(movFromRight);
                        }
                    }
                }
            });
            alertDialog.setView(view);
            alertDialog.show();
        }
    }



    @OnClick({R.id.ibOpenYesterday, R.id.ibOpenTomorrow, R.id.fabAddEating})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ibOpenYesterday:
                vpEatingTimeLine.setCurrentItem(vpEatingTimeLine.getCurrentItem() - 1);
                break;
            case R.id.ibOpenTomorrow:
                vpEatingTimeLine.setCurrentItem(vpEatingTimeLine.getCurrentItem() + 1);
                break;
            case R.id.fabAddEating:
                AlertDialogChoiseEating.createChoiseEatingAlertDialog(getActivity(),
                        tvDateForMainScreen.getText().toString()).show();
                break;
        }
    }
}

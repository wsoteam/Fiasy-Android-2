package com.wsoteam.diet.MainScreen.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.amplitude.api.Amplitude;
import com.view.calender.horizontal.umar.horizontalcalendarview.DayDateMonthYearModel;
import com.view.calender.horizontal.umar.horizontalcalendarview.HorizontalCalendarListener;
import com.view.calender.horizontal.umar.horizontalcalendarview.HorizontalCalendarView;
import com.wsoteam.diet.AmplitudaEvents;
import com.wsoteam.diet.Config;
import com.wsoteam.diet.MainScreen.intercom.IntercomFactory;
import com.wsoteam.diet.POJOProfile.Profile;
import com.wsoteam.diet.R;
import com.wsoteam.diet.Sync.UserDataHolder;
import com.wsoteam.diet.Sync.WorkWithFirebaseDB;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.intercom.android.sdk.Intercom;

import static android.content.Context.MODE_PRIVATE;

public class FragmentDiary extends Fragment implements HorizontalCalendarListener {
    private final String TAG_COUNT_OF_RUN_FOR_ALERT_DIALOG = "COUNT_OF_RUN";
    @BindView(R.id.ibOpenYesterday) ImageButton ibOpenYesterday;
    @BindView(R.id.ibOpenTomorrow) ImageButton ibOpenTomorrow;
    @BindView(R.id.tvCalendarMonth) TextView tvCalendarMonth;
    @BindView(R.id.pbProt) ProgressBar pbProgressProt;
    @BindView(R.id.pbCarbo) ProgressBar pbProgressCarbo;
    @BindView(R.id.pbFat) ProgressBar pbProgressFat;
    @BindView(R.id.pbCalories) ProgressBar pbProgressCalories;
    @BindView(R.id.tvCarbo) TextView tvCarbo;
    @BindView(R.id.tvFat) TextView tvFat;
    @BindView(R.id.tvProt) TextView tvProt;
    @BindView(R.id.mainappbar) AppBarLayout mainappbar;
    @BindView(R.id.cvParams) CardView cvParams;
    @BindView(R.id.collapsingToolbarLayout) CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.vpEatingTimeLine) ViewPager vpEatingTimeLine;
    @BindView(R.id.tvDateForMainScreen) TextView tvDateForMainScreen;
    @BindView(R.id.horizontalcalendarview) HorizontalCalendarView horizontalcalendarview;
    private Unbinder unbinder;
    private Profile profile;
    private int COUNT_OF_RUN = 0;
    private SharedPreferences countOfRun;
    private boolean isFiveStarSend = false;

    private AlertDialog alertDialogBuyInfo;
    private SharedPreferences sharedPreferences, freeUser;

    @Override
    public void onResume() {
        super.onResume();
        IntercomFactory.show();
        if (UserDataHolder.getUserData() != null && UserDataHolder.getUserData().getProfile() != null) {
            profile = UserDataHolder.getUserData().getProfile();
            setMaxParamsInProgressBars(profile);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.activity_main_new, container, false);
        unbinder = ButterKnife.bind(this, mainView);
        getActivity().setTitle("");
        Amplitude.getInstance().logEvent(AmplitudaEvents.view_diary);
        /** on your logout method:**/
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("com.wsoteam.diet.ACTION_LOGOUT");
        getActivity().sendBroadcast(broadcastIntent);

        WorkWithFirebaseDB.setFirebaseStateListener();

        horizontalcalendarview.setContext(this);

        boolean isPremAlert = getActivity().getSharedPreferences(Config.ALERT_BUY_SUBSCRIPTION, MODE_PRIVATE)
                .getBoolean(Config.ALERT_BUY_SUBSCRIPTION, false);

        if (isPremAlert) {
            View view = getLayoutInflater().inflate(R.layout.alert_dialog_buy_sub_info, null);
            Button button = view.findViewById(R.id.alerd_buy_info_btn);
            button.setOnClickListener(view1 -> {
                if (alertDialogBuyInfo != null) {
                    alertDialogBuyInfo.dismiss();
                }
            });

            alertDialogBuyInfo = new AlertDialog.Builder(getActivity())
                    .setView(view).create();

            alertDialogBuyInfo.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            alertDialogBuyInfo.show();

            sharedPreferences = getActivity().getSharedPreferences(Config.ALERT_BUY_SUBSCRIPTION, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(Config.ALERT_BUY_SUBSCRIPTION, false);
            editor.apply();
        }

        cvParams.setBackgroundResource(R.drawable.main_card_params);

        bindViewPager();
        return mainView;
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
    public void updateMonthOnScroll(DayDateMonthYearModel selectedDate) {
        tvCalendarMonth.setText(getResources().getStringArray(R.array.monthList)[Integer.parseInt(selectedDate.monthNumeric) - 1]);
    }

    @Override
    public void newDateSelected(DayDateMonthYearModel selectedDate) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onPause() {
        super.onPause();
        IntercomFactory.hide();
    }

    private void setMaxParamsInProgressBars(Profile profile) {
        pbProgressCalories.setMax(profile.getMaxKcal());
        pbProgressProt.setMax(profile.getMaxProt());
        pbProgressCarbo.setMax(profile.getMaxCarbo());
        pbProgressFat.setMax(profile.getMaxFat());
    }

    private void additionOneToSharedPreference() {
        countOfRun = getActivity().getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = countOfRun.edit();
        editor.putInt(TAG_COUNT_OF_RUN_FOR_ALERT_DIALOG, countOfRun.getInt(TAG_COUNT_OF_RUN_FOR_ALERT_DIALOG, COUNT_OF_RUN) + 1);
        editor.apply();

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
                /*AlertDialogChoiseEating.createChoiseEatingAlertDialog(getActivity(),
                        tvDateForMainScreen.getText().toString()).show();*/
                Intercom.client().displayMessenger();
                break;
        }
    }
}

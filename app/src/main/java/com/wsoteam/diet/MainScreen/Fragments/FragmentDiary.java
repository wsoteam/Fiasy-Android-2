package com.wsoteam.diet.MainScreen.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.amplitude.api.Amplitude;
import com.github.jhonnyx2012.horizontalpicker.DatePickerListener;
import com.github.jhonnyx2012.horizontalpicker.HorizontalPicker;
import com.wsoteam.diet.AmplitudaEvents;
import com.wsoteam.diet.BranchProfile.Fragments.FragmentProfile;
import com.wsoteam.diet.Config;
import com.wsoteam.diet.MainScreen.Dialogs.SublimePickerDialogFragment;
import com.wsoteam.diet.MainScreen.intercom.IntercomFactory;
import com.wsoteam.diet.POJOProfile.Profile;
import com.wsoteam.diet.R;
import com.wsoteam.diet.Sync.UserDataHolder;
import com.wsoteam.diet.Sync.WorkWithFirebaseDB;

import org.joda.time.DateTime;
import org.joda.time.Days;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.intercom.android.sdk.Intercom;

import static android.content.Context.MODE_PRIVATE;

public class FragmentDiary extends Fragment implements SublimePickerDialogFragment.OnDateChoosedListener, DatePickerListener {
    private final String TAG_COUNT_OF_RUN_FOR_ALERT_DIALOG = "COUNT_OF_RUN";
    @BindView(R.id.pbProt)
    ProgressBar pbProgressProt;
    @BindView(R.id.pbCarbo)
    ProgressBar pbProgressCarbo;
    @BindView(R.id.pbFat)
    ProgressBar pbProgressFat;
    @BindView(R.id.pbCalories)
    ProgressBar pbProgressCalories;
    @BindView(R.id.tvCarbo)
    TextView tvCarbo;
    @BindView(R.id.tvFat)
    TextView tvFat;
    @BindView(R.id.tvProt)
    TextView tvProt;
    @BindView(R.id.tvCaloriesNeed)
    TextView tvCaloriesNeed;
    @BindView(R.id.textView138)
    TextView tvDaysAtRow;
    @BindView(R.id.mainappbar)
    AppBarLayout mainappbar;
    @BindView(R.id.cvParams)
    CardView cvParams;
    @BindView(R.id.collapsingToolbarLayout)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.vpEatingTimeLine)
    ViewPager vpEatingTimeLine;
    @BindView(R.id.llSum)
    LinearLayout llSum;
    @BindView(R.id.llHead)
    ConstraintLayout llHead;
    @BindView(R.id.datePicker)
    HorizontalPicker datePicker;
    @BindView(R.id.btnNotification)
    ImageView btnNotification;
    private BottomNavigationView bnvMain;
    private Unbinder unbinder;
    private Profile profile;
    private int COUNT_OF_RUN = 0;
    private FragmentTransaction transaction;
    private int dayPosition = Config.COUNT_PAGE;
    private SharedPreferences countOfRun;
    private boolean isFiveStarSend = false;
    private AlertDialog alertDialogBuyInfo;
    private SharedPreferences sharedPreferences, freeUser;
    private LinearLayout.LayoutParams layoutParams;
    private Window window;
    private ViewPager.OnPageChangeListener viewPagerListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i1) {
        }

        @Override
        public void onPageSelected(int i) {
            if (dayPosition < i)
                datePicker.plusDay();
            else if (dayPosition > i)
                datePicker.minusDay();

            dayPosition = i;
        }

        @Override
        public void onPageScrollStateChanged(int i) {
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.activity_main_new, container, false);
        unbinder = ButterKnife.bind(this, mainView);
        getActivity().setTitle("");
        bnvMain = getActivity().findViewById(R.id.bnv_main);
        transaction = getActivity().getSupportFragmentManager().beginTransaction();
        Amplitude.getInstance().logEvent(AmplitudaEvents.view_diary);
        /** on your logout method:**/
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("com.wsoteam.diet.ACTION_LOGOUT");
        getActivity().sendBroadcast(broadcastIntent);

        layoutParams = (LinearLayout.LayoutParams) llHead.getLayoutParams();

        mainappbar.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            float diff = (float) Math.abs(verticalOffset) / appBarLayout.getTotalScrollRange();
            llHead.setAlpha(diff);
            float offset = (diff * 81) - 81;
            layoutParams.topMargin = convertDpToPx((int) offset);
            llHead.setLayoutParams(layoutParams);
        });

        WorkWithFirebaseDB.setFirebaseStateListener();

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

        bindViewPager();

        datePicker.setListener(this).init();
        datePicker.setBackgroundColor(Color.TRANSPARENT);

        return mainView;
    }

    private int convertDpToPx(int dp) {
        float pixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, llHead.getResources().getDisplayMetrics());
        return Math.round(pixels);
    }

    private void bindViewPager() {
        vpEatingTimeLine.addOnPageChangeListener(viewPagerListener);
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
    }

    @Override
    public void dateChoosed(Calendar calendar, int dayOfMonth, int month, int year) {
        datePicker.setDate(new DateTime(calendar.getTime()).withTime(0, 0, 0, 0));
    }

    @Override
    public void onDateSelected(DateTime dateSelected) {
        DateTime today = new DateTime().withTime(0, 0, 0, 0);
        int difference = Days.daysBetween(dateSelected, today).getDays() * (dateSelected.getYear() < today.getMillis() ? -1 : 1);
        int page = Config.COUNT_PAGE / 2 + difference;
        if (vpEatingTimeLine != null && vpEatingTimeLine.getCurrentItem() != page) {
            vpEatingTimeLine.clearOnPageChangeListeners();
            vpEatingTimeLine.setCurrentItem(page);
            vpEatingTimeLine.addOnPageChangeListener(viewPagerListener);
            dayPosition = vpEatingTimeLine.getCurrentItem();
        }
    }

    @Override
    public void onCalendarClicked() {
        SublimePickerDialogFragment sublimePickerDialogFragment = new SublimePickerDialogFragment();
        Bundle bundle = new Bundle();
        sublimePickerDialogFragment.setArguments(bundle);
        sublimePickerDialogFragment.setCancelable(true);
        sublimePickerDialogFragment.setTargetFragment(this, 0);
        sublimePickerDialogFragment.show(getFragmentManager(), null);
    }

    private void attachCaloriesPopup() {
        View popupView = getLayoutInflater().inflate(R.layout.layout_notification_calories_over, null);
        final PopupWindow popupWindow = new PopupWindow(popupView,
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setContentView(popupView);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupWindow.showAsDropDown(tvCaloriesNeed, tvCaloriesNeed.getWidth() / 2, 0, Gravity.BOTTOM);
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

    private int getDaysAtRow() {
        countOfRun = getActivity().getPreferences(MODE_PRIVATE);
        return countOfRun.getInt(TAG_COUNT_OF_RUN_FOR_ALERT_DIALOG, 0);
    }

    @OnClick({R.id.fabAddEating, R.id.btnNotification, R.id.tvReports})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fabAddEating:
                /*AlertDialogChoiseEating.createChoiceEatingAlertDialog(getActivity(),
                        tvDateForMainScreen.getText().toString()).show();*/
                Intercom.client().displayMessenger();
                break;
            case R.id.btnNotification:
                attachCaloriesPopup();
                break;
            case R.id.tvReports:
                window = getActivity().getWindow();
                window.setStatusBarColor(Color.parseColor("#2E4E4E"));
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                transaction.replace(R.id.flFragmentContainer, new FragmentProfile()).commit();
                bnvMain.setSelectedItemId(R.id.bnv_main_profile);
                break;
        }
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

    @Override
    public void onResume() {
        super.onResume();
        IntercomFactory.show();
        if (UserDataHolder.getUserData() != null && UserDataHolder.getUserData().getProfile() != null) {
            profile = UserDataHolder.getUserData().getProfile();
            setMaxParamsInProgressBars(profile);
            tvDaysAtRow.setText(getActivity().getResources().getQuantityString(R.plurals.daysAtRow, getDaysAtRow(), getDaysAtRow()));
        }
        if (datePicker != null)
            datePicker.setDate(new DateTime());
    }
}

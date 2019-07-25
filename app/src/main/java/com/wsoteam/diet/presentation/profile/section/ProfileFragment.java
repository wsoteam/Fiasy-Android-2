package com.wsoteam.diet.presentation.profile.section;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.bumptech.glide.Glide;
import com.github.lzyzsd.circleprogress.DonutProgress;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.wsoteam.diet.BranchOfAnalyzer.TabsFragment;
import com.wsoteam.diet.POJOProfile.Profile;
import com.wsoteam.diet.R;
import com.wsoteam.diet.common.views.bar.BarRender;
import com.wsoteam.diet.common.views.bar.formater.XWeekFormatter;
import com.wsoteam.diet.common.views.bar.formater.XYearFormatter;
import com.wsoteam.diet.common.views.bar.marker.BarMarker;
import com.wsoteam.diet.presentation.profile.settings.ProfileSettingsActivity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import dagger.android.support.AndroidSupportInjection;
import de.hdodenhof.circleimageview.CircleImageView;

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
    @BindView(R.id.gv) BarChart gv;
    @BindView(R.id.tvTopDateSwitcher) TextView tvTopDateSwitcher;
    @BindView(R.id.tvBottomDateSwitcher) TextView tvBottomDateSwitcher;
    private boolean isOpen = false;
    private float MAX_PROGRESS = 100;
    private long time = 500, periodTick = 5, countTick = 100;
    private CountDownTimer countDownTimer;
    private int counterMove = 0;
    private int INTERVAL_CHOISE = 0; //0 - week, 1 - month, 2 - year
    private final int CHOISED_WEEK = 0, CHOISED_MONTH = 1, CHOISED_YEAR = 2;
    private ArrayList<String> days = new ArrayList<>();

    @Override
    public void bindCircleProgressBar(float progress) {
        if (progress <= 100) {
            animFillProgressBar(progress, false);
        } else {
            animFillProgressBar(MAX_PROGRESS, true);
        }
    }

    private void animFillProgressBar(float progress, boolean isMax) {
        countDownTimer = new CountDownTimer(time, periodTick) {
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
    public void drawYearGraphs(List<BarEntry> pairs, int[] colors, String bottomText, String topText) {
        Log.e("LOL", String.valueOf(pairs.size()));
        BarDataSet barDataSet = new BarDataSet(pairs, "");
        barDataSet.setColors(colors);
        barDataSet.setDrawValues(false);

        BarData barData = new BarData(barDataSet);
        gv.setRenderer(new BarRender(gv, gv.getAnimator(), gv.getViewPortHandler(), colors, 18));
        gv.setDrawValueAboveBar(true);
        barData.setBarWidth(0.4f);

        XAxis xAxis = gv.getXAxis();
        xAxis.setDrawGridLines(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new XYearFormatter());

        YAxis yAxisRight = gv.getAxisRight();
        yAxisRight.setEnabled(false);
        yAxisRight.setAxisMinimum(0f);

        YAxis yAxisLeft = gv.getAxisLeft();
        yAxisLeft.setAxisMinimum(0f);
        gv.setMarker(new MarkerView(getActivity(), R.layout.marker_calories));
        BarMarker barMarker = new BarMarker(getActivity(), R.layout.marker_calories, colors, pairs);

        gv.setDrawBarShadow(false);
        gv.getDescription().setEnabled(false);
        gv.setDrawGridBackground(false);
        gv.getLegend().setEnabled(false);
        gv.setData(barData);
        gv.setMarker(barMarker);
        gv.notifyDataSetChanged();
        gv.invalidate();

        tvTopDateSwitcher.setText(topText);
        tvBottomDateSwitcher.setText(bottomText);
    }

    @Override
    public void drawMonthGraphs(List<BarEntry> pairs, int[] colors, String bottomText, String topText) {
        Log.e("LOL", String.valueOf(pairs.size()));
        BarDataSet barDataSet = new BarDataSet(pairs, "");
        barDataSet.setColors(colors);
        barDataSet.setDrawValues(false);

        BarData barData = new BarData(barDataSet);
        gv.setRenderer(new BarRender(gv, gv.getAnimator(), gv.getViewPortHandler(), colors, 18));
        gv.setDrawValueAboveBar(true);
        barData.setBarWidth(0.4f);

        XAxis xAxis = gv.getXAxis();
        xAxis.setDrawGridLines(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new XYearFormatter());

        YAxis yAxisRight = gv.getAxisRight();
        yAxisRight.setEnabled(false);
        yAxisRight.setAxisMinimum(0f);

        YAxis yAxisLeft = gv.getAxisLeft();
        yAxisLeft.setAxisMinimum(0f);
        gv.setMarker(new MarkerView(getActivity(), R.layout.marker_calories));
        BarMarker barMarker = new BarMarker(getActivity(), R.layout.marker_calories, colors, pairs);

        gv.setDrawBarShadow(false);
        gv.getDescription().setEnabled(false);
        gv.setDrawGridBackground(false);
        gv.getLegend().setEnabled(false);
        gv.setData(barData);
        gv.setMarker(barMarker);
        gv.notifyDataSetChanged();
        gv.invalidate();

        tvTopDateSwitcher.setText(topText);
        tvBottomDateSwitcher.setText(bottomText);
    }

    @Override
    public void drawWeekGraphs(List<BarEntry> pairs, int[] colors, String bottomText, String topText) {
        BarDataSet barDataSet = new BarDataSet(pairs, "");
        barDataSet.setColors(colors);
        barDataSet.setDrawValues(false);

        BarData barData = new BarData(barDataSet);
        gv.setRenderer(new BarRender(gv, gv.getAnimator(), gv.getViewPortHandler(), colors, 18));
        gv.setDrawValueAboveBar(true);
        barData.setBarWidth(0.4f);

        XAxis xAxis = gv.getXAxis();
        xAxis.setDrawGridLines(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new XWeekFormatter());

        YAxis yAxisRight = gv.getAxisRight();
        yAxisRight.setEnabled(false);
        yAxisRight.setAxisMinimum(0f);

        YAxis yAxisLeft = gv.getAxisLeft();
        yAxisLeft.setAxisMinimum(0f);
        gv.setMarker(new MarkerView(getActivity(), R.layout.marker_calories));
        BarMarker barMarker = new BarMarker(getActivity(), R.layout.marker_calories, colors, pairs);

        gv.setDrawBarShadow(false);
        gv.getDescription().setEnabled(false);
        gv.setDrawGridBackground(false);
        gv.getLegend().setEnabled(false);
        gv.setData(barData);
        gv.setMarker(barMarker);
        gv.notifyDataSetChanged();
        gv.invalidate();

        tvTopDateSwitcher.setText(topText);
        tvBottomDateSwitcher.setText(bottomText);
    }

    @Override
    public void onPause() {
        if (countDownTimer != null) {
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

    @Override
    public void onResume() {
        super.onResume();
        profilePresenter.attachPresenter();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_profile, container, false);
        unbinder = ButterKnife.bind(this, view);
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


    @OnClick({R.id.ibSettings, R.id.ibExpandable, R.id.ibLeft, R.id.ibRight})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ibSettings:
                startActivity(new Intent(getActivity(), ProfileSettingsActivity.class));
                break;
            case R.id.ibExpandable:
                openParams();
                break;
            case R.id.ibLeft:
                counterMove--;
                drawChoisedGraph();
                break;
            case R.id.ibRight:
                counterMove++;
                drawChoisedGraph();
                break;
        }
    }

    @OnClick({R.id.rbtnWeek, R.id.rbtnMonth, R.id.rbtnYear})
    public void onRadioButtonClicked(RadioButton radioButton) {
        boolean checked = radioButton.isChecked();
        switch (radioButton.getId()) {
            case R.id.rbtnWeek:
                if (checked) {
                    counterMove = 0;
                    INTERVAL_CHOISE = 0;
                    drawChoisedGraph();
                }
                break;
            case R.id.rbtnMonth:
                if (checked) {
                    counterMove = 0;
                    INTERVAL_CHOISE = 1;
                    drawChoisedGraph();
                }
                break;
            case R.id.rbtnYear:
                if (checked) {
                    counterMove = 0;
                    INTERVAL_CHOISE = 2;
                    drawChoisedGraph();
                }
                break;
        }
    }

    private void drawChoisedGraph() {
        switch (INTERVAL_CHOISE) {
            case CHOISED_WEEK:
                profilePresenter.getWeekGraph(counterMove);
                break;
            case CHOISED_MONTH:
                profilePresenter.getMonthGraph(counterMove);
                break;
            case CHOISED_YEAR:
                profilePresenter.getYearGraph(counterMove);
                break;
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

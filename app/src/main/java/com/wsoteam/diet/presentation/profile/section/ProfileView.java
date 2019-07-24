package com.wsoteam.diet.presentation.profile.section;

import com.arellomobile.mvp.GenerateViewState;
import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.github.mikephil.charting.data.BarEntry;
import com.wsoteam.diet.POJOProfile.Profile;
import com.wsoteam.diet.presentation.global.BaseView;

import java.util.List;
import java.util.SortedMap;


public interface ProfileView extends MvpView {
    void fillViewsIfProfileNotNull(Profile profile);
    void bindCircleProgressBar(float progress);
    void drawWeekGraphs(List<BarEntry> pairs, int[] colors, String bottomText, String topText);
    void drawYearGraphs(List<BarEntry> pairs, int[] colors, String bottomText, String topText);
}

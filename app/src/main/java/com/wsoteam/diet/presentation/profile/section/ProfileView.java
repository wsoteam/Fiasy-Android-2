package com.wsoteam.diet.presentation.profile.section;

import com.arellomobile.mvp.MvpView;
import com.github.mikephil.charting.data.BarEntry;
import com.wsoteam.diet.POJOProfile.Profile;
import java.util.ArrayList;
import java.util.List;


public interface ProfileView extends MvpView {
    void fillViewsIfProfileNotNull(Profile profile);
    void bindCircleProgressBar(float progress);
    void drawWeekGraphs(List<BarEntry> pairs, int[] colors, String bottomText, String topText);
    void drawYearGraphs(List<BarEntry> pairs, int[] colors, String bottomText, String topText);
    void drawMonthGraphs(List<BarEntry> pairs, int[] colors, String bottomText, String topText, ArrayList<String> namesIntervals);
}

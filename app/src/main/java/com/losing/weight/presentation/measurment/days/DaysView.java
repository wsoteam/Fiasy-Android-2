package com.losing.weight.presentation.measurment.days;

import com.arellomobile.mvp.MvpView;
import com.losing.weight.presentation.measurment.POJO.Weight;

import java.util.List;

public interface DaysView extends MvpView {
    void updateUI(List<Weight> weightsForShow, String topText, String bottomText, String weekAverage, int currentDayNumber, boolean isNeedRefreshLabels);
    void showUpdateWeightToast();
}

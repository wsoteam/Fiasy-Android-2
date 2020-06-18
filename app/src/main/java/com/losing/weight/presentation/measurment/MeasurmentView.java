package com.losing.weight.presentation.measurment;

import com.arellomobile.mvp.MvpView;
import com.losing.weight.presentation.measurment.POJO.Chest;
import com.losing.weight.presentation.measurment.POJO.Hips;
import com.losing.weight.presentation.measurment.POJO.Waist;

public interface MeasurmentView extends MvpView {
    void updateUI(Chest lastChest, Waist lastWaist, Hips lastHips, int chestTimeDiff, int chestValueDiff, int waistTimeDiff, int waistValueDiff, int hipsValueDiff, int hipsTimeDiff, int mainTimeDiff);

}

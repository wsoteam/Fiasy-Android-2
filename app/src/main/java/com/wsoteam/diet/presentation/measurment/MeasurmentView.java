package com.wsoteam.diet.presentation.measurment;

import com.arellomobile.mvp.MvpView;
import com.wsoteam.diet.presentation.measurment.POJO.Chest;
import com.wsoteam.diet.presentation.measurment.POJO.Hips;
import com.wsoteam.diet.presentation.measurment.POJO.Waist;
import com.wsoteam.diet.presentation.measurment.POJO.Weight;

import java.util.List;

public interface MeasurmentView extends MvpView {
    void updateUI(Chest lastChest, Waist lastWaist, Hips lastHips, int chestTimeDiff, int chestValueDiff, int waistTimeDiff, int waistValueDiff, int hipsValueDiff, int hipsTimeDiff, int mainTimeDiff);

}

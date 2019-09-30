package com.wsoteam.diet.presentation.measurment.history.fragment;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

@InjectViewState
public class MeasHistoryPresenter extends MvpPresenter<MeasHistoryView> {
    private final int MEAS_WEIGHT = 0;
    private final int MEAS_WAIST = 1;
    private final int MEAS_CHEST = 2;
    private final int MEAS_HIPS = 3;

    public MeasHistoryPresenter() {
    }

    public void getHistoryList(int type) {
        switch (type) {
            case MEAS_WEIGHT:
                getWeightHistory();
                break;
            case MEAS_WAIST:
                getWaistHistory();
                break;
            case MEAS_CHEST:
                getChestHistory();
                break;
            case MEAS_HIPS:
                getHipsHistory();
                break;
        }
    }

    private void getHipsHistory() {
    }

    private void getChestHistory() {
    }

    private void getWaistHistory() {
    }

    private void getWeightHistory() {
    }
}

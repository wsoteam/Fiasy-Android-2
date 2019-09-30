package com.wsoteam.diet.presentation.measurment.history.fragment;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.wsoteam.diet.Sync.UserDataHolder;
import com.wsoteam.diet.presentation.measurment.POJO.Weight;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@InjectViewState
public class MeasHistoryPresenter extends MvpPresenter<MeasHistoryView> {
    private final int MEAS_WEIGHT = 0;
    private final int MEAS_WAIST = 1;
    private final int MEAS_CHEST = 2;
    private final int MEAS_HIPS = 3;
    private ArrayList<String> keys = new ArrayList<>();
    private ArrayList<String> values = new ArrayList<>();

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
        if (UserDataHolder.getUserData().getWeights() != null && UserDataHolder.getUserData().getWeights().size() > 0){
            HashMap<String, Weight> weightHashMap = UserDataHolder.getUserData().getWeights();
            keys = new ArrayList<>();
            Iterator iterator = weightHashMap.entrySet().iterator();
            while (iterator.hasNext()){
                Map.Entry pair = (Map.Entry) iterator.next();
                keys.add((String) pair.getKey());
            }
            Collections.sort(keys);

            values = new ArrayList<>();
            for (int i = 0; i < keys.size(); i++) {
                values.add(String.valueOf(weightHashMap.get(keys.get(i)).getWeight()));
            }
        }else {
            keys = new ArrayList<>();
            values = new ArrayList<>();
        }

    }
}

package com.wsoteam.diet.presentation.measurment.history.fragment;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.wsoteam.diet.R;
import com.wsoteam.diet.Sync.UserDataHolder;
import com.wsoteam.diet.presentation.measurment.POJO.Weight;
import com.wsoteam.diet.presentation.measurment.history.Config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@InjectViewState
public class MeasHistoryPresenter extends MvpPresenter<MeasHistoryView> {
    private ArrayList<String> keys = new ArrayList<>();
    private ArrayList<String> values = new ArrayList<>();
    private Context context;

    public MeasHistoryPresenter() {
    }

    public MeasHistoryPresenter(Context context) {
        this.context = context;
    }

    public void getHistoryList(int type) {
        switch (type) {
            case Config.MEAS_WEIGHT:
                getWeightHistory();
                break;
            case Config.MEAS_WAIST:
                getWaistHistory();
                break;
            case Config.MEAS_CHEST:
                getChestHistory();
                break;
            case Config.MEAS_HIPS:
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
        if (UserDataHolder.getUserData().getWeights() != null && UserDataHolder.getUserData().getWeights().size() > 0) {
            HashMap<String, Weight> weightHashMap = UserDataHolder.getUserData().getWeights();
            keys = new ArrayList<>();
            Iterator iterator = weightHashMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry pair = (Map.Entry) iterator.next();
                keys.add((String) pair.getKey());
            }
            Collections.sort(keys);

            values = new ArrayList<>();
            for (int i = 0; i < keys.size(); i++) {
                values.add(String.valueOf(weightHashMap.get(keys.get(i)).getWeight()));
            }
        } else {
            keys = new ArrayList<>();
            values = new ArrayList<>();
        }

        getViewState().updateUI(keys, convertToSpannable(values));
    }

    private List<Spannable> convertToSpannable(ArrayList<String> values) {
        List<Spannable> paintedStrings = new ArrayList<>();
        for (int i = 0; i < values.size(); i++) {
            paintedStrings.add(toSpannable(values.get(i)));
        }
        return paintedStrings;
    }

    private Spannable toSpannable(String s) {
        int position = s.length();
        s = s + " " + context.getResources().getString(R.string.meas_kg);
        Spannable spannable = new SpannableString(s);
        spannable.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.value_history_color)), 0, position, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannable;
    }

    private void showLists(ArrayList<String> keys, ArrayList<String> values) {
        for (int i = 0; i < keys.size(); i++) {
            Log.e("LOL", "key - " + keys.get(i) + ", value - " + values.get(i));
        }
    }
}

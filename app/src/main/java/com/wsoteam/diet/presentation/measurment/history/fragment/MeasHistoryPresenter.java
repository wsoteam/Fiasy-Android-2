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
import com.wsoteam.diet.presentation.measurment.POJO.Chest;
import com.wsoteam.diet.presentation.measurment.POJO.Hips;
import com.wsoteam.diet.presentation.measurment.POJO.Waist;
import com.wsoteam.diet.presentation.measurment.POJO.Weight;
import com.wsoteam.diet.presentation.measurment.history.Config;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
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
        if (UserDataHolder.getUserData().getHips() != null && UserDataHolder.getUserData().getHips().size() > 0) {
            HashMap<String, Hips> measHashMap = UserDataHolder.getUserData().getHips();
            keys = new ArrayList<>();
            Iterator iterator = measHashMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry pair = (Map.Entry) iterator.next();
                keys.add((String) pair.getKey());
            }
            Collections.sort(keys);

            values = new ArrayList<>();
            for (int i = 0; i < keys.size(); i++) {
                values.add(String.valueOf(measHashMap.get(keys.get(i)).getMeas()));
            }
        } else {
            keys = new ArrayList<>();
            values = new ArrayList<>();
        }
        keys = keysToDates(keys);
        Collections.reverse(keys);
        Collections.reverse(values);
        getViewState().updateUI(keys, convertToSpannable(values, false));
    }

    private void getChestHistory() {
        if (UserDataHolder.getUserData().getChest() != null && UserDataHolder.getUserData().getChest().size() > 0) {
            HashMap<String, Chest> measHashMap = UserDataHolder.getUserData().getChest();
            keys = new ArrayList<>();
            Iterator iterator = measHashMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry pair = (Map.Entry) iterator.next();
                keys.add((String) pair.getKey());
            }
            Collections.sort(keys);

            values = new ArrayList<>();
            for (int i = 0; i < keys.size(); i++) {
                values.add(String.valueOf(measHashMap.get(keys.get(i)).getMeas()));
            }
            keys = keysToDates(keys);
            Collections.reverse(keys);
            Collections.reverse(values);
        } else {
            keys = new ArrayList<>();
            values = new ArrayList<>();
        }
        getViewState().updateUI(keys, convertToSpannable(values, false));
    }

    private void getWaistHistory() {
        if (UserDataHolder.getUserData().getWaist() != null && UserDataHolder.getUserData().getWaist().size() > 0) {
            HashMap<String, Waist> measHashMap = UserDataHolder.getUserData().getWaist();
            keys = new ArrayList<>();
            Iterator iterator = measHashMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry pair = (Map.Entry) iterator.next();
                keys.add((String) pair.getKey());
            }
            Collections.sort(keys);

            values = new ArrayList<>();
            for (int i = 0; i < keys.size(); i++) {
                values.add(String.valueOf(measHashMap.get(keys.get(i)).getMeas()));
            }
            keys = keysToDates(keys);
            Collections.reverse(keys);
            Collections.reverse(values);
        } else {
            keys = new ArrayList<>();
            values = new ArrayList<>();
        }
        getViewState().updateUI(keys, convertToSpannable(values, false));
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
            keys = keysToDates(keys);
            Collections.reverse(keys);
            Collections.reverse(values);
        } else {
            keys = new ArrayList<>();
            values = new ArrayList<>();
        }
        getViewState().updateUI(keys, convertToSpannable(values, true));
    }

    private ArrayList<String> keysToDates(ArrayList<String> keys) {
        ArrayList<Long> millisList = new ArrayList<>();
        for (int i = 0; i < keys.size(); i++) {
            millisList.add(Long.parseLong(keys.get(i)));
        }
        keys = new ArrayList<>();
        DateFormatSymbols months = new DateFormatSymbols() {
            @Override
            public String[] getMonths() {
                return context.getResources().getStringArray(R.array.names_months_meas);
            }
        };
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy", months);
        for (int i = 0; i < millisList.size(); i++) {
            Date date = new Date(millisList.get(i));
            keys.add(formatter.format(date));
        }
        return keys;
    }

    private List<Spannable> convertToSpannable(ArrayList<String> values, boolean isWeight) {
        List<Spannable> paintedStrings = new ArrayList<>();
        for (int i = 0; i < values.size(); i++) {
            paintedStrings.add(toSpannable(values.get(i), isWeight));
        }
        return paintedStrings;
    }

    private Spannable toSpannable(String s, boolean isWeight) {
        int position = s.length();
        String valueUnit;
        if (isWeight){
            valueUnit = context.getResources().getString(R.string.weight_unit);
        }else {
            valueUnit = context.getResources().getString(R.string.growth_unit);
        }
        s = s + " " + valueUnit;
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

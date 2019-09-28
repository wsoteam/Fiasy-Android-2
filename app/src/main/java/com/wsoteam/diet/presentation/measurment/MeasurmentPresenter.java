package com.wsoteam.diet.presentation.measurment;

import android.content.Context;
import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.wsoteam.diet.Sync.UserDataHolder;
import com.wsoteam.diet.Sync.WorkWithFirebaseDB;
import com.wsoteam.diet.presentation.measurment.POJO.Chest;
import com.wsoteam.diet.presentation.measurment.POJO.Hips;
import com.wsoteam.diet.presentation.measurment.POJO.Meas;
import com.wsoteam.diet.presentation.measurment.POJO.Waist;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@InjectViewState
public class MeasurmentPresenter extends MvpPresenter<MeasurmentView> {
    private Context context;
    private Calendar calendar;
    private long currentTime;
    private long oneDay = 86400000;

    private Chest lastChest;
    private Waist lastWaist;
    private Hips lastHips;
    private int chestValueDiff, waistValuesDiff, hipsValueDiff,
            chestTimeDiff, waistTimeDiff, hipsTimeDiff,
            mainTimeDiff;


    public MeasurmentPresenter() {
    }

    public MeasurmentPresenter(Context context) {
        this.context = context;
        calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        currentTime = calendar.getTimeInMillis();
        handlMeases();
    }

    private void handlMeases() {
        handlChests();
        handlWaists();
        handlHips();
        setMainTimeDiff();
        getViewState().updateUI(lastChest, lastWaist, lastHips, chestTimeDiff, chestValueDiff, waistTimeDiff, waistTimeDiff, hipsValueDiff, hipsTimeDiff, mainTimeDiff);
    }

    private void setMainTimeDiff() {
        int firstMax = Math.max(chestTimeDiff, waistTimeDiff);
        mainTimeDiff = Math.max(firstMax, hipsTimeDiff);
    }

    private void handlHips() {
        if (UserDataHolder.getUserData().getHips() == null || UserDataHolder.getUserData().getHips().size() == 0){
            lastHips = null;
            hipsValueDiff = 0;
            hipsTimeDiff = 0;
        }else {
            List<String> keys = new ArrayList<>();
            HashMap<String, Hips> meases = UserDataHolder.getUserData().getHips();
            Iterator iterator = meases.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry pair = (Map.Entry) iterator.next();
                keys.add((String) pair.getKey());
            }
            Collections.sort(keys);
            String lastKey = keys.get(keys.size() - 1);
            lastHips = meases.get(lastKey);
            if (keys.size() > 1) {
                String penultKey = keys.get(keys.size() - 2);
                Hips penultMeas = meases.get(penultKey);
                hipsValueDiff = lastHips.getMeas() - penultMeas.getMeas();
                hipsTimeDiff = Math.round((lastHips.getTimeInMillis() - penultMeas.getTimeInMillis()) / oneDay);
            }else {
                hipsValueDiff = 0;
                hipsTimeDiff = 0;
            }
        }
    }

    private void handlWaists() {
        if (UserDataHolder.getUserData().getWaist() == null || UserDataHolder.getUserData().getWaist().size() == 0){
            lastWaist = null;
            waistValuesDiff = 0;
            waistTimeDiff = 0;
        }else {
            List<String> keys = new ArrayList<>();
            HashMap<String, Waist> meases = UserDataHolder.getUserData().getWaist();
            Iterator iterator = meases.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry pair = (Map.Entry) iterator.next();
                keys.add((String) pair.getKey());
            }
            Collections.sort(keys);
            String lastKey = keys.get(keys.size() - 1);
            lastWaist = meases.get(lastKey);
            if (keys.size() > 1) {
                String penultKey = keys.get(keys.size() - 2);
                Waist penultMeas = meases.get(penultKey);
                waistValuesDiff = lastWaist.getMeas() - penultMeas.getMeas();
                waistTimeDiff = Math.round((lastWaist.getTimeInMillis() - penultMeas.getTimeInMillis()) / oneDay);
            }else {
                waistValuesDiff = 0;
                waistTimeDiff = 0;
            }
        }
    }

    private void handlChests() {
        if (UserDataHolder.getUserData().getChest() == null || UserDataHolder.getUserData().getChest().size() == 0){
            lastChest = null;
            chestValueDiff = 0;
            chestTimeDiff = 0;
        }else {
            List<String> keys = new ArrayList<>();
            HashMap<String, Chest> meases = UserDataHolder.getUserData().getChest();
            Iterator iterator = meases.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry pair = (Map.Entry) iterator.next();
                keys.add((String) pair.getKey());
            }
            Collections.sort(keys);
            String lastKey = keys.get(keys.size() - 1);
            lastChest = meases.get(lastKey);
            if (keys.size() > 1) {
                String penultKey = keys.get(keys.size() - 2);
                Chest penultChest = meases.get(penultKey);
                chestValueDiff = lastChest.getMeas() - penultChest.getMeas();
                chestTimeDiff = Math.round((lastChest.getTimeInMillis() - penultChest.getTimeInMillis()) / oneDay);
            }else {
                chestTimeDiff = 0;
                chestValueDiff = 0;
            }
        }
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
    }

    private void saveMeas(Meas meas) {
        if (meas instanceof Chest) {
            WorkWithFirebaseDB.setChest((Chest) meas);
        } else if (meas instanceof Waist) {
            WorkWithFirebaseDB.setWaist((Waist) meas);
        } else {
            WorkWithFirebaseDB.setHips((Hips) meas);
        }
    }

    private void clearTime() {
        calendar.set(Calendar.HOUR_OF_DAY, 1);
        calendar.set(Calendar.MINUTE, 1);
        calendar.set(Calendar.SECOND, 1);
        calendar.set(Calendar.MILLISECOND, 1);
    }

    private void createData() {
        for (int i = 0; i < 300; i++) {

            calendar.setTimeInMillis(currentTime);
            clearTime();
            calendar.setTimeInMillis(currentTime - oneDay * i);
            Chest chest = new Chest("", calendar.getTimeInMillis(), i);
            saveMeas(chest);
            Waist waist = new Waist("", calendar.getTimeInMillis(), i);
            saveMeas(waist);
            Hips hips = new Hips("", calendar.getTimeInMillis(), i);
            saveMeas(hips);
            Log.e("LOL", String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
        }
    }


}

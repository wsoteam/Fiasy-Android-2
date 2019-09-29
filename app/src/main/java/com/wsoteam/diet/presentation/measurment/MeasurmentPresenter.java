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
    private HashMap<String, Chest> listChests;
    private HashMap<String, Waist> listWaist;
    private HashMap<String, Hips> listHips;


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
        bindListMeases();
        handlChests(listChests);
        handlWaists(listWaist);
        handlHips(listHips);
        setMainTimeDiff();
        getViewState().updateUI(lastChest, lastWaist, lastHips, chestTimeDiff, chestValueDiff, waistTimeDiff, waistValuesDiff, hipsValueDiff, hipsTimeDiff, mainTimeDiff);
    }


    private void reHandleMeases() {
        handlChests(listChests);
        handlWaists(listWaist);
        handlHips(listHips);
        setMainTimeDiff();
        getViewState().updateUI(lastChest, lastWaist, lastHips, chestTimeDiff, chestValueDiff, waistTimeDiff, waistValuesDiff, hipsValueDiff, hipsTimeDiff, mainTimeDiff);
    }

    private void bindListMeases() {
        listChests = UserDataHolder.getUserData().getChest();
        listHips = UserDataHolder.getUserData().getHips();
        listWaist = UserDataHolder.getUserData().getWaist();
    }

    private void setMainTimeDiff() {
        int firstMax = Math.min(chestTimeDiff, waistTimeDiff);
        mainTimeDiff = Math.min(firstMax, hipsTimeDiff);
    }

    private void handlHips(HashMap<String, Hips> hips) {
        if (hips == null || hips.size() == 0){
            lastHips = null;
            hipsValueDiff = 0;
            hipsTimeDiff = 0;
        }else {
            List<String> keys = new ArrayList<>();
            Iterator iterator = hips.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry pair = (Map.Entry) iterator.next();
                keys.add((String) pair.getKey());
            }
            Collections.sort(keys);
            String lastKey = keys.get(keys.size() - 1);
            lastHips = hips.get(lastKey);
            if (keys.size() > 1) {
                String penultKey = keys.get(keys.size() - 2);
                Hips penultMeas = hips.get(penultKey);
                hipsValueDiff = lastHips.getMeas() - penultMeas.getMeas();
            }else {
                hipsValueDiff = 0;
            }
            hipsTimeDiff = getDaysDiff(lastHips.getTimeInMillis());
        }
    }

    private void handlWaists(HashMap<String, Waist> waist) {
        if (waist == null || waist.size() == 0){
            lastWaist = null;
            waistValuesDiff = 0;
            waistTimeDiff = 0;
        }else {
            List<String> keys = new ArrayList<>();
            Iterator iterator = waist.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry pair = (Map.Entry) iterator.next();
                keys.add((String) pair.getKey());
            }
            Collections.sort(keys);
            String lastKey = keys.get(keys.size() - 1);
            lastWaist = waist.get(lastKey);
            if (keys.size() > 1) {
                String penultKey = keys.get(keys.size() - 2);
                Waist penultMeas = waist.get(penultKey);
                waistValuesDiff = lastWaist.getMeas() - penultMeas.getMeas();
            }else {
                waistValuesDiff = 0;
            }
            waistTimeDiff = getDaysDiff(lastWaist.getTimeInMillis());
        }
    }

    private void handlChests(HashMap<String, Chest> chest) {
        if (chest == null || chest.size() == 0){
            lastChest = null;
            chestValueDiff = 0;
            chestTimeDiff = 0;
        }else {
            List<String> keys = new ArrayList<>();
            Iterator iterator = chest.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry pair = (Map.Entry) iterator.next();
                keys.add((String) pair.getKey());
            }
            Collections.sort(keys);
            String lastKey = keys.get(keys.size() - 1);
            lastChest = chest.get(lastKey);
            if (keys.size() > 1) {
                String penultKey = keys.get(keys.size() - 2);
                Chest penultChest = chest.get(penultKey);
                chestValueDiff = lastChest.getMeas() - penultChest.getMeas();
            }else {
                chestTimeDiff = 0;
            }
            chestTimeDiff = getDaysDiff(lastChest.getTimeInMillis());
        }
    }


    private int getDaysDiff(long lastMeasTime) {
        calendar.setTimeInMillis(currentTime);
        clearTime();
        int daysDiff = Math.round((calendar.getTimeInMillis() - lastMeasTime) / oneDay);
        return daysDiff;
    }


    public void saveMeas(Meas meas) {
        calendar.setTimeInMillis(currentTime);
        clearTime();
        meas.setTimeInMillis(calendar.getTimeInMillis());
        if (meas instanceof Chest) {
            WorkWithFirebaseDB.setChest((Chest) meas);
            if (listChests == null){
                listChests = new HashMap<>();
            }
            listChests.put(String.valueOf(meas.getTimeInMillis()), (Chest) meas);
        } else if (meas instanceof Waist) {
            WorkWithFirebaseDB.setWaist((Waist) meas);
            if (listWaist == null){
                listWaist = new HashMap<>();
            }
            listWaist.put(String.valueOf(meas.getTimeInMillis()), (Waist) meas);
        } else {
            WorkWithFirebaseDB.setHips((Hips) meas);
            if (listHips == null){
                listHips = new HashMap<>();
            }
            listHips.put(String.valueOf(meas.getTimeInMillis()), (Hips) meas);
        }
        reHandleMeases();
    }


    private void clearTime() {
        calendar.set(Calendar.HOUR_OF_DAY, 1);
        calendar.set(Calendar.MINUTE, 1);
        calendar.set(Calendar.SECOND, 1);
        calendar.set(Calendar.MILLISECOND, 1);
    }

    private void createData() {
        for (int i = 0; i < 300; i++) {
            calendar.setTimeInMillis(currentTime- oneDay * i);
            clearTime();
            Chest chest = new Chest("", calendar.getTimeInMillis(), i);
            saveMeas(chest);
            Waist waist = new Waist("", calendar.getTimeInMillis(), i);
            saveMeas(waist);
            Hips hips = new Hips("", calendar.getTimeInMillis(), i);
            saveMeas(hips);
        }
    }


    public Chest getLastChest() {
        if (lastChest != null) {
            return lastChest;
        }else {
            return new Chest();
        }
    }

    public Waist getLastWaist() {
        if (lastWaist != null) {
            return lastWaist;
        }else {
            return new Waist();
        }
    }

    public Hips getLastHips() {
        if (lastHips != null) {
            return lastHips;
        }else {
            return new Hips();
        }
    }
}

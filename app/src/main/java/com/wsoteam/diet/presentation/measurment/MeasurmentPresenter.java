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
import com.wsoteam.diet.presentation.measurment.POJO.Weight;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

@InjectViewState
public class MeasurmentPresenter extends MvpPresenter<MeasurmentView> {
    private Context context;
    private Calendar calendar;
    private long currentTime;
    private long oneDay = 86400000;

    public MeasurmentPresenter() {
    }

    public MeasurmentPresenter(Context context) {
        this.context = context;
        calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        currentTime = calendar.getTimeInMillis();
        createData();
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
    }

    private void saveMeas(Meas meas){
        if (meas instanceof Chest){
            WorkWithFirebaseDB.setChest((Chest) meas);
        }else if (meas instanceof Waist){
            WorkWithFirebaseDB.setWaist((Waist) meas);
        }else {
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

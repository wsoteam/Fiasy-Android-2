package com.wsoteam.diet.presentation.measurment.days;

import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.wsoteam.diet.Sync.UserDataHolder;
import com.wsoteam.diet.Sync.WorkWithFirebaseDB;
import com.wsoteam.diet.common.helpers.BodyCalculates;
import com.wsoteam.diet.common.helpers.DateAndTime;
import com.wsoteam.diet.presentation.measurment.ConfigMeasurment;
import com.wsoteam.diet.presentation.measurment.POJO.Weight;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

@InjectViewState
public class DaysPresenter extends MvpPresenter<DaysView> {
    private Calendar calendar;
    private final int START_POSITION = 0;
    private long oneDay = 86400000;
    private long currentTime;
    private HashMap<String, Weight> weights;
    private Context context;

    public DaysPresenter() {
    }


    public DaysPresenter(Context context) {
        this.context = context;
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        currentTime = calendar.getTimeInMillis();
    }



    public void updateUI(int position) {
        prepareWeekData(getWeekInterval(position));
    }

    public void refreshUI(int position, Weight weight){
        reCalculateWeekData(getWeekInterval(position), weight);
        if (weight != null) {
            handleCurrentWeight(weight);
        }
    }

    private void handleCurrentWeight(Weight weight) {
        calendar.setTimeInMillis(currentTime);
        calendar = DateAndTime.dropTime(calendar);
        long currentTimeInMillis = calendar.getTimeInMillis();
        if (currentTimeInMillis == weight.getTimeInMillis()) {
            getViewState().showUpdateWeightToast();
            BodyCalculates.saveWeight(weight.getWeight(), context);
        }
    }

    void reCalculateWeekData(long[] weekInterval, @Nullable Weight newWeight){
        int currentDayNumber = getCurrentDayNumber(weekInterval);

        calendar.setTimeInMillis(currentTime);
        calendar = DateAndTime.dropTime(calendar);
        if (newWeight != null) {
            weights.put(String.valueOf(newWeight.getTimeInMillis()), newWeight);
        }

        List<Weight> weightsForShow = new ArrayList<>();
        for (int i = 0; i < weekInterval.length; i++) {
            Weight weight = weights.get(String.valueOf(weekInterval[i]));
            if (weight != null) {
                weightsForShow.add(weight);
            }else {
                weightsForShow.add(new Weight("", weekInterval[i], ConfigMeasurment.EMPTY_DAY));
            }
        }
        getViewState().updateUI(weightsForShow, getTopText(weekInterval), getBottomText(weekInterval), getWeekAverage(weightsForShow), currentDayNumber, true);
    }

    void prepareWeekData(long[] weekInterval){
        int currentDayNumber = getCurrentDayNumber(weekInterval);

        calendar.setTimeInMillis(currentTime);
        calendar = DateAndTime.dropTime(calendar);
        weights = new HashMap<>();
        if (UserDataHolder.getUserData().getWeights() != null){
            weights = UserDataHolder.getUserData().getWeights();
        }

        List<Weight> weightsForShow = new ArrayList<>();
        for (int i = 0; i < weekInterval.length; i++) {
            Weight weight = weights.get(String.valueOf(weekInterval[i]));
            if (weight != null) {
                weightsForShow.add(weight);
            }else {
                weightsForShow.add(new Weight("", weekInterval[i], ConfigMeasurment.EMPTY_DAY));
            }
        }
        getViewState().updateUI(weightsForShow, getTopText(weekInterval), getBottomText(weekInterval), getWeekAverage(weightsForShow), currentDayNumber, false);

    }

    private int getCurrentDayNumber(long[] weekInterval) {
        int currentDayNumber = ConfigMeasurment.EMPTY_CURRENT_DAY;
        calendar.setTimeInMillis(currentTime);
        calendar = DateAndTime.dropTime(calendar);
        for (int i = 0; i < weekInterval.length; i++) {
            if (weekInterval[i] == calendar.getTimeInMillis()){
                currentDayNumber = i;
            }
        }

        if (currentDayNumber == ConfigMeasurment.EMPTY_CURRENT_DAY){
            if (calendar.getTimeInMillis() > weekInterval[weekInterval.length - 1]){
                currentDayNumber = ConfigMeasurment.FUTURE_WEEK;
            }else {
                currentDayNumber = ConfigMeasurment.PAST_WEEK;
            }
        }
        return currentDayNumber;
    }

    private String getWeekAverage(List<Weight> weightsForShow) {
        int countNotEmptyDays = 7;
        double sumWeights = 0;
        for (int i = 0; i < weightsForShow.size(); i++) {
            if (weightsForShow.get(i).getWeight() != ConfigMeasurment.EMPTY_DAY){
                sumWeights += weightsForShow.get(i).getWeight();
            }else {
                countNotEmptyDays --;
            }
        }
        if (countNotEmptyDays == 0){
            return ConfigMeasurment.EMPTY_WEEK;
        }else {
            double value = sumWeights / countNotEmptyDays;
            String doubleValue = String.format(Locale.US, "%.1f", value);
            return doubleValue;
        }
    }

    private String getTopText(long[] interval) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(interval[0]);
        return String.valueOf(calendar.get(Calendar.YEAR));
    }

    private String getBottomText(long[] interval) {
        String bottomText = "";
        calendar = DateAndTime.dropTime(calendar);
        calendar.setTimeInMillis(interval[0]);
        String firstDay = getNumber(calendar.get(Calendar.DAY_OF_MONTH));
        String firstMonth = getNumber(calendar.get(Calendar.MONTH) + 1);
        bottomText = firstDay + "." + firstMonth + " - ";

        calendar.setTimeInMillis(interval[interval.length - 1]);
        String secondDay = getNumber(calendar.get(Calendar.DAY_OF_MONTH));
        String secondMonth = getNumber(calendar.get(Calendar.MONTH) + 1);
        bottomText += secondDay + "." + secondMonth;
        return bottomText;
    }

    private String getNumber(int i) {
        String number;
        if (String.valueOf(i).length() == 1) {
            number = "0" + String.valueOf(i);
        } else {
            number = String.valueOf(i);
        }
        return number;
    }



    public long[] getWeekInterval(int position) {
        calendar.setTimeInMillis(currentTime);
        calendar = DateAndTime.dropTime(calendar);
        long[] weekInterval = new long[7];
        int week = calendar.get(Calendar.WEEK_OF_YEAR);
        calendar.set(Calendar.WEEK_OF_YEAR, week + position);
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
        for (int i = 0; i < 7; i++) {
            weekInterval[i] = calendar.getTimeInMillis() + oneDay * i;
        }
        return weekInterval;
    }

    public void addWeight(Weight weight){
        Weight weightMeasurment = new Weight("", weight.getTimeInMillis(), weight.getWeight());
        WorkWithFirebaseDB.setWeight(weightMeasurment, String.valueOf(weight.getTimeInMillis()));
    }

    private void createData() {
        for (int i = 0; i < 300; i++) {
            calendar.setTimeInMillis(currentTime);
            calendar = DateAndTime.dropTime(calendar);
            calendar.setTimeInMillis(currentTime - oneDay * i);
            //setWeight(85, calendar.getTimeInMillis());
            Log.e("LOL", String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
        }
    }

    public void deleteWeight(int currentPosition, Weight weight) {
        WorkWithFirebaseDB.deleteWeight(String.valueOf(weight.getTimeInMillis()));
        weights.remove(String.valueOf(weight.getTimeInMillis()));
        refreshUI(currentPosition, null);
    }

    public double getCurrentWeight() {
        return UserDataHolder.getUserData().getProfile().getWeight();
    }
}

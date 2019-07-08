package com.wsoteam.diet.presentation.profile.section;

import com.wsoteam.diet.Sync.UserDataHolder;
import com.wsoteam.diet.model.Breakfast;
import com.wsoteam.diet.model.Eating;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class SectionPresenter {

    public void dateSort() {
        HashMap<String, Integer> calories = new HashMap<>();
        Iterator caloriesIterator = calories.entrySet().iterator();

        HashMap<String, Eating> eatings = getAllEatingLists();
        Iterator eatingIterator = eatings.entrySet().iterator();

        while (eatingIterator.hasNext()) {
            Map.Entry pair = (Map.Entry) eatingIterator.next();
            Eating eating = (Eating) pair.getValue();
            String date = formDate(eating.getDay(), eating.getMonth(), eating.getYear());
            boolean isNeedCreateNewDate = true;

            if (calories.size() < 0) {
                calories.put(date, eating.getCalories());
                isNeedCreateNewDate = false;
            } else {
                while (caloriesIterator.hasNext()) {
                    Map.Entry entry = (Map.Entry) eatingIterator.next();
                    String key = (String) entry.getKey();
                    Integer sumCalories = (Integer) entry.getValue();
                    if (key.equals(date)) {
                        entry.setValue(sumCalories + eating.getCalories());
                        isNeedCreateNewDate = false;
                    }
                }
            }
            
            if (isNeedCreateNewDate){
                calories.put(date, eating.getCalories());
            }
        }


    }

    private HashMap<String, Eating> getAllEatingLists() {
        HashMap<String, Eating> eatings = new HashMap<>();
        if (UserDataHolder.getUserData() != null && UserDataHolder.getUserData().getBreakfasts() != null) {
            eatings.putAll(UserDataHolder.getUserData().getBreakfasts());
        }
        if (UserDataHolder.getUserData() != null && UserDataHolder.getUserData().getLunches() != null) {
            eatings.putAll(UserDataHolder.getUserData().getLunches());
        }
        if (UserDataHolder.getUserData() != null && UserDataHolder.getUserData().getDinners() != null) {
            eatings.putAll(UserDataHolder.getUserData().getDinners());
        }
        if (UserDataHolder.getUserData() != null && UserDataHolder.getUserData().getSnacks() != null) {
            eatings.putAll(UserDataHolder.getUserData().getSnacks());
        }
        return eatings;
    }


    private String formDate(int day, int month, int year) {
        String dayInString, monthInString, yearInString;

        if (day < 10) {
            dayInString = "0" + String.valueOf(day);
        } else {
            dayInString = String.valueOf(day);
        }

        if (month < 10) {
            monthInString = "0" + String.valueOf(month + 1);
        } else {
            monthInString = String.valueOf(month);
        }

        yearInString = String.valueOf(year);

        return dayInString + "." + monthInString + "." + yearInString;
    }
}

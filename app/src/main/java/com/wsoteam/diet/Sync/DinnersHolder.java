package com.wsoteam.diet.Sync;

import com.wsoteam.diet.BranchOfAnalyzer.POJOEating.Dinner;
import com.wsoteam.diet.BranchOfAnalyzer.POJOEating.Lunch;

import java.util.List;

public class DinnersHolder {
    private static List<Dinner> dinners;

    public void bindObjectWithHolder(List<Dinner> dinners){
        this.dinners = dinners;
    }

    public static List<Dinner> getDinners(){
        return dinners;
    }
}

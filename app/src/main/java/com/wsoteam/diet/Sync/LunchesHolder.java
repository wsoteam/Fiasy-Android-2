package com.wsoteam.diet.Sync;

import com.wsoteam.diet.BranchOfAnalyzer.POJOEating.Lunch;

import java.util.List;

public class LunchesHolder {
    private static List<Lunch> lunches;

    public void bindObjectWithHolder(List<Lunch> lunches){
        this.lunches = lunches;
    }

    public static List<Lunch> getLunches(){
        return lunches;
    }
}

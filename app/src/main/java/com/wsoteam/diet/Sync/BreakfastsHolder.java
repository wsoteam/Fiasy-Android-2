package com.wsoteam.diet.Sync;

import com.wsoteam.diet.BranchOfAnalyzer.POJOEating.Breakfast;

import java.util.List;

public class BreakfastsHolder {
    private static List<Breakfast> breakfasts;

    public void bindObjectWithHolder(List<Breakfast> breakfasts){
        this.breakfasts = breakfasts;
    }

    public static List<Breakfast> getBreakfasts(){
        return breakfasts;
    }
}

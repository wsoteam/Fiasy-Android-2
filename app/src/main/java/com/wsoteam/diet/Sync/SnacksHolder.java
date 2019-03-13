package com.wsoteam.diet.Sync;

import com.wsoteam.diet.BranchOfAnalyzer.POJOEating.Dinner;
import com.wsoteam.diet.BranchOfAnalyzer.POJOEating.Snack;

import java.util.List;

public class SnacksHolder {
    private static List<Snack> snacks;

    public void bindObjectWithHolder(List<Snack> snacks){
        this.snacks = snacks;
    }

    public static List<Snack> getSnacks(){
        return snacks;
    }
}

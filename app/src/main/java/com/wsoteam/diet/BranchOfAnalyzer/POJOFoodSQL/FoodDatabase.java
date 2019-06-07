package com.wsoteam.diet.BranchOfAnalyzer.POJOFoodSQL;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {Food.class}, version = 4, exportSchema = false)
public abstract class FoodDatabase extends RoomDatabase {
    public abstract FoodDAO foodDAO();
}

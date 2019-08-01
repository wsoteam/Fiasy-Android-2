package com.wsoteam.diet.BranchOfAnalyzer.POJOFoodSQL;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Food.class}, version = 4, exportSchema = false)
public abstract class FoodDatabase extends RoomDatabase {
    public abstract FoodDAO foodDAO();
}

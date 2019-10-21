package com.wsoteam.diet.BranchOfAnalyzer.POJOFoodSQL;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import com.wsoteam.diet.presentation.search.basket.db.BasketDAO;
import com.wsoteam.diet.presentation.search.basket.db.BasketEntity;
import com.wsoteam.diet.presentation.search.basket.db.HistoryDAO;
import com.wsoteam.diet.presentation.search.basket.db.HistoryEntity;

@Database(entities = {Food.class, BasketEntity.class, HistoryEntity.class }, version = 5, exportSchema = false)
public abstract class FoodDatabase extends RoomDatabase {
    public abstract FoodDAO foodDAO();
    public abstract BasketDAO basketDAO();
    public abstract HistoryDAO historyDAO();
}

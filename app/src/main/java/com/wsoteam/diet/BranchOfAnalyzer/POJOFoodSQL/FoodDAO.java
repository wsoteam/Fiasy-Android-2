package com.wsoteam.diet.BranchOfAnalyzer.POJOFoodSQL;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface FoodDAO {

    @Insert
    void insertAll(Food... foods);

    @Insert
    void insert(Food food);

    @Query("select * from Food where name like :color")
    List<Food> getFoods(String color);

}

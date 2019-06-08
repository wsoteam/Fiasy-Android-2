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

    @Query("select * from Food where name like :search")
    List<Food> getFoods(String search);

    @Query("select * from Food where name like :search limit :count offset :start")
    List<Food> getLimitFoods(String search, int count, int start);

    @Query("select * from Food where id = :id")
    Food getById(long id);

    @Query("select * from Food")
    List<Food> getAll();

}

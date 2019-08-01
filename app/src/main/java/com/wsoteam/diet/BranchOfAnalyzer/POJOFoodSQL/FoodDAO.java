package com.wsoteam.diet.BranchOfAnalyzer.POJOFoodSQL;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface FoodDAO {

    @Insert
    void insertAll(Food... foods);

    @Insert
    void insert(Food food);

    @Query("select * from Food where name like :search limit :end offset :start")
    List<Food> searchFullMatchWord(String search, int end, int start);

    @Query("select * from Food where fullInfo like :search limit :count offset :start")
    List<Food> searchOneWord(String search, int count, int start);

    @Query("select * from Food where fullInfo like :firstWord and fullInfo like :secondWord limit :count offset :start")
    List<Food> searchTwoWord(String firstWord, String secondWord, int count, int start);

    @Query("select * from Food where fullInfo like :firstWord and fullInfo like :secondWord and fullInfo " +
            "like :thirdWord limit :count offset :start")
    List<Food> searchThreeWord(String firstWord, String secondWord, String thirdWord, int count, int start);

    @Query("select * from Food where fullInfo like :firstWord and fullInfo like :secondWord " +
            "and fullInfo like :thirdWord and fullInfo like :fourthWord limit :count offset :start")
    List<Food> searchFourWord(String firstWord, String secondWord, String thirdWord, String fourthWord, int count, int start);

    @Query("select * from Food where fullInfo like :firstWord and fullInfo like :secondWord " +
            "and fullInfo like :thirdWord and fullInfo like :fourthWord and fullInfo like :fifthWord limit :count offset :start")
    List<Food> searchFiveWord(String firstWord, String secondWord, String thirdWord, String fourthWord, String fifthWord, int count, int start);

    @Query("select * from Food where id = :id")
    Food getById(long id);

    @Query("select * from Food")
    List<Food> getAll();

}

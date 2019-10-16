package com.wsoteam.diet.presentation.search.basket.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface BasketDAO {

  @Insert
  void insert(BasketEntity food);

  @Query("select * from BasketEntity")
  List<BasketEntity> getAll();
}

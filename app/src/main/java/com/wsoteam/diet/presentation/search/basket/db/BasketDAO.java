package com.wsoteam.diet.presentation.search.basket.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface BasketDAO {

  @Insert
  void insert(BasketEntity food);

  @Query("select * from BasketEntity")
  List<BasketEntity> getAll();


  @Query("DELETE FROM basketentity WHERE serverId = :id AND deepId = :deepId")
  void deleteById(int id, int deepId);

  @Delete
  void delete(BasketEntity food);
}

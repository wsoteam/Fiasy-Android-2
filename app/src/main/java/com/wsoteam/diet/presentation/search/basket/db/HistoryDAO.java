package com.wsoteam.diet.presentation.search.basket.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface HistoryDAO {
  @Insert
  void insert(HistoryEntity historyEntity);

  @Query("select * from HistoryEntity")
  List<HistoryEntity> getAll();

  @Query("DELETE FROM HistoryEntity")
  void deleteAll();
}

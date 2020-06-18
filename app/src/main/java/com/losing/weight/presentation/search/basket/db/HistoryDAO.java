package com.losing.weight.presentation.search.basket.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface HistoryDAO {
  @Insert
  void insert(HistoryEntity historyEntity);

  @Insert
  void insertMany(List<HistoryEntity> historyEntities);

  @Query("select * from HistoryEntity")
  List<HistoryEntity> getAll();

  @Query("DELETE FROM HistoryEntity")
  void deleteAll();
}

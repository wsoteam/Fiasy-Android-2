package com.losing.weight.presentation.search.basket.db;

import androidx.room.Entity;
import com.losing.weight.common.networking.food.ISearchResult;

@Entity
public class HistoryEntity extends BasketEntity implements ISearchResult {

  public HistoryEntity() {
  }

  public HistoryEntity(BasketEntity basketEntity) {
    super(basketEntity.getServerId(), basketEntity.getDeepId(),
        basketEntity.getName(),
        basketEntity.getBrand(), basketEntity.isLiquid(), basketEntity.getCountPortions(),
        basketEntity.getKilojoules(), basketEntity.getCalories(), basketEntity.getProteins(),
        basketEntity.getCarbohydrates(), basketEntity.getSugar(), basketEntity.getFats(),
        basketEntity.getSaturatedFats(), basketEntity.getMonoUnSaturatedFats(),
        basketEntity.getPolyUnSaturatedFats(),
        basketEntity.getCholesterol(), basketEntity.getCellulose(), basketEntity.getSodium(),
        basketEntity.getPottassium(), basketEntity.getEatingType(), basketEntity.getNamePortion(), basketEntity.getSizePortion());
  }

  @Override
  public void swapId(BasketEntity basketEntity) {
    super.swapId(basketEntity);
  }
}

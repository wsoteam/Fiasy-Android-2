package com.wsoteam.diet.common.networking.food;

import com.wsoteam.diet.common.networking.food.POJO.FoodResult;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface FoodResultAPI {
    @GET("/api/v1/products/")
    Observable<FoodResult> getResponse(@Query("limit") int limit, @Query("offset") int offset, @Query("search") String search);
}

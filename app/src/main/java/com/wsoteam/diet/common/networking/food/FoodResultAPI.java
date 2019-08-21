package com.wsoteam.diet.common.networking.food;

import com.wsoteam.diet.IPCheck.IPCheckObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface FoodResultAPI {
    @GET("/api/v1/products/")
    Call<FoodResult> getResponse(@Query("limit") int limit, @Query("offset") int offset,  @Query("search") String search);
}

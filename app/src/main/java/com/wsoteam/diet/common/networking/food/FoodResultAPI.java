package com.wsoteam.diet.common.networking.food;

import com.wsoteam.diet.common.networking.food.POJO.FoodResult;

import com.wsoteam.diet.common.networking.food.suggest.Suggest;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface FoodResultAPI {
    @GET("/api/v1/search/")
    Observable<FoodResult> getResponse(@Query("limit") int limit, @Query("offset") int offset, @Query("search") String search);

    @GET("/api/v1/search/suggest/")
    Observable<Suggest> getSuggestions(@Query("name_suggest__completion") String search);

    @GET("/api/v1/search/")
    Observable<FoodResult> search(@Query("search") String search, @Query("page") int page);
}

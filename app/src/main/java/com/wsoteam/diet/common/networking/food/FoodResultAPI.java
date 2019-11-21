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

    @GET("/api/v1/search/")
    Observable<FoodResult> searchNoBrand(@Query("search") String search , @Query("page") int page, @Query("brand") String empty);

    @GET("/api/v1/en/search/")
    Observable<FoodResult> searchEn(@Query("search") String search, @Query("page") int page);

    @GET("/api/v1/en/search/")
    Observable<FoodResult> searchEnNoBrand(@Query("search") String search , @Query("page") int page, @Query("brand") String empty);

    @GET("/api/v1/de/search/")
    Observable<FoodResult> searchDe(@Query("search") String search, @Query("page") int page);

    @GET("/api/v1/de/search/")
    Observable<FoodResult> searchDeNoBrand(@Query("search") String search , @Query("page") int page, @Query("brand") String empty);

    @GET("/api/v1/es/search/")
    Observable<FoodResult> searchEs(@Query("search") String search, @Query("page") int page);

    @GET("/api/v1/es/search/")
    Observable<FoodResult> searchEsNoBrand(@Query("search") String search , @Query("page") int page, @Query("brand") String empty);

    @GET("/api/v1/pt/search/")
    Observable<FoodResult> searchPt(@Query("search") String search, @Query("page") int page);

    @GET("/api/v1/pt/search/")
    Observable<FoodResult> searchPtNoBrand(@Query("search") String search , @Query("page") int page, @Query("brand") String empty);

}

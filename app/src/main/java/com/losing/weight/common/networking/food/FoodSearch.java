package com.losing.weight.common.networking.food;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.losing.weight.BuildConfig;
import com.losing.weight.Config;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FoodSearch {
    private static FoodSearch instance = new FoodSearch();
    private FoodResultAPI api;


    public static FoodSearch getInstance(){
        return instance;
    }

    public FoodSearch() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new HttpLoggingInterceptor().setLevel((BuildConfig.DEBUG) ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE))
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.CURRENT_SEARCH_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        api = retrofit.create(FoodResultAPI.class);
    }

    public FoodResultAPI getFoodSearchAPI(){
        return api;
    }
}
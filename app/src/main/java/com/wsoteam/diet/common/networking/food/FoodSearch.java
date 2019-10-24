package com.wsoteam.diet.common.networking.food;

import android.util.Log;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.wsoteam.diet.BuildConfig;
import com.wsoteam.diet.common.networking.food.POJO.FoodResult;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FoodSearch {
    //private static final String BASE_URL = "http://116.203.193.111:8000";
    private static final String BASE_URL = "http://78.47.35.187:8000";
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
                .baseUrl(BASE_URL)
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
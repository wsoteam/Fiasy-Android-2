package com.wsoteam.diet.common.networking.food;

import android.util.Log;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FoodSearch {
    private static final String BASE_URL = "http://116.203.193.111:8000";

    public static void getSearchResult() {
        Log.e("LOL", "start");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        FoodResultAPI foodSearchAPI = retrofit.create(FoodResultAPI.class);
        Observable<FoodResult> result = foodSearchAPI.getResponse(1, 0, "молоко");


        result
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(t -> Log.e("LOL", String.valueOf(t.getCount())));
    }

}

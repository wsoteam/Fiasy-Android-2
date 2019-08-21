package com.wsoteam.diet.common.networking.food;

import android.util.Log;

import com.wsoteam.diet.IPCheck.IPCheckObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FoodSearch {
    private static final String BASE_URL = "http://116.203.193.111:8000";

    public static void getSearchResult(){
        Log.e("LOL", "start");
        Retrofit retrofit = new Retrofit.Builder().
                baseUrl(BASE_URL).
                addConverterFactory(GsonConverterFactory.create()).
                build();

        FoodResultAPI foodSearchAPI = retrofit.create(FoodResultAPI.class);
        Call<FoodResult> result = foodSearchAPI.getResponse(1, 0, "молоко");
        result.enqueue(new Callback<FoodResult>() {
            @Override
            public void onResponse(Call<FoodResult> call, Response<FoodResult> response) {
                Log.e("LOL", String.valueOf(response.body().getCount()));

            }

            @Override
            public void onFailure(Call<FoodResult> call, Throwable t) {

            }
        });
    }

}

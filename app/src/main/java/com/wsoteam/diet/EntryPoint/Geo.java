package com.wsoteam.diet.EntryPoint;

import android.content.Context;

import com.wsoteam.diet.Config;
import com.wsoteam.diet.IPCheck.IPCheckApi;
import com.wsoteam.diet.IPCheck.IPCheckObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Geo {
    private static final String BASE_URL = "http://ip-api.com/";

    public static void getGeo(Context context){
        Retrofit retrofit = new Retrofit.Builder().
                baseUrl(BASE_URL).
                addConverterFactory(GsonConverterFactory.create()).
                build();

        IPCheckApi checkApi = retrofit.create(IPCheckApi.class);
        Call<IPCheckObject> objectCall = checkApi.getResponse();
        objectCall.enqueue(new Callback<IPCheckObject>() {
            @Override
            public void onResponse(Call<IPCheckObject> call, Response<IPCheckObject> response) {
                saveGeo(context, response.body().getCountryCode());
            }

            @Override
            public void onFailure(Call<IPCheckObject> call, Throwable t) {

            }
        });
    }

    private static void saveGeo(Context context , String geoCode){
        context.getSharedPreferences(Config.GEO, Context.MODE_PRIVATE).
                edit().putString(Config.GEO, geoCode).
                commit();
    }
}

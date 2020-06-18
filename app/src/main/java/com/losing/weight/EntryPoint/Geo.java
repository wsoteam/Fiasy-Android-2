package com.losing.weight.EntryPoint;

import android.content.Context;
import androidx.annotation.NonNull;

import com.amplitude.api.Amplitude;
import com.amplitude.api.Identify;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.losing.weight.ABConfig;
import com.losing.weight.IPCheck.IPCheckApi;
import com.losing.weight.IPCheck.IPCheckObject;
import com.losing.weight.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;

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
                /*if (response.body().getCountryCode().equals(Config.UA_GEO)){
                    setABTestConfig(ABConfig.C_VERSION, context);
                }else {
                    getABVersion(context);
                }*/

            }

            @Override
            public void onFailure(Call<IPCheckObject> call, Throwable t) {

            }
        });
    }

    private static void getABVersion(Context context) {
        FirebaseRemoteConfig firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        firebaseRemoteConfig.setDefaults(R.xml.default_config);

        firebaseRemoteConfig.fetch(3600).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    firebaseRemoteConfig.activateFetched();
                    Amplitude.getInstance().logEvent("norm_ab");
                } else {
                    Amplitude.getInstance().logEvent("crash_ab");
                }
                setABTestConfig(firebaseRemoteConfig.getString(ABConfig.REQUEST_STRING), context);
            }
        });
    }

    private static void setABTestConfig(String responseString, Context context) {
        Identify abStatus = new Identify().set(ABConfig.AB_VERSION, responseString);
        Amplitude.getInstance().identify(abStatus);
        context.getSharedPreferences(ABConfig.KEY_FOR_SAVE_STATE, MODE_PRIVATE).
                edit().putString(ABConfig.KEY_FOR_SAVE_STATE, responseString).
                commit();
    }
}

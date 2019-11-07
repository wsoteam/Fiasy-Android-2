package com.wsoteam.diet.utils;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.wsoteam.diet.BuildConfig;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkService {

  private static NetworkService mInstance;
  private static final String BASE_URL = "http://78.47.35.187:8000";
  private Retrofit mRetrofit;

  private NetworkService() {
    OkHttpClient okHttpClient = new OkHttpClient.Builder()
        .addInterceptor(new HttpLoggingInterceptor().setLevel((BuildConfig.DEBUG) ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE))
        .build();

    mRetrofit = new Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build();
  }

  public static NetworkService getInstance() {
    if (mInstance == null) {
      mInstance = new NetworkService();
    }
    return mInstance;
  }

  public ApiRequest getApi() {
    return mRetrofit.create(ApiRequest.class);
  }
}

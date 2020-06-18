package com.losing.weight.IPCheck;

import retrofit2.Call;
import retrofit2.http.GET;

public interface IPCheckApi {

    @GET("json")
    Call<IPCheckObject> getResponse();
}

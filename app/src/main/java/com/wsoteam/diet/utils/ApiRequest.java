package com.wsoteam.diet.utils;

import com.wsoteam.diet.model.*;
import io.reactivex.Observable;
import retrofit2.http.GET;

public interface ApiRequest {

  @GET("/api/v1/articles/")
  Observable<ApiResult<Article>> getArticles();

  @GET("/api/v1/authors")
  Observable<ApiResult<Author>> getAuthors();


}

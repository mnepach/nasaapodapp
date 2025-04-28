package com.example.nasaapodapp.data.remote;

import com.example.nasaapodapp.data.model.ApodResponse;

import java.util.List;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NasaApiService {
    @GET("planetary/apod")
    Single<ApodResponse> getApod(@Query("api_key") String apiKey);

    @GET("planetary/apod")
    Single<List<ApodResponse>> getApodList(
            @Query("api_key") String apiKey,
            @Query("count") int count);
}
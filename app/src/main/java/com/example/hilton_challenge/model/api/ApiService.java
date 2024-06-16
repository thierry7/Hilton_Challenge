package com.example.hilton_challenge.model.api;

import com.example.hilton_challenge.model.GeolocationResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    @GET("json")
    Call<GeolocationResponse> fetchGeolocation(@Query("query") String ipAddress);
}

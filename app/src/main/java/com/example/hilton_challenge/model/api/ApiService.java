package com.example.hilton_challenge.model.api;

import com.example.hilton_challenge.model.GeolocationResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    @GET("json/{query}")
    Call<GeolocationResponse> fetchGeolocation(@Path("query") String ipAddress);
}

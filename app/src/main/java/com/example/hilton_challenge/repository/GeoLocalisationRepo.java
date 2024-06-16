package com.example.hilton_challenge.repository;

import static android.content.ContentValues.TAG;

import android.util.Log;

import com.example.hilton_challenge.model.Geolocation;
import com.example.hilton_challenge.model.GeolocationResponse;
import com.example.hilton_challenge.model.api.ApiService;
import com.example.hilton_challenge.model.local.MyDao;

import javax.inject.Inject;

public class GeoLocalisationRepo {
    private final ApiService api;
    private final MyDao dao;

    @Inject
    public GeoLocalisationRepo(ApiService api, MyDao dao) {
        this.api = api;
        this.dao = dao;
    }

    public Geolocation getLocationByIp(String ip) {

        Geolocation localData = dao.getGeolocationByQuery(ip);
        if (localData != null && !isDataStale(localData)) {
            return localData;
        } else {
            try {
                Log.d(TAG, "Fetching from API for IP: " + ip);
                retrofit2.Response<GeolocationResponse> response = api.fetchGeolocation(ip).execute();
                if (response.isSuccessful() && response.body() != null) {
                    GeolocationResponse responseBody = response.body();
                    if (responseBody.getStatus().equals("success") && ip.equals(responseBody.getQuery())) {
                        Geolocation newData = Geolocation.fromResponse(responseBody);
                        if (!dao.doesGeolocationExist(ip)) {
                            dao.insertGeolocation(newData);
                        }
                        return newData;
                    } else {
                        Log.e(TAG, "IP mismatch or unsuccessful status. Requested IP: " + ip + ", Response IP: " + responseBody.getQuery());

                    }
                } else {
                    Log.e(TAG, "Network request failed for IP: " + ip + ". Response code: " + response.code());

                }
                return null;

            } catch (Exception e) {
                Log.e(TAG, "Network request failed for IP: ", e);
                return null;
            }
        }
    }

    private boolean isDataStale(Geolocation localData) {
        long currentTime = System.currentTimeMillis();
        return currentTime - localData.getTimestamp() > 5 * 60 * 1000;
    }

}

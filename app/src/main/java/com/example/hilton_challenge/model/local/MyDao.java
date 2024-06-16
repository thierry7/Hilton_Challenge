package com.example.hilton_challenge.model.local;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.hilton_challenge.model.Geolocation;

@Dao
public interface MyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertGeolocation(Geolocation location);

    @Query("SELECT * FROM geolocation WHERE `query` = :ip")
    public Geolocation getGeolocationByQuery(String ip);

    @Query("SELECT EXISTS(SELECT 1 FROM Geolocation WHERE `query` = :ip)")
    boolean doesGeolocationExist(String ip);

}

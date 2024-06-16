package com.example.hilton_challenge.model.local;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.hilton_challenge.model.Geolocation;
import com.example.hilton_challenge.model.local.MyDao;

@Database(entities = {Geolocation.class}, version = 1, exportSchema = false)
public abstract class GeoDataBase extends RoomDatabase {
    public abstract MyDao geolocationDao();

}

package com.example.hilton_challenge.di;


import android.content.Context;

import androidx.room.Room;

import com.example.hilton_challenge.model.local.GeoDataBase;
import com.example.hilton_challenge.model.local.MyDao;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class DatabaseModule {
    @Provides
    @Singleton
    public static GeoDataBase provideDatabase(@ApplicationContext Context context) {
        return Room.databaseBuilder(context, GeoDataBase.class, "geo-db").build();
    }

    @Provides
    public static MyDao provideGeolocationDao(GeoDataBase database) {
        return database.geolocationDao();
    }
}

package com.example.hilton_challenge.di;

import com.example.hilton_challenge.model.api.ApiService;
import com.example.hilton_challenge.repository.GeoLocalisationRepo;
import com.example.hilton_challenge.model.local.MyDao;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class RepoModule {
    @Provides
    @Singleton
    public GeoLocalisationRepo provideGeoLocalisationRepo(ApiService apiService, MyDao dao) {
        return new GeoLocalisationRepo(apiService, dao);
    }
}

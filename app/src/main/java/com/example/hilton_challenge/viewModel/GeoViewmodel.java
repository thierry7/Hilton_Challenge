package com.example.hilton_challenge.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.hilton_challenge.repository.GeoLocalisationRepo;
import com.example.hilton_challenge.model.Geolocation;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class GeoViewmodel extends ViewModel {

    private final GeoLocalisationRepo myRepo;
    private final MutableLiveData<Geolocation> geolocation = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    @Inject
    public GeoViewmodel(GeoLocalisationRepo myRepo) {
        this.myRepo = myRepo;
    }

    public LiveData<Geolocation> getGeolocation(){
        return geolocation;
    }

    public LiveData<String> getErrorMessage() {return errorMessage;}

    public void fetchLocation(String ip){
        new Thread(
                ()->{
                    Geolocation data = myRepo.getLocationByIp(ip);
                    if(data != null){
                        geolocation.postValue(data);
                    }else{
                        errorMessage.postValue("Failed to fetch data for IP:  " + ip);
                    }

                }
        ).start();
    }
}

package com.example.hilton_challenge.viewModel;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Observer;

import com.example.hilton_challenge.model.Geolocation;
import com.example.hilton_challenge.repository.GeoLocalisationRepo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class GeoViewmodelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private GeoLocalisationRepo geoLocalisationRepo;

    @Mock
    private Observer<Geolocation> geolocationObserver;

    @Mock
    private Observer<String> errorObserver;

    @InjectMocks
    private GeoViewmodel geoViewmodel;

    private ExecutorService executorService;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        geoViewmodel = new GeoViewmodel(geoLocalisationRepo);
        geoViewmodel.getGeolocation().observeForever(geolocationObserver);
        geoViewmodel.getErrorMessage().observeForever(errorObserver);
        executorService = Executors.newSingleThreadExecutor();
    }

    @Test
    public void fetchLocation_success() throws InterruptedException {
        Geolocation geolocation = new Geolocation("8.8.8.8", "US", "California", "Mountain View", 37.386, -122.0838);
        when(geoLocalisationRepo.getLocationByIp(anyString())).thenReturn(geolocation);

        CountDownLatch latch = new CountDownLatch(1);
        executorService.submit(() -> {
            geoViewmodel.fetchLocation("8.8.8.8");
            latch.countDown();
        });

        latch.await(5, TimeUnit.SECONDS);

        // Assert
        System.out.println("Mock interactions: " + Mockito.mockingDetails(geoLocalisationRepo).getInvocations());
        verify(geoLocalisationRepo).getLocationByIp("8.8.8.8");
        verify(geolocationObserver).onChanged(geolocation);
        verify(errorObserver, never()).onChanged(anyString());
    }

    @Test
    public void fetchLocation_failure() throws InterruptedException {
        when(geoLocalisationRepo.getLocationByIp(anyString())).thenReturn(null);

        CountDownLatch latch = new CountDownLatch(1);
        executorService.submit(() -> {
            geoViewmodel.fetchLocation("8.8.8.8");
            latch.countDown();
        });

        latch.await(5, TimeUnit.SECONDS);

        // Assert
        verify(geoLocalisationRepo).getLocationByIp("8.8.8.8");
        verify(geolocationObserver, never()).onChanged(any());
        verify(errorObserver).onChanged("Failed to fetch data for IP:  8.8.8.8");
    }
}

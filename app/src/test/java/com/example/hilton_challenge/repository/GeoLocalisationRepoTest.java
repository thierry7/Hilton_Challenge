package com.example.hilton_challenge.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.example.hilton_challenge.model.Geolocation;
import com.example.hilton_challenge.model.GeolocationResponse;
import com.example.hilton_challenge.model.api.ApiService;
import com.example.hilton_challenge.model.local.MyDao;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GeoLocalisationRepoTest {

    @Mock
    private ApiService apiService;
    @Mock
    private MyDao myDao;

    private GeoLocalisationRepo repo;

    private MockWebServer mockWebServer;
    private Retrofit retrofit;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        retrofit = new Retrofit.Builder()
                .baseUrl(mockWebServer.url("/"))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);
        repo = new GeoLocalisationRepo(apiService, myDao);
    }

    @After
    public void tearDown() throws Exception {
        mockWebServer.shutdown();
    }

    /*** Checks if the location is retrieved from the cache */

    @Test
    public void testGetLocationByIp_CacheHit() {
        Geolocation mockLocation = new Geolocation();
        mockLocation.setTimestamp(System.currentTimeMillis());
        when(myDao.getGeolocationByQuery(anyString())).thenReturn(mockLocation);

        Geolocation result = repo.getLocationByIp("8.8.8.8");

        assertEquals(mockLocation, result);
    }

    /*** Checks if the location is fetched from the API when it's not in the cache. */
    @Test
    public void testGetLocationByIp_CacheMiss() {
        GeolocationResponse mockResponse = new GeolocationResponse();
        mockResponse.setStatus("success");
        mockResponse.setQuery("8.8.8.8");

        mockWebServer.enqueue(new MockResponse()
                .setBody("{\"status\":\"success\",\"query\":\"8.8.8.8\",\"country\":\"United States\",\"countryCode\":\"US\",\"region\":\"CA\",\"regionName\":\"California\",\"city\":\"Mountain View\",\"zip\":\"94043\",\"lat\":37.422,\"lon\":-122.084,\"timezone\":\"America/Los_Angeles\",\"isp\":\"Google LLC\",\"org\":\"Google LLC\",\"as\":\"AS15169 Google LLC\"}")
                .setResponseCode(200));

        Geolocation mockLocation = new Geolocation();
        when(myDao.getGeolocationByQuery(anyString())).thenReturn(null);
        when(myDao.doesGeolocationExist(anyString())).thenReturn(false);

        Geolocation result = repo.getLocationByIp("8.8.8.8");

        assertEquals("8.8.8.8", result.getQuery());
        assertEquals("United States", result.getCountry());
        assertEquals("Mountain View", result.getCity());
    }

    /** Simulates a network error to test the error handling */

    @Test
    public void testGetLocationByIp_NetworkError() {
        when(myDao.getGeolocationByQuery(anyString())).thenReturn(null);

        Geolocation result = repo.getLocationByIp("8.8.8.8");

        assertNull(result);
    }

    /** Tests the behavior when the IP in the response doesn't match the requested IP.*/
    @Test
    public void testGetLocationByIp_IpMismatch() {
        GeolocationResponse mockResponse = new GeolocationResponse();
        mockResponse.setStatus("success");
        mockResponse.setQuery("8.8.4.4");

        mockWebServer.enqueue(new MockResponse()
                .setBody("{\"status\":\"success\",\"query\":\"8.8.4.4\",\"country\":\"United States\",\"countryCode\":\"US\",\"region\":\"CA\",\"regionName\":\"California\",\"city\":\"Mountain View\",\"zip\":\"94043\",\"lat\":37.422,\"lon\":-122.084,\"timezone\":\"America/Los_Angeles\",\"isp\":\"Google LLC\",\"org\":\"Google LLC\",\"as\":\"AS15169 Google LLC\"}")
                .setResponseCode(200));

        when(myDao.getGeolocationByQuery(anyString())).thenReturn(null);

        Geolocation result = repo.getLocationByIp("8.8.8.8");

        assertNull(result);
    }
}

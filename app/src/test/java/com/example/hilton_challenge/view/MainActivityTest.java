package com.example.hilton_challenge.view;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.example.hilton_challenge.MyApplication;
import com.example.hilton_challenge.R;
import com.example.hilton_challenge.viewModel.GeoViewmodel;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowToast;

import dagger.hilt.android.testing.HiltAndroidTest;

@HiltAndroidTest
@RunWith(RobolectricTestRunner.class)
@Config(application = MyApplication.class, manifest = Config.NONE, sdk = 28)
public class MainActivityTest {

    private MainActivity mainActivity;

    @Mock
    private GeoViewmodel viewModel;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mainActivity = Robolectric.buildActivity(MainActivity.class).create().get();
        mainActivity.viewModel = viewModel;
    }

    @Test
    public void whenValidIpAddressEntered_thenFetchLocation() {
        // Given
        String ipAddress = "192.168.0.1";
        EditText ipAddressEditText = mainActivity.findViewById(R.id.ipAddressEditText);
        ipAddressEditText.setText(ipAddress);
        Button searchButton = mainActivity.findViewById(R.id.searchButton);

        // When
        searchButton.performClick();

        // Then
        Mockito.verify(viewModel).fetchLocation(ipAddress);
    }

    @Test
    public void whenErrorMessageReceived_thenShowToast() {
        // Given
        String errorMessage = "Invalid IP address";
        MutableLiveData<String> errorMessageLiveData = new MutableLiveData<>();
        errorMessageLiveData.setValue(errorMessage);
        Mockito.when(viewModel.getErrorMessage()).thenReturn(errorMessageLiveData);

        // When
        mainActivity.displayErrorMessage(errorMessage);

        // Then
        // Using Robolectric to verify the Toast
        Toast latestToast = ShadowToast.getLatestToast();
        assertNotNull(latestToast);
        assertEquals(Toast.LENGTH_LONG, latestToast.getDuration());
        assertEquals(errorMessage, ShadowToast.getTextOfLatestToast());
    }
}

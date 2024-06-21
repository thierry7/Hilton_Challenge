package com.example.hilton_challenge.view;

import android.os.Bundle;
import android.util.Log;

import com.example.hilton_challenge.R;
import com.example.hilton_challenge.model.Geolocation;
import com.example.hilton_challenge.viewModel.GeoViewmodel;

import androidx.appcompat.app.AppCompatActivity;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    GeoViewmodel viewModel;
    private EditText ipAddressEditText;
    private TextView countryTextView, cityTextView, regionTexView, zipTextView, ipTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate: Initializing views");
        ipAddressEditText = findViewById(R.id.ipAddressEditText);
        Button searchButton = findViewById(R.id.searchButton);
        ipTextView = findViewById(R.id.ipTextView);
        countryTextView = findViewById(R.id.countryTextView);
        cityTextView = findViewById(R.id.cityTextView);
        regionTexView = findViewById(R.id.regionTextView);
        zipTextView = findViewById(R.id.zipTextView);

        Log.d(TAG, "onCreate: Setting up ViewModel");
        viewModel = new ViewModelProvider(this).get(GeoViewmodel.class);

        Log.d(TAG, "onCreate: Observing LiveData");
        viewModel.getGeolocation().observe(this, this::displayResult);

        viewModel.getErrorMessage().observe(this, errorMessage -> {
            if (errorMessage != null) {
                Log.e(TAG, "onCreate: Error message received: " + errorMessage);
                displayErrorMessage(errorMessage);
            }
        });

        searchButton.setOnClickListener(view -> {
            String ipAddress = ipAddressEditText.getText().toString();
            Log.d(TAG, "onCreate: Search button clicked with IP: " + ipAddress);
            if (isValidIp(ipAddress)) {
                Log.d(TAG, "onCreate: Valid IP address");
                viewModel.fetchLocation(ipAddress);
                ipAddressEditText.setText(null);
            } else {
                Log.w(TAG, "onCreate: Invalid IP address");
                Toast.makeText(MainActivity.this, "Invalid IP address", Toast.LENGTH_LONG).show();
            }
        });
    }

    private boolean isValidIp(String ip) {
        String ipPattern =
                "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";
        boolean isValid = ip.matches(ipPattern);
        Log.d(TAG, "isValidIp: " + ip + " is " + (isValid ? "valid" : "invalid"));
        return isValid;
    }

    void displayResult(Geolocation geolocation) {
        if (geolocation != null) {
            Log.d(TAG, "displayResult: Displaying geolocation data");
            ipTextView.setText(getString(R.string.requested_ip) + geolocation.getQuery());
            countryTextView.setText(getString(R.string.country) + geolocation.getCountry());
            cityTextView.setText(getString(R.string.city) + geolocation.getCity());
            regionTexView.setText(getString(R.string.region) + geolocation.getRegionName());
            zipTextView.setText(getString(R.string.zip_code) + geolocation.getZip());
        } else {
            Log.w(TAG, "displayResult: Geolocation data is null");
        }
    }
    void displayErrorMessage(String errorMessage){
        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();

    }
}

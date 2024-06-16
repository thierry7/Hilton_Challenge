package com.example.hilton_challenge.view;

import android.os.Bundle;

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


    private GeoViewmodel viewModel;
    private EditText ipAddressEditText;
    private TextView countryTextView, cityTextView, regionTexView, zipTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ipAddressEditText = findViewById(R.id.ipAddressEditText);
        Button searchButton = findViewById(R.id.searchButton);
        countryTextView = findViewById(R.id.countryTextView);
        cityTextView = findViewById(R.id.cityTextView);
        regionTexView=  findViewById(R.id.regionTextView);
        zipTextView = findViewById(R.id.zipTextView);

        viewModel = new ViewModelProvider(this).get(GeoViewmodel.class);

        viewModel.getGeolocation().observe(this, this::displayResult);

        viewModel.getErrorMessage().observe(this, errorMessage -> {
            if (errorMessage != null) {
                Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
            }
        });

        searchButton.setOnClickListener(view -> {
            String ipAddress = ipAddressEditText.getText().toString();
            if (isValidIp(ipAddress)) {
                viewModel.fetchLocation(ipAddress);
                ipAddressEditText.setText(null);
            } else {
                Toast.makeText(MainActivity.this, "Invalid IP address", Toast.LENGTH_LONG).show();
            }
        });
    }

    private boolean isValidIp(String ip) {
        String ipPattern =
                "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";
        return ip.matches(ipPattern);
    }
    private void displayResult(Geolocation geolocation) {
        if (geolocation != null) {
            countryTextView.setText(geolocation.getCountry());
            cityTextView.setText(geolocation.getCity());
            regionTexView.setText(geolocation.getRegionName());
            zipTextView.setText(geolocation.getZip());
        } else {
            countryTextView.setText("No data found");
        }
    }



}
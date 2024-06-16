package com.example.hilton_challenge.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "geolocation")
public class Geolocation {
    @PrimaryKey(autoGenerate = true)
    private long timestamp;
    private String country;
    private String countryCode;
    private String regionName;
    private String city;
    private String zip;
    private double lat;
    private double lon;
    private String query;

    // Default constructor required by Room
    public Geolocation() {
    }

    public Geolocation(String s, String us, String california, String mountainView, double v, double v1) {
        query =s;
        country = us;
        city = california;
        regionName = mountainView;
        lat = v;
        lon = v1;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public static Geolocation fromResponse(GeolocationResponse response) {
        Geolocation geolocation = new Geolocation();
        geolocation.setQuery(response.getQuery());
        geolocation.setCountry(response.getCountry());
        geolocation.setCountryCode(response.getCountryCode());
        geolocation.setRegionName(response.getRegionName());
        geolocation.setCity(response.getCity());
        geolocation.setZip(response.getZip());
        geolocation.setLat(response.getLat());
        geolocation.setLon(response.getLon());
        geolocation.setTimestamp(System.currentTimeMillis()); // Set current timestamp
        return geolocation;
    }
}
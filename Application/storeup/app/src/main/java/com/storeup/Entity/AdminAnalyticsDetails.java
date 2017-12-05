package com.storeup.Entity;

public class AdminAnalyticsDetails {

    private String latitude;

    private String longitude;

    public AdminAnalyticsDetails(String latitude, String longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public  String getLongitude() {
        return longitude;
    }
}

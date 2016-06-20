package com.ciaran.upskill.travelagency.domain;

public class CoOrdindate {
    private double longitude;
    private double latitude;

    public CoOrdindate(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }


    public double getDistance(CoOrdindate location) {
        return 0.0;
    }
}

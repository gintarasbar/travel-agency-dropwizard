package com.ciaran.upskill.travelagency.domain;

public class CoOrdinate {
    private double longitude;
    private double latitude;

    public CoOrdinate(double latitude, double longitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }


    public double getDistance(CoOrdinate coOrdindate) {
        double latitudeDiff = coOrdindate.getLatitude()-this.latitude;
        double longitudeDiff = coOrdindate.getLongitude()-this.longitude;
        return Math.sqrt((latitudeDiff*latitudeDiff)+(longitudeDiff*longitudeDiff));
    }

    @Override
    public String toString() {
        return latitude + "," + longitude;
    }
}

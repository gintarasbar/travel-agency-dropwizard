package com.ciaran.upskill.travelagency.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

import static java.lang.Math.PI;
import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.round;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;

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
        double earthRadius = 6373;
        Double latDistance = Math.toRadians(coOrdindate.getLatitude() - this.getLatitude());
        Double lonDistance = Math.toRadians(coOrdindate.getLongitude() - this.getLongitude());
        Double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(this.getLatitude())) * Math.cos(Math.toRadians(coOrdindate.getLatitude()))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = earthRadius * c;
        BigDecimal bd = new BigDecimal(distance).setScale(2, RoundingMode.HALF_EVEN);
        return bd.doubleValue();
    }

    @Override
    public String toString() {
        return latitude + "," + longitude;
    }
}

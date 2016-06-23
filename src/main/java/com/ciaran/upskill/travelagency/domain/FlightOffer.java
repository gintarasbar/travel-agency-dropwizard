package com.ciaran.upskill.travelagency.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.UUID;

public class FlightOffer {

    @JsonProperty
    private UUID id;
    @JsonProperty
    private double price;
    @JsonProperty
    private String flightOriginId;
    @JsonProperty
    private String flightDestinationId;

    private double distance;
    @JsonProperty
    private String airline;
    @JsonProperty
    AgencyDate[] flightDates;

    public FlightOffer(UUID id, double price, String flightOriginId, String flightDestinationId, String airline, AgencyDate[] flightDates) {
        this.id = id;
        this.price = price;
        this.flightOriginId = flightOriginId;
        this.flightDestinationId = flightDestinationId;
        this.airline = airline;
        this.distance = 0.0;
        this.flightDates = flightDates;
    }

    public UUID getId() {
        return id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getFlightOriginId() {
        return flightOriginId;
    }

    public String getFlightDestinationId() {
        return flightDestinationId;
    }

    public double getDistance() {
        return distance;
    }

    public String getAirline() {
        return airline;
    }

    public AgencyDate[] getFlightDates() {
        return flightDates;
    }

    public void setFlightDates(AgencyDate[] flightDates) {
        this.flightDates = flightDates;
    }

    @Override
    public String toString() {
        String flightDatesString = null;
        int i = 0;
        for(AgencyDate date : flightDates){
            if(flightDatesString == null){
                flightDatesString = "[" + flightDates[i].toString();
            } else {
                flightDatesString = flightDatesString + ";" + flightDates[i].toString();
            }
            i++;
        }
        flightDatesString = flightDatesString+"]";
        return id + "," + price + "," + flightOriginId + "," + flightDestinationId + "," + airline + "," + flightDatesString;
    }
}

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
    private City flightOrigin;
    @JsonProperty
    private City flightDestination;
    private double distance;
    @JsonProperty
    private String airline;
    @JsonProperty
    Date[] flightDates;

    public FlightOffer(double price, City flightOrigin, City flightDestination, String airline, Date[] flightDates) {
        this.id = UUID.randomUUID();
        this.price = price;
        this.flightOrigin = flightOrigin;
        this.flightDestination = flightDestination;
        this.airline = airline;
        this.distance = flightOrigin.getLocation().getDistance(flightDestination.getLocation());
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

    public City getFlightOrigin() {
        return flightOrigin;
    }

    public City getFlightDestination() {
        return flightDestination;
    }

    public double getDistance() {
        return distance;
    }

    public String getAirline() {
        return airline;
    }

    public Date[] getFlightDates() {
        return flightDates;
    }

    public void setFlightDates(Date[] flightDates) {
        this.flightDates = flightDates;
    }
}

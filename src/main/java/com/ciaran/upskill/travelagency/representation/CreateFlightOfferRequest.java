package com.ciaran.upskill.travelagency.representation;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

public class CreateFlightOfferRequest {

    @JsonProperty
    @NotNull
    private double price;

    @JsonProperty
    @NotNull
    private String flightOrigin;


    @JsonProperty
    @NotNull
    private String flightDestination;

    @JsonProperty
    private String airline;

    @JsonProperty
    private String[] flightDates;

    @JsonCreator
    public CreateFlightOfferRequest(@JsonProperty("price") double price,
                                    @JsonProperty("flightOrigin") String flightOrigin,
                                    @JsonProperty("flightDestination") String flightDestination,
                                    @JsonProperty("airline") String airline,
                                    @JsonProperty("flightDates") String[] flightDates) {
        this.price = price;
        this.flightOrigin = flightOrigin;
        this.flightDestination = flightDestination;
        this.airline = airline;
        this.flightDates = flightDates;
    }

    public double getPrice() {
        return price;
    }

    public String getFlightOrigin() {
        return flightOrigin;
    }

    public String getFlightDestination() {
        return flightDestination;
    }

    public String getAirline() {
        return airline;
    }

    public String[] getFlightDates() {
        return flightDates;
    }
}

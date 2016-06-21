package com.ciaran.upskill.travelagency.representation;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

public class UpdateFlightOfferRequest {

    @JsonProperty
    private double price;

    @JsonProperty
    private String[] flightDates;

    public UpdateFlightOfferRequest(@JsonProperty("price") double price,
                                    @JsonProperty("flightDates") String[] flightDates) {
        this.price = price;
        this.flightDates = flightDates;
    }

    public double getPrice() {
        return price;
    }

    public String[] getFlightDates() {
        return flightDates;
    }
}

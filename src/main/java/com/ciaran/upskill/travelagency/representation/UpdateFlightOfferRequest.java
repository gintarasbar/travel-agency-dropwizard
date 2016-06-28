package com.ciaran.upskill.travelagency.representation;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import static com.google.common.base.Preconditions.checkArgument;

public class UpdateFlightOfferRequest {

    @JsonProperty
    private Double price;

    @JsonProperty
    private String[] flightDates;

    @JsonCreator
    public UpdateFlightOfferRequest(@JsonProperty("price") Double price,
                                    @JsonProperty("flightDates") String[] flightDates) {
        this.price = price;
        this.flightDates = flightDates;
        checkArgument(price!=null || flightDates!=null, "Must have an update!");
    }

    public Double getPrice() {
        return price;
    }

    public String[] getFlightDates() {
        return flightDates;
    }
}

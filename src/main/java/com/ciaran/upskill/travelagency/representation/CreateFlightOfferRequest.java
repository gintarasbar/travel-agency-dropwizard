package com.ciaran.upskill.travelagency.representation;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public class CreateFlightOfferRequest {

    @JsonProperty
    private Double price;

    @JsonProperty
    private String flightOrigin;


    @JsonProperty
    private String flightDestination;

    @JsonProperty
    private String airline;

    @JsonProperty
    private String[] flightDates;

    @JsonCreator
    public CreateFlightOfferRequest(@JsonProperty("price") Double price,
                                    @JsonProperty("flightOrigin") String flightOrigin,
                                    @JsonProperty("flightDestination") String flightDestination,
                                    @JsonProperty("airline") String airline,
                                    @JsonProperty("flightDates") String[] flightDates) {
        this.price = checkNotNull(price);
        checkArgument(price>0, "Price must be greater than 0");
        this.flightOrigin = checkNotNull(flightOrigin);
        this.flightDestination = checkNotNull(flightDestination);
        this.airline = airline;
        this.flightDates = checkNotNull(flightDates);
    }

    public Double getPrice() {
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

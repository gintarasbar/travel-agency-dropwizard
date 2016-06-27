package com.ciaran.upskill.travelagency.service;

import com.ciaran.upskill.travelagency.domain.FlightOffer;
import org.joda.time.DateTime;

import java.util.UUID;

public final class FlightOfferBuilder {
    DateTime[] flightDates;
    private double price;
    private UUID id;
    private String flightOriginId;
    private String flightDestinationId;
    private String airline;

    public FlightOfferBuilder() {
    }

    public static FlightOfferBuilder aFlightOffer() {
        return new FlightOfferBuilder();
    }

    public FlightOfferBuilder withPrice(double price) {
        this.price = price;
        return this;
    }

    public FlightOfferBuilder withFlightDates(DateTime[] flightDates) {
        this.flightDates = flightDates;
        return this;
    }

    public FlightOfferBuilder withId(UUID id) {
        this.id = id;
        return this;
    }

    public FlightOfferBuilder withFlightOriginId(String flightOriginId) {
        this.flightOriginId = flightOriginId;
        return this;
    }

    public FlightOfferBuilder withFlightDestinationId(String flightDestinationId) {
        this.flightDestinationId = flightDestinationId;
        return this;
    }

    public FlightOfferBuilder withAirline(String airline) {
        this.airline = airline;
        return this;
    }

    public FlightOffer build() {
        FlightOffer flightOffer = new FlightOffer(id, price, flightOriginId, flightDestinationId, airline, flightDates);
        return flightOffer;
    }
}

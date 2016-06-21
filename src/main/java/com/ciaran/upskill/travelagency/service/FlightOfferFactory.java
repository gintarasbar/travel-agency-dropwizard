package com.ciaran.upskill.travelagency.service;

import com.ciaran.upskill.travelagency.domain.FlightOffer;

import java.util.Date;
import java.util.UUID;

public class FlightOfferFactory {

    public static FlightOffer create(UUID uuid, double price, String flightOriginId, String flightDestinationId, String airline, Date[] datesArray){
        FlightOffer flightOffer = new FlightOffer(uuid, price, flightOriginId, flightDestinationId, airline, datesArray);
        return flightOffer;
    }
}

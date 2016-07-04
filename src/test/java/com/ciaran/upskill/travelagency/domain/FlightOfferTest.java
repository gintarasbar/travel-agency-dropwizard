package com.ciaran.upskill.travelagency.domain;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class FlightOfferTest {

    private UUID id;
    private DateTime[] dates;
    private String airline;
    private String flightDestinationId;
    private String flightOriginId;
    private double price;
    private FlightOffer flightOffer;
    private double distance;

    @Before
    public void setup() {
        dates = new DateTime[3];
        dates[0] = new DateTime("2016-06-21T10:00:00.000");
        dates[1] = new DateTime("2016-06-27T12:00:00.000");
        dates[2] = new DateTime("2016-07-01T16:30:00.000");
        id = UUID.randomUUID();
        airline = "Ryanair";
        flightDestinationId = "parisFR";
        flightOriginId = "londonGB";
        price = 22.99;
        distance = 344;
        flightOffer = new FlightOffer(id, price, flightOriginId, flightDestinationId, distance, airline, dates);
    }
    @Test
    public void shouldConvertToCSVRow(){
        String[] flightOfferArray = flightOffer.toCSVRow().split(",");
        System.out.println(flightOffer.toCSVRow());
        assertThat(flightOfferArray.length, is(7));
        assertThat(flightOfferArray[0], is(equalTo(id.toString())));
        assertThat(Double.parseDouble(flightOfferArray[1]), is(equalTo(price)));
        assertThat(flightOfferArray[2], is(equalTo(flightOriginId)));
        assertThat(flightOfferArray[3], is(equalTo(flightDestinationId)));
        assertThat(Double.parseDouble(flightOfferArray[4]), is(equalTo(distance)));
        assertThat(flightOfferArray[5], is(equalTo(airline)));
        assertThat(flightOfferArray[6], is(equalTo("["+dates[0]+";"+dates[1]+";"+dates[2]+"]")));
    }
}
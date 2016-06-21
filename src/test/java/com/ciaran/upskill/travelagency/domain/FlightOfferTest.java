package com.ciaran.upskill.travelagency.domain;

import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class FlightOfferTest {

    private UUID id;
    private AgencyDate[] dates;
    private String airline;
    private String flightDestinationId;
    private String flightOriginId;
    private double price;
    private FlightOffer flightOffer;

    @Before
    public void setup() {
        dates = new AgencyDate[3];
        dates[0] = new AgencyDate("2016-06-21");
        dates[1] = new AgencyDate("2016-06-27");
        dates[2] = new AgencyDate("2016-07-01");
        id = UUID.randomUUID();
        airline = "Ryanair";
        flightDestinationId = "parisFR";
        flightOriginId = "londonGB";
        price = 22.99;
        flightOffer = new FlightOffer(id, price, flightOriginId, flightDestinationId, airline, dates);
    }
    @Test
    public void shouldBeCSVReadableToStringMethod(){
        String[] flightOfferArray = flightOffer.toString().split(",");
        System.out.println(flightOffer.toString());
        assertThat(flightOfferArray.length, is(6));
        assertThat(flightOfferArray[0], is(equalTo(id.toString())));
        assertThat(Double.parseDouble(flightOfferArray[1]), is(equalTo(price)));
        assertThat(flightOfferArray[2], is(equalTo(flightOriginId)));
        assertThat(flightOfferArray[3], is(equalTo(flightDestinationId)));
        assertThat(flightOfferArray[4], is(equalTo(airline)));
        assertThat(flightOfferArray[5], is(equalTo("["+dates[0]+";"+dates[1]+";"+dates[2]+"]")));
    }
}
package com.ciaran.upskill.travelagency.domain;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class CityTest {

    private CoOrdinate location;
    private String countryCode;
    private String region;
    private int population;
    private String name;
    private String printName;
    private City city;

    @Before
    public void setup(){
        location = new CoOrdinate(51.514125, -.093689);
        countryCode = "GB";
        region = "H2";
        population = 12000000;
        name = "london";
        printName = "London";
        city = new City(countryCode, name, printName, region, population, location);
    }

    @Test
    public void shouldConvertToStringInCSVReadableFormat() throws Exception {
        String[] flightOfferArray = city.toString().split(",");
        assertThat(flightOfferArray.length, is(7));
        assertThat(flightOfferArray[0], is(equalTo(countryCode)));
        assertThat(flightOfferArray[1], is(equalTo(name)));
        assertThat(flightOfferArray[2], is(equalTo(printName)));
        assertThat(flightOfferArray[3], is(equalTo(region)));
        assertThat(Integer.parseInt(flightOfferArray[4]), is(equalTo(population)));
        assertThat(Double.parseDouble(flightOfferArray[5]), is(equalTo(location.getLatitude())));
        assertThat(Double.parseDouble(flightOfferArray[6]), is(equalTo(location.getLongitude())));
    }

}
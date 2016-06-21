package com.ciaran.upskill.travelagency.storage;

import com.ciaran.upskill.travelagency.domain.FlightOffer;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class FlightOffersRepositoryTest {

    private FlightOffersRepository flightOffersRepository;

    @Before
    public void setUp(){
        flightOffersRepository = new FlightOffersRepository();
    }

    @Test
    public void shouldLoadFromCSV(){
        flightOffersRepository.load();
        assertThat(flightOffersRepository.isEmpty(), is(false));
    }

    @Test
    public void shouldSaveToCSV(){
        FlightOffer flightOffer;
        Date[] dates = new Date[3];
        dates[0] = new Date(System.currentTimeMillis());
        dates[1] = new Date(System.currentTimeMillis() + 36000000);
        dates[2] = new Date(System.currentTimeMillis() + 72000000);
        UUID id = UUID.randomUUID();
        String airline = "Ryanair";
        String flightDestinationId = "parisFR";
        String flightOriginId = "londonGB";
        double price = 22.99;
        flightOffer = new FlightOffer(id, price, flightOriginId, flightDestinationId, airline, dates);
        flightOffersRepository.add(flightOffer);
        flightOffersRepository.save();
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader("/Users/ciaran.potter/projects/personal/travel-agency-dropwizard/src/main/resources/flightoffers.csv"));
            assertThat(bufferedReader.readLine(), is(equalTo("Id,Price,FlightOriginId,FlightDestinationId,Airline,Dates")));
            assertThat(bufferedReader.readLine(), is(equalTo(flightOffer.toString())));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
package com.ciaran.upskill.travelagency.storage;

import com.ciaran.upskill.travelagency.domain.FlightOffer;
import com.ciaran.upskill.travelagency.domain.AgencyDate;
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
    private static final String csvFile = "/Users/ciaran.potter/projects/personal/travel-agency-dropwizard/src/test/resources/flightoffers.csv";
    private FlightOffer flightOffer;

    @Before
    public void setUp(){
        flightOffersRepository = new FlightOffersRepository(csvFile);
        AgencyDate[] dates = new AgencyDate[3];
        dates[0] = new AgencyDate("2016-06-21");
        dates[1] = new AgencyDate("2016-06-27");
        dates[2] = new AgencyDate("2016-07-01");
        UUID id = UUID.randomUUID();
        String airline = "Ryanair";
        String flightDestinationId = "parisFR";
        String flightOriginId = "londonGB";
        double price = 22.99;
        flightOffer = new FlightOffer(id, price, flightOriginId, flightDestinationId, airline, dates);
    }

    @Test
    public void shouldLoadFromCSV(){
        flightOffersRepository.load();
        assertThat(flightOffersRepository.isEmpty(), is(false));
    }

    @Test
    public void shouldSaveToCSV(){
        flightOffersRepository.add(flightOffer);
        try {
            flightOffersRepository.save();
            BufferedReader bufferedReader = new BufferedReader(new FileReader(csvFile));
            assertThat(bufferedReader.readLine(), is(equalTo("Id,Price,FlightOriginId,FlightDestinationId,Airline,Dates")));
            assertThat(bufferedReader.readLine(), is(equalTo(flightOffer.toString())));
            bufferedReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void shouldGetFlightOfferById(){
        UUID id = UUID.randomUUID();
        FlightOffer flightOffer2 = new FlightOffer(id, Math.random(), "ab", "cd", "ef", new AgencyDate[2]);
        flightOffersRepository.add(flightOffer);
        flightOffersRepository.add(flightOffer2);
        FlightOffer retrievedFlightoffer = flightOffersRepository.getFLightOfferById(id);
        assertThat(retrievedFlightoffer, is(equalTo(flightOffer2)));
    }

    @Test
    public void shouldUpdateFlightOfferDate(){
        flightOffersRepository.add(flightOffer);
        AgencyDate[] dates = new AgencyDate[2];
        dates[0] = new AgencyDate("2016-06-21");
        dates[1] = new AgencyDate("2016-06-27");
        flightOffersRepository.updateDates(flightOffer.getId(), dates);
        FlightOffer retrievedOffer = flightOffersRepository.getFLightOfferById(flightOffer.getId());
        assertThat(retrievedOffer.getFlightDates(), is(equalTo(dates)));
    }

    @Test
    public void shouldUpdateFlightOfferPrice(){
        flightOffersRepository.add(flightOffer);
        double newPrice = 23.49;
        flightOffersRepository.updatePrice(flightOffer.getId(), newPrice);
        FlightOffer retrievedOffer = flightOffersRepository.getFLightOfferById(flightOffer.getId());
        assertThat(retrievedOffer.getPrice(), is(equalTo(newPrice)));
    }

}
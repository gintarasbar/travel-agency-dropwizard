package com.ciaran.upskill.travelagency.storage;

import com.ciaran.upskill.travelagency.domain.FlightOffer;
import com.google.common.io.Resources;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class FlightOffersRepositoryTest {

    private FlightOffersRepository flightOffersRepository;
    private static final String csvFile = "flightoffers.csv";
    private FlightOffer flightOffer;
    private String flightDestinationId;
    private String flightOriginId;

    @Before
    public void setUp(){
        flightOffersRepository = new FlightOffersRepository(Resources.getResource(csvFile).getPath());
        DateTime[] dates = new DateTime[3];
        dates[0] = new DateTime("2016-06-21");
        dates[1] = new DateTime("2016-06-27");
        dates[2] = new DateTime("2016-07-01");
        UUID id = UUID.randomUUID();
        String airline = "Ryanair";
        flightDestinationId = "parisFR";
        flightOriginId = "londonGB";
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
            BufferedReader bufferedReader = new BufferedReader(new FileReader(Resources.getResource(csvFile).getPath()));
            assertThat(bufferedReader.readLine(), is(equalTo("Id,Price,FlightOriginId,FlightDestinationId,Airline,Dates")));
            assertThat(bufferedReader.readLine(), is(equalTo(flightOffer.toCSVRow())));
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
        FlightOffer flightOffer2 = new FlightOffer(id, Math.random(), "ab", "cd", "ef", new DateTime[2]);
        flightOffersRepository.add(flightOffer);
        flightOffersRepository.add(flightOffer2);
        Optional<FlightOffer> retrievedFlightoffer = flightOffersRepository.getFLightOfferById(id);
        assertThat(retrievedFlightoffer.get(), is(equalTo(flightOffer2)));
    }

    @Test
    public void shouldUpdateFlightOfferDate(){
        flightOffersRepository.add(flightOffer);
        DateTime[] dates = new DateTime[2];
        dates[0] = new DateTime("2016-06-21");
        dates[1] = new DateTime("2016-06-27");
        flightOffersRepository.updateDates(flightOffer.getId(), dates);
        Optional<FlightOffer> retrievedOffer = flightOffersRepository.getFLightOfferById(flightOffer.getId());
        assertThat(retrievedOffer.get().getFlightDates(), is(equalTo(dates)));
    }

    @Test
    public void shouldUpdateFlightOfferPrice(){
        flightOffersRepository.add(flightOffer);
        double newPrice = 23.49;
        flightOffersRepository.updatePrice(flightOffer.getId(), newPrice);
        Optional<FlightOffer> retrievedOffer = flightOffersRepository.getFLightOfferById(flightOffer.getId());
        assertThat(retrievedOffer.get().getPrice(), is(equalTo(newPrice)));
    }

    @Test
    public void shouldFindFlightOffersByFlightOrigin(){
        flightOffersRepository.load();
        flightOffersRepository.add(flightOffer);
        Collection<FlightOffer> flightOfferCollection = flightOffersRepository.getFlightOfferByFlightOrigin(flightOriginId);
        for(FlightOffer nextFlightOffer : flightOfferCollection){
            assertThat(nextFlightOffer.getFlightOriginId(), is(equalTo(flightOriginId)));
        }
    }

    @Test
    public void shouldFindFLightOffersByFlightDestination(){
        flightOffersRepository.load();
        flightOffersRepository.add(flightOffer);
        Collection<FlightOffer> flightOfferCollection = flightOffersRepository.getFlightOfferByFlightDestination(flightDestinationId);
        for(FlightOffer nextFlightOffer : flightOfferCollection){
            assertThat(nextFlightOffer.getFlightDestinationId(), is(equalTo(flightDestinationId)));
        }
    }

}
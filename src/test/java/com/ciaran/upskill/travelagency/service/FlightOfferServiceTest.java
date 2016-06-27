package com.ciaran.upskill.travelagency.service;

import com.ciaran.upskill.travelagency.domain.AgencyDate;
import com.ciaran.upskill.travelagency.domain.City;
import com.ciaran.upskill.travelagency.domain.CoOrdinate;
import com.ciaran.upskill.travelagency.domain.FlightOffer;
import com.ciaran.upskill.travelagency.representation.CreateFlightOfferRequest;
import com.ciaran.upskill.travelagency.representation.UpdateFlightOfferRequest;
import com.ciaran.upskill.travelagency.storage.CitiesRepository;
import com.ciaran.upskill.travelagency.storage.FlightOffersRepository;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Collection;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.matches;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class FlightOfferServiceTest {

    @Mock
    private CityService cityService;

    @Mock
    private FlightOffersRepository flightOffersRepository;

    private FlightOfferService flightOfferService;

    @Before
    public void setUp(){
        cityService = Mockito.mock(CityService.class);
        flightOffersRepository = Mockito.mock(FlightOffersRepository.class);
        flightOfferService = new FlightOfferService(flightOffersRepository, cityService);

    }

    @Test
    public void shouldCreateFlightAndSave(){
        City city = new City("GB", "london", "London", "H2", 12000000, new CoOrdinate(51.514125, -.093689));
        when(cityService.getCityById(anyString())).thenReturn(city);
        CreateFlightOfferRequest createFlightOfferRequest = new CreateFlightOfferRequest(2.99, "londonGB", "parisFR", "Ryanair", new String[]{"2016-06-23", "2016-06-24"});
        FlightOffer flightOffer = flightOfferService.createFlightOffer(createFlightOfferRequest);
        verify(flightOffersRepository).add(flightOffer);
        verify(flightOffersRepository).save();
    }

    @Test
    public void shouldNotCreateFlightAndReturnNullIfCityDoesntExist(){
        when(cityService.getCityById(anyString())).thenReturn(null);
        CreateFlightOfferRequest createFlightOfferRequest = new CreateFlightOfferRequest(2.99, "londonGB", "parisFR", "Ryanair", new String[]{"2016-06-23", "2016-06-24"});
        FlightOffer flightOffer = flightOfferService.createFlightOffer(createFlightOfferRequest);
        assertThat(flightOffer, is(equalTo(null)));
        verify(flightOffersRepository, times(0)).add(any());
        verify(flightOffersRepository, times(0)).save();
    }

    @Test
    public void shouldUpdateFlightAndSave(){
        UUID id = UUID.randomUUID();
        FlightOffer flightOffer = new FlightOffer(id, 2.99, "londonGB", "parisFR", "Ryanair", new DateTime[]{new DateTime("2016-06-23T13:00:00.000"), new DateTime("2016-06-24T16:00:00.000")});
        when(flightOffersRepository.getFLightOfferById(any())).thenReturn(flightOffer);
        String[] flightDates = {"2016-06-25T13:00:00.000", "2016-06-26T16:00:00.000"};
        UpdateFlightOfferRequest updateFlightOfferRequest = new UpdateFlightOfferRequest(3.99, flightDates);
        flightOffer = flightOfferService.updateFlightOffer(id.toString(), updateFlightOfferRequest);
        verify(flightOffersRepository).save();
        assertThat(flightOffer.getPrice(), is(3.99));
        for(int i = 0; i<flightOffer.getFlightDates().length; i++){
            assertThat(flightOffer.getFlightDates()[i], is(equalTo(new DateTime(flightDates[i]))));
        }

    }

    @Test
    public void shouldNotUpdateFlightAndSaveIfFlightIdNotFound(){
        UUID id = UUID.randomUUID();
        DateTime[] flightDates = {new DateTime("2016-06-23T13:00:00.000"), new DateTime("2016-06-24T16:00:00.000")};
        FlightOffer flightOffer = new FlightOffer(id, 2.99, "londonGB", "parisFR", "Ryanair", flightDates);
        when(flightOffersRepository.getFLightOfferById(any())).thenReturn(null);
        UpdateFlightOfferRequest updateFlightOfferRequest = new UpdateFlightOfferRequest(3.99, new String[]{"2016-06-25T13:00:00.000", "2016-06-26T16:00:00.000"});
        FlightOffer flightOffer2 = flightOfferService.updateFlightOffer(id.toString(), updateFlightOfferRequest);
        verify(flightOffersRepository, times(0)).save();
        assertThat(flightOffer.getPrice(), is(2.99));
        assertThat(flightOffer.getFlightDates(), is(flightDates));
        assertThat(flightOffer2, is(equalTo(null)));
    }

    @Test
    public void shouldNotUpdateFlightAndSaveIfThereIsNoUpdate(){
        UUID id = UUID.randomUUID();
        DateTime[] flightDates = {new DateTime("2016-06-23T13:00:00.000"), new DateTime("2016-06-24T16:00:00.000")};
        FlightOffer flightOffer = new FlightOffer(id, 2.99, "londonGB", "parisFR", "Ryanair", flightDates);
        when(flightOffersRepository.getFLightOfferById(any())).thenReturn(flightOffer);
        UpdateFlightOfferRequest updateFlightOfferRequest = new UpdateFlightOfferRequest(2.99, null);
        FlightOffer flightOffer2 = flightOfferService.updateFlightOffer(id.toString(), updateFlightOfferRequest);
        verify(flightOffersRepository, times(0)).save();
        assertThat(flightOffer.getPrice(), is(2.99));
        assertThat(flightOffer.getFlightDates(), is(flightDates));
        assertThat(flightOffer2, is(equalTo(flightOffer)));
    }

    @Test
    public void shouldDeleteAndSaveIfFlightIsCancelled(){
        UUID id = UUID.randomUUID();
        DateTime[] flightDates = {new DateTime("2016-06-23T13:00:00.000"), new DateTime("2016-06-24T16:00:00.000")};
        FlightOffer flightOffer = new FlightOffer(id, 2.99, "londonGB", "parisFR", "Ryanair", flightDates);
        when(flightOffersRepository.getFLightOfferById(id)).thenReturn(flightOffer);
        when(flightOffersRepository.remove(flightOffer)).thenReturn(true);
        assertThat(flightOfferService.cancelFlightOffer(id.toString()), is(true));
        verify(flightOffersRepository).remove(flightOffer);
        verify(flightOffersRepository).save();
    }

    @Test
    public void shouldReturnFalseIfFlightNotFound(){
        UUID id = UUID.randomUUID();
        DateTime[] flightDates = {new DateTime("2016-06-23T13:00:00.000"), new DateTime("2016-06-24T16:00:00.000")};
        FlightOffer flightOffer = new FlightOffer(id, 2.99, "londonGB", "parisFR", "Ryanair", flightDates);
        when(flightOffersRepository.getFLightOfferById(any())).thenReturn(null);
        assertThat(flightOfferService.cancelFlightOffer(id.toString()), is(false));
        verify(flightOffersRepository, times(0)).remove(flightOffer);
        verify(flightOffersRepository, times(0)).save();
    }

    @Test
    public void shouldFindOnlyFlightOffersWithMatchingJourneyStartAndDate(){
        flightOffersRepository = new FlightOffersRepository("");
        flightOfferService = new FlightOfferService(flightOffersRepository, cityService);
        DateTime[] flightDates = {new DateTime("2016-06-23T13:00:00.000"), new DateTime("2016-06-24T16:00:00.000")};
        UUID id = UUID.randomUUID();
        FlightOffer flightOffer1 = new FlightOffer(UUID.randomUUID(), 2.99, "londonGB", "parisFR", "Ryanair", flightDates);
        FlightOffer flightOffer2 = new FlightOffer(UUID.randomUUID(), 2.99, "londonGB", "parisFR", "EasyJet", new DateTime[]{new DateTime("2016-06-24T16:00:00.000")});
        FlightOffer flightOffer3 = new FlightOffer(UUID.randomUUID(), 2.99, "moscowRU", "parisFR", "Ryanair", flightDates);
        FlightOffer flightOffer4 = new FlightOffer(UUID.randomUUID(), 2.99, "londonGB", "moscowRU", "EasyJet", flightDates);
        flightOffersRepository.add(flightOffer1);
        flightOffersRepository.add(flightOffer2);
        flightOffersRepository.add(flightOffer3);
        flightOffersRepository.add(flightOffer4);
        Collection<FlightOffer> flightOfferCollection = flightOfferService.findFlightOfferByJourneyStart("londonGB", "2016-06-23");
        assertThat(flightOfferCollection.contains(flightOffer1), is(true));
        assertThat(flightOfferCollection.contains(flightOffer2), is(false));
        assertThat(flightOfferCollection.contains(flightOffer3), is(false));
        assertThat(flightOfferCollection.contains(flightOffer4), is(true));
    }
}
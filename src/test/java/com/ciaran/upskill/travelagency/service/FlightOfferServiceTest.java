package com.ciaran.upskill.travelagency.service;

import com.ciaran.upskill.travelagency.domain.AgencyDate;
import com.ciaran.upskill.travelagency.domain.City;
import com.ciaran.upskill.travelagency.domain.CoOrdinate;
import com.ciaran.upskill.travelagency.domain.FlightOffer;
import com.ciaran.upskill.travelagency.representation.CreateFlightOfferRequest;
import com.ciaran.upskill.travelagency.representation.UpdateFlightOfferRequest;
import com.ciaran.upskill.travelagency.storage.CitiesRepository;
import com.ciaran.upskill.travelagency.storage.FlightOffersRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

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
        FlightOffer flightOffer = new FlightOffer(id, 2.99, "londonGB", "parisFR", "Ryanair", new AgencyDate[]{new AgencyDate("2016-06-23"), new AgencyDate("2016-06-24")});
        when(flightOffersRepository.getFLightOfferById(any())).thenReturn(flightOffer);
        String[] flightDates = {"2016-06-25", "2016-06-26"};
        UpdateFlightOfferRequest updateFlightOfferRequest = new UpdateFlightOfferRequest(3.99, flightDates);
        flightOffer = flightOfferService.updateFlightOffer(id.toString(), updateFlightOfferRequest);
        verify(flightOffersRepository).save();
        assertThat(flightOffer.getPrice(), is(3.99));
        for(int i = 0; i<flightOffer.getFlightDates().length; i++){
            assertThat(flightOffer.getFlightDates()[i].toString(), is(equalTo(flightDates[i])));
        }

    }

    @Test
    public void shouldNotUpdateFlightAndSaveIfFlightIdNotFound(){
        UUID id = UUID.randomUUID();
        AgencyDate[] flightDates = {new AgencyDate("2016-06-23"), new AgencyDate("2016-06-24")};
        FlightOffer flightOffer = new FlightOffer(id, 2.99, "londonGB", "parisFR", "Ryanair", flightDates);
        when(flightOffersRepository.getFLightOfferById(any())).thenReturn(null);
        UpdateFlightOfferRequest updateFlightOfferRequest = new UpdateFlightOfferRequest(3.99, new String[]{"2016-06-25", "2016-06-26"});
        FlightOffer flightOffer2 = flightOfferService.updateFlightOffer(id.toString(), updateFlightOfferRequest);
        verify(flightOffersRepository, times(0)).save();
        assertThat(flightOffer.getPrice(), is(2.99));
        assertThat(flightOffer.getFlightDates(), is(flightDates));
        assertThat(flightOffer2, is(equalTo(null)));
    }
}
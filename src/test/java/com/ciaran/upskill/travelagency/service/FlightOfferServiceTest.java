package com.ciaran.upskill.travelagency.service;

import com.ciaran.upskill.travelagency.domain.City;
import com.ciaran.upskill.travelagency.domain.CoOrdinate;
import com.ciaran.upskill.travelagency.domain.FlightOffer;
import com.ciaran.upskill.travelagency.representation.CreateFlightOfferRequest;
import com.ciaran.upskill.travelagency.representation.UpdateFlightOfferRequest;
import com.ciaran.upskill.travelagency.storage.CitiesRepository;
import com.ciaran.upskill.travelagency.storage.FlightOffersRepository;
import javassist.NotFoundException;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FlightOfferServiceTest {

    @Mock
    private CityService cityService;

    @Mock
    private FlightOffersRepository flightOffersRepository;

    private FlightOfferService flightOfferService;

    private final String worldCitiesCSV = "worldcities.csv";

    @Before
    public void setUp(){
        flightOfferService = new FlightOfferService(flightOffersRepository, cityService);

    }

    @Test
    public void shouldCreateFlightAndSave() throws NotFoundException {
        City city = new City("GB", "london", "London", "H2", 12000000, new CoOrdinate(51.514125, -.093689));
        when(cityService.getCityById(anyString())).thenReturn(city);
        CreateFlightOfferRequest createFlightOfferRequest = new CreateFlightOfferRequest(2.99, "londonGB", "parisFR", "Ryanair", new String[]{"2016-06-23", "2016-06-24"});

        FlightOffer flightOffer = flightOfferService.createFlightOffer(createFlightOfferRequest);

        verify(flightOffersRepository).add(flightOffer);
        verify(flightOffersRepository).save();
    }

    @Test(expected = NotFoundException.class)
    public void shouldNotCreateFlightIfCityDoesntExist() throws NotFoundException {
        cityService = new CityService(new CitiesRepository(worldCitiesCSV));
        flightOfferService = new FlightOfferService(flightOffersRepository, cityService);
        CreateFlightOfferRequest createFlightOfferRequest = new CreateFlightOfferRequest(2.99, "citydoesnotexistGB", "parisFR", "Ryanair", new String[]{"2016-06-23", "2016-06-24"});

        FlightOffer flightOffer = flightOfferService.createFlightOffer(createFlightOfferRequest);
    }

    @Test
    public void shouldUpdateFlightAndSave() throws NotFoundException {
        UUID id = UUID.randomUUID();
        DateTime[] flightDates = {new DateTime("2016-06-23T13:00:00.000"), new DateTime("2016-06-24T16:00:00.000")};
        FlightOffer flightOffer = new FlightOffer(id, 2.99, "londonGB", "parisFR", 344, "Ryanair", flightDates);
        when(flightOffersRepository.getFLightOfferById(any())).thenReturn(Optional.of(flightOffer));
        String[] newFlightDates = {"2016-06-25T13:00:00.000", "2016-06-26T16:00:00.000"};
        DateTime[] newFlightDateTimes = {new DateTime("2016-06-25T13:00:00.000"), new DateTime("2016-06-26T16:00:00.000")};
        UpdateFlightOfferRequest updateFlightOfferRequest = new UpdateFlightOfferRequest(3.99, newFlightDates);

        FlightOffer returnedFlightOffer = flightOfferService.updateFlightOffer(id, updateFlightOfferRequest);

        verify(flightOffersRepository).save();
        assertThat(returnedFlightOffer.getPrice(), is(3.99));
        assertThat(returnedFlightOffer.getFlightDates(), is(newFlightDateTimes));

    }

    @Test(expected = NotFoundException.class)
    public void shouldNotUpdateFlightAndSaveIfFlightIdNotFound() throws NotFoundException {
        UUID id = UUID.randomUUID();
        DateTime[] flightDates = {new DateTime("2016-06-23T13:00:00.000"), new DateTime("2016-06-24T16:00:00.000")};
        FlightOffer flightOffer = new FlightOffer(id, 2.99, "londonGB", "parisFR", 344, "Ryanair", flightDates);
        when(flightOffersRepository.getFLightOfferById(any())).thenReturn(Optional.empty());
        UpdateFlightOfferRequest updateFlightOfferRequest = new UpdateFlightOfferRequest(3.99, new String[]{"2016-06-25T13:00:00.000", "2016-06-26T16:00:00.000"});

        flightOfferService.updateFlightOffer(id, updateFlightOfferRequest);
    }

    @Test
    public void shouldNotUpdateFlightAndSaveIfThereIsNoUpdate() throws NotFoundException {
        UUID id = UUID.randomUUID();
        DateTime[] flightDates = {new DateTime("2016-06-23T13:00:00.000"), new DateTime("2016-06-24T16:00:00.000")};
        FlightOffer flightOffer = new FlightOffer(id, 2.99, "londonGB", "parisFR", 344, "Ryanair", flightDates);
        when(flightOffersRepository.getFLightOfferById(any())).thenReturn(Optional.of(flightOffer));
        UpdateFlightOfferRequest updateFlightOfferRequest = new UpdateFlightOfferRequest(2.99, null);

        FlightOffer flightOffer2 = flightOfferService.updateFlightOffer(id, updateFlightOfferRequest);

        verify(flightOffersRepository, times(0)).save();
        assertThat(flightOffer.getPrice(), is(2.99));
        assertThat(flightOffer.getFlightDates(), is(flightDates));
        assertThat(flightOffer2, is(equalTo(flightOffer)));
    }

    @Test
    public void shouldDeleteAndSaveIfFlightIsCancelled(){
        UUID id = UUID.randomUUID();
        DateTime[] flightDates = {new DateTime("2016-06-23T13:00:00.000"), new DateTime("2016-06-24T16:00:00.000")};
        FlightOffer flightOffer = new FlightOffer(id, 2.99, "londonGB", "parisFR", 344, "Ryanair", flightDates);
        when(flightOffersRepository.getFLightOfferById(id)).thenReturn(Optional.of(flightOffer));
        when(flightOffersRepository.remove(flightOffer)).thenReturn(true);

        assertThat(flightOfferService.cancelFlightOffer(id), is(true));
        verify(flightOffersRepository).remove(flightOffer);
        verify(flightOffersRepository).save();
    }

    @Test
    public void shouldReturnFalseIfFlightNotFound(){
        UUID id = UUID.randomUUID();
        DateTime[] flightDates = {new DateTime("2016-06-23T13:00:00.000"), new DateTime("2016-06-24T16:00:00.000")};
        FlightOffer flightOffer = new FlightOffer(id, 2.99, "londonGB", "parisFR", 344, "Ryanair", flightDates);
        when(flightOffersRepository.getFLightOfferById(any())).thenReturn(Optional.empty());

        assertThat(flightOfferService.cancelFlightOffer(id), is(false));
        verify(flightOffersRepository, times(0)).remove(flightOffer);
        verify(flightOffersRepository, times(0)).save();
    }

    @Test
    public void shouldFindOnlyFlightOffersWithMatchingJourneyStartAndDate(){
        DateTime[] flightDates = {new DateTime("2016-06-23T13:00:00.000"), new DateTime("2016-06-24T16:00:00.000")};
        UUID id = UUID.randomUUID();
        FlightOffer flightOffer1 = new FlightOffer(UUID.randomUUID(), 2.99, "londonGB", "parisFR", 344, "Ryanair", flightDates);
        FlightOffer flightOffer2 = new FlightOffer(UUID.randomUUID(), 2.99, "londonGB", "parisFR", 344, "EasyJet", new DateTime[]{new DateTime("2016-06-24T16:00:00.000")});
        FlightOffer flightOffer3 = new FlightOffer(UUID.randomUUID(), 2.99, "moscowRU", "parisFR", 2500, "Ryanair", flightDates);
        FlightOffer flightOffer4 = new FlightOffer(UUID.randomUUID(), 2.99, "londonGB", "moscowRU", 2500, "EasyJet", flightDates);
        Collection<FlightOffer> initialFlightOfferCollection = new HashSet<>();
        initialFlightOfferCollection.add(flightOffer1);
        initialFlightOfferCollection.add(flightOffer2);
        initialFlightOfferCollection.add(flightOffer4);
        when(flightOffersRepository.getFlightOfferByFlightOrigin(any())).thenReturn(initialFlightOfferCollection);

        Collection<FlightOffer> flightOfferCollection = flightOfferService.findFlightOfferByJourneyStart("londonGB", "2016-06-23");

        assertThat(flightOfferCollection, containsInAnyOrder(flightOffer1, flightOffer4));
    }

    @Test
    public void shouldReturnFlightWithNearestFlightOriginWhenSearchingByDestination() throws NotFoundException {
        DateTime[] flightDates = {new DateTime("2016-06-23T13:00:00.000"), new DateTime("2016-06-24T16:00:00.000"), new DateTime("2016-06-25T16:00:00.000")};
        UUID id = UUID.randomUUID();
        String londonGB = "londonGB";
        String parisFR = "parisFR";
        String moscowRU = "moscowRU";
        String manchesterGB = "manchesterGB";
        FlightOffer flightOffer1 = new FlightOffer(UUID.randomUUID(), 2.99, moscowRU, parisFR, 2500, "Ryanair", flightDates);
        FlightOffer flightOffer2 = new FlightOffer(UUID.randomUUID(), 2.99, londonGB, parisFR, 344, "Ryanair", flightDates);
        Collection<FlightOffer> initialFlightOfferCollection = new HashSet<>();
        initialFlightOfferCollection.add(flightOffer1);
        initialFlightOfferCollection.add(flightOffer2);
        when(flightOffersRepository.getFlightOfferByFlightDestination(any())).thenReturn(initialFlightOfferCollection);
        when(cityService.getDistance(manchesterGB, londonGB)).thenReturn(162.9);
        when(cityService.getDistance(manchesterGB, moscowRU)).thenReturn(1581.6);

        Collection<FlightOffer> flightOfferCollection = flightOfferService.findNearestFlightOfferToJourneyEnd(parisFR, manchesterGB, "2016-06-23T00:00:00Z");

        assertThat(flightOfferCollection, containsInAnyOrder(flightOffer2));
    }

    @Test
    public void shouldReturnFlightWithNearestFlightOriginThenDateWhenSearchingByDestination() throws NotFoundException {
        DateTime[] flightDates = {new DateTime("2016-06-23T13:00:00.000"), new DateTime("2016-06-24T16:00:00.000")};
        UUID id = UUID.randomUUID();
        String londonGB = "londonGB";
        String parisFR = "parisFR";
        String moscowRU = "moscowRU";
        String manchesterGB = "manchesterGB";
        FlightOffer flightOffer1 = new FlightOffer(UUID.randomUUID(), 2.99, moscowRU, parisFR, 2500, "Ryanair", flightDates);
        FlightOffer flightOffer2 = new FlightOffer(UUID.randomUUID(), 2.99, londonGB, parisFR, 344, "Ryanair", flightDates);
        FlightOffer flightOffer3 = new FlightOffer(UUID.randomUUID(), 2.99, londonGB, parisFR, 344, "Ryanair", new DateTime[]{new DateTime("2016-06-25T16:00:00Z"), new DateTime("2016-06-26T06:00:00Z")});
        FlightOffer flightOffer4 = new FlightOffer(UUID.randomUUID(), 2.99, moscowRU, parisFR, 2500, "Ryanair", new DateTime[]{new DateTime("2016-06-25T16:00:00Z"), new DateTime("2016-06-26T06:00:00Z")});
        Collection<FlightOffer> initialFlightOfferCollection = new HashSet<>();
        initialFlightOfferCollection.add(flightOffer1);
        initialFlightOfferCollection.add(flightOffer2);
        initialFlightOfferCollection.add(flightOffer3);
        initialFlightOfferCollection.add(flightOffer4);
        when(cityService.getDistance(manchesterGB, londonGB)).thenReturn(214.0);
        when(cityService.getDistance(manchesterGB, moscowRU)).thenReturn(1581.6);
        when(flightOffersRepository.getFlightOfferByFlightDestination(any())).thenReturn(initialFlightOfferCollection);

        Collection<FlightOffer> flightOfferCollection = flightOfferService.findNearestFlightOfferToJourneyEnd(parisFR, manchesterGB, "2016-06-25T00:00:00Z");

        assertThat(flightOfferCollection, containsInAnyOrder(flightOffer3));
    }


}
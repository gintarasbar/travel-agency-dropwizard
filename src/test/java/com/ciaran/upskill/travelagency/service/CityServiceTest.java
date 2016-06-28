package com.ciaran.upskill.travelagency.service;

import com.ciaran.upskill.travelagency.storage.CitiesRepository;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Mockito.mock;

public class CityServiceTest {

    @Mock
    CitiesRepository citiesRepository;

    CityService cityService;

    public void setUp(){
        citiesRepository = mock(CitiesRepository.class);
        cityService = new CityService(citiesRepository);
    }

    @Test
    public void shouldReturnNearestCityFromCollectionOfIds(){
        //TODO this tests and some others
    }

}
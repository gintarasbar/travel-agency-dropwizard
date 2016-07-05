package com.ciaran.upskill.travelagency.service;

import com.ciaran.upskill.travelagency.storage.CitiesRepository;
import com.google.common.io.Resources;
import javassist.NotFoundException;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static com.ciaran.upskill.travelagency.TravelAgencyApp.worldCitiesCSV;
import static org.hamcrest.JMock1Matchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.matches;
import static org.mockito.Mockito.mock;

public class CityServiceTest {

    CitiesRepository citiesRepository;

    CityService cityService;

    @Before
    public void setUp(){
        citiesRepository = new CitiesRepository(Resources.getResource(worldCitiesCSV).getPath());
        citiesRepository.load();
        cityService = new CityService(citiesRepository);
    }

    @Test
    public void shouldReturnNearestCityFromCollectionOfIds() throws NotFoundException {
        ArrayList<String> cityIdList = new ArrayList<>();
        cityIdList.add("londonGB");
        cityIdList.add("moscowRU");
        cityIdList.add("parisFR");
        cityIdList.add("beijingCN");
        cityIdList.add("new-yorkUS");
        cityIdList.add("edinburghGB");

        String cityNearestBristol = cityService.chooseNearesCity(cityService.getCityById("bristolGB"), cityIdList);
        String cityNearestGlasgow = cityService.chooseNearesCity(cityService.getCityById("glasgowGB"), cityIdList);
        String cityNearestShanghai = cityService.chooseNearesCity(cityService.getCityById("shanghaiCN"), cityIdList);
        String cityNearestChicago = cityService.chooseNearesCity(cityService.getCityById("chicagoUS"), cityIdList);
        String cityNearestOmsk = cityService.chooseNearesCity(cityService.getCityById("omskRU"), cityIdList);
        String cityNearestLyon = cityService.chooseNearesCity(cityService.getCityById("lyonFR"), cityIdList);

        assertThat(cityNearestBristol.matches("londonGB"), is(true));
        assertThat(cityNearestOmsk.matches("moscowRU"), is(true));
        assertThat(cityNearestLyon.matches("parisFR"), is(true));
        assertThat(cityNearestShanghai.matches("beijingCN"), is(true));
        assertThat(cityNearestChicago.matches("new-yorkUS"), is(true));
        assertThat(cityNearestGlasgow.matches("edinburghGB"), is(true));


    }

}
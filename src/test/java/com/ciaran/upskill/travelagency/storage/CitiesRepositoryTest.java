package com.ciaran.upskill.travelagency.storage;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class CitiesRepositoryTest {

    private CitiesRepository cities;

    private static final String worldCitiesCSV= "/Users/ciaran.potter/projects/personal/travel-agency-dropwizard/src/test/resources/worldcities.csv";

    @Before
    public void setup(){
        cities = new CitiesRepository(worldCitiesCSV);
        cities.load();
    }

    @Test
    public void shouldLoadCitiesFromCSV(){
        assertThat(cities.isEmpty(), is(false));
    }

    @Test
    public void shouldGetCityById(){
        assertThat(cities.getCityById("londonGB"), is(not(equalTo(null))));
    }


}
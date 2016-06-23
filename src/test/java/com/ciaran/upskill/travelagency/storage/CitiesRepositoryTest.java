package com.ciaran.upskill.travelagency.storage;

import com.google.common.io.Resources;
import org.junit.Before;
import org.junit.Test;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class CitiesRepositoryTest {

    private CitiesRepository cities;

    private static final String worldCitiesCSV = "worldcities.csv";

    @Before
    public void setup() {
        cities = new CitiesRepository(Resources.getResource(worldCitiesCSV).getPath());
        cities.load();
    }

    @Test
    public void shouldLoadCitiesFromCSV() {
        assertThat(cities.isEmpty(), is(false));
    }

    @Test
    public void shouldGetCityById() {
        checkNotNull(cities.getCityById("londonGB"));
    }


}
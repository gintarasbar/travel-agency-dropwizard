package com.ciaran.upskill.travelagency.service;

import com.ciaran.upskill.travelagency.domain.City;
import com.ciaran.upskill.travelagency.storage.CitiesRepository;

import java.util.Collection;

public class CityService {

    private CitiesRepository citiesRepository;

    public CityService(CitiesRepository citiesRepository) {
        this.citiesRepository = citiesRepository;
    }

    public City chooseNearesCity(City baseCity, Collection<String> cityOptions){
        return null;
    }

    public double getDistance(String baseCityId, String destinationCityId){
        return 0.0;
    }

    public City getCityById(String id) {
        return citiesRepository.getCityById(id);
    }
}

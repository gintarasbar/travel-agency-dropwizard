package com.ciaran.upskill.travelagency.service;

import com.ciaran.upskill.travelagency.domain.City;
import com.ciaran.upskill.travelagency.storage.CitiesRepository;
import javassist.NotFoundException;

import java.util.Collection;

public class CityService {

    private CitiesRepository citiesRepository;

    public CityService(CitiesRepository citiesRepository) {
        this.citiesRepository = citiesRepository;
    }

    public String chooseNearesCity(City baseCity, Collection<String> cityOptions){
        double closestDistance = -1.0;
        String closestCity = null;
        for(String city: cityOptions){
            double distance = 0;
            try {
                distance = citiesRepository.getCityById(city).getDistance(baseCity);
                if (closestCity == null || distance < closestDistance){
                    closestDistance = distance;
                    closestCity = city;
                }
            } catch (NotFoundException e) {
                e.printStackTrace();
            }
        }
        return closestCity;
    }

    public double getDistance(String baseCityId, String destinationCityId) throws NotFoundException {
        return citiesRepository.getCityById(baseCityId).getDistance(citiesRepository.getCityById(destinationCityId));
    }

    public City getCityById(String id) throws NotFoundException {
        return citiesRepository.getCityById(id);
    }
}

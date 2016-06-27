package com.ciaran.upskill.travelagency;

import com.ciaran.upskill.travelagency.config.TravelAgencyConfig;
import com.ciaran.upskill.travelagency.resource.TravelAgencyResource;
import com.ciaran.upskill.travelagency.service.CityService;
import com.ciaran.upskill.travelagency.service.FlightOfferService;
import com.ciaran.upskill.travelagency.storage.CitiesRepository;
import com.ciaran.upskill.travelagency.storage.FlightOffersRepository;
import com.google.common.io.Resources;
import io.dropwizard.Application;
import io.dropwizard.setup.Environment;

public class TravelAgencyApp extends Application<TravelAgencyConfig> {

    private static final String flightOffersCSV = "flightoffers.csv";
    public static final String worldCitiesCSV= "worldcities.csv";

    public static void main(String[] args) throws Exception{
        new TravelAgencyApp().run(args);
    }

    public void run(TravelAgencyConfig configuration, Environment environment) throws Exception {

        final FlightOffersRepository flightOffersRepository = new FlightOffersRepository(Resources.getResource(flightOffersCSV).getPath());
        flightOffersRepository.load();
        final CitiesRepository citiesRepository = new CitiesRepository(Resources.getResource(worldCitiesCSV).getPath());
        citiesRepository.load();
        final CityService cityService = new CityService(citiesRepository);
        final FlightOfferService flightOfferService = new FlightOfferService(flightOffersRepository, cityService);
        final TravelAgencyResource resource = new TravelAgencyResource(flightOfferService);
        final TravelAgencyHealthCheck healthCheck =
                new TravelAgencyHealthCheck(configuration.getTemplate());

        environment.getObjectMapper().disable(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        environment.healthChecks().register("template", healthCheck);
        environment.jersey().register(resource);

    }
}

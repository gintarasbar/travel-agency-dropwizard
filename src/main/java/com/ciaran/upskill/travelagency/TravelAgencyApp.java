package com.ciaran.upskill.travelagency;

import com.ciaran.upskill.travelagency.config.TravelAgencyConfig;
import com.ciaran.upskill.travelagency.resource.TravelAgencyResource;
import com.ciaran.upskill.travelagency.storage.CitiesRepository;
import com.ciaran.upskill.travelagency.storage.FlightOffersRepository;
import io.dropwizard.Application;
import io.dropwizard.setup.Environment;

public class TravelAgencyApp extends Application<TravelAgencyConfig> {

    private static final String flightOffersCSV = "/Users/ciaran.potter/projects/personal/travel-agency-dropwizard/src/main/resources/flightoffers.csv";
    public static final String worldCitiesCSV= "/Users/ciaran.potter/projects/personal/travel-agency-dropwizard/src/main/resources/worldcities.csv";

    public static void main(String[] args) throws Exception{
        new TravelAgencyApp().run(args);
    }

    public void run(TravelAgencyConfig configuration, Environment environment) throws Exception {
        final FlightOffersRepository flightOffersRepository = new FlightOffersRepository(flightOffersCSV);
        flightOffersRepository.load();
        final CitiesRepository citiesRepository = new CitiesRepository(worldCitiesCSV);
        citiesRepository.load();
        final TravelAgencyResource resource = new TravelAgencyResource(flightOffersRepository, citiesRepository);
        final TravelAgencyHealthCheck healthCheck =
                new TravelAgencyHealthCheck(configuration.getTemplate());
        environment.healthChecks().register("template", healthCheck);
        environment.jersey().register(resource);

    }
}

package com.ciaran.upskill.travelagency;

import com.ciaran.upskill.travelagency.config.TravelAgencyConfig;
import com.ciaran.upskill.travelagency.resource.TravelAgencyResource;
import io.dropwizard.Application;
import io.dropwizard.setup.Environment;

public class TravelAgencyApp extends Application<TravelAgencyConfig> {

    public static void main(String[] args) throws Exception{
        new TravelAgencyApp().run(args);
    }

    public void run(TravelAgencyConfig configuration, Environment environment) throws Exception {

        final TravelAgencyResource resource = new TravelAgencyResource(
                configuration.getTemplate(),
                configuration.getDefaultName()
        );
        final TravelAgencyHealthCheck healthCheck =
                new TravelAgencyHealthCheck(configuration.getTemplate());
        environment.healthChecks().register("template", healthCheck);
        environment.jersey().register(resource);

    }
}

package com.ciaran.upskill.travelagency.resource;

import com.ciaran.upskill.travelagency.Saying;
import com.ciaran.upskill.travelagency.config.TravelAgencyConfig;
import com.codahale.metrics.annotation.Timed;
import com.google.common.base.Optional;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.concurrent.atomic.AtomicLong;

@Path("/flight-offers")
public class TravelAgencyResource {
    private final String template;
    private final String defaultName;
    private final AtomicLong counter;

    //TODO replace Dropwizard example
    public TravelAgencyResource(String template, String defaultName) {
        this.template = template;
        this.defaultName = defaultName;
        this.counter = new AtomicLong();
    }


    @GET
    @Path("/hello-world")
    @Produces(MediaType.APPLICATION_JSON)
    @Timed
    public Saying sayHello(@QueryParam("name") Optional<String> name) {
        final String value = String.format(template, name.or(defaultName));
        return new Saying(counter.incrementAndGet(), value);
    }
    //end of example
}

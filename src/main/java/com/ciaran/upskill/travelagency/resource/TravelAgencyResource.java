package com.ciaran.upskill.travelagency.resource;

import com.ciaran.upskill.travelagency.domain.FlightOffer;
import com.ciaran.upskill.travelagency.representation.CreateFlightOfferRequest;
import com.ciaran.upskill.travelagency.representation.UpdateFlightOfferRequest;
import com.ciaran.upskill.travelagency.service.FlightOfferService;
import com.ciaran.upskill.travelagency.storage.CitiesRepository;
import com.ciaran.upskill.travelagency.storage.FlightOffersRepository;
import com.codahale.metrics.annotation.Timed;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/flight-offers")
public class TravelAgencyResource {
    private FlightOfferService flightOfferService;


    public TravelAgencyResource(FlightOfferService flightOfferService) {
        this.flightOfferService = flightOfferService;
    }

    @POST
    @Path("/")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Timed
    public FlightOffer createFlightOffer(CreateFlightOfferRequest createFlightOfferRequest){
        return flightOfferService.createFlightOffer(createFlightOfferRequest);
    }

    @POST
    @Path("/{id}/update")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Timed
    public FlightOffer updateFlightOffer(@PathParam("id") String flightOfferId, UpdateFlightOfferRequest updateFlightOfferRequest){
        return flightOfferService.updateFlightOffer(flightOfferId, updateFlightOfferRequest);

    }
}

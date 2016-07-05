package com.ciaran.upskill.travelagency.resource;

import com.ciaran.upskill.travelagency.domain.FlightOffer;
import com.ciaran.upskill.travelagency.representation.CreateFlightOfferRequest;
import com.ciaran.upskill.travelagency.representation.UpdateFlightOfferRequest;
import com.ciaran.upskill.travelagency.service.FlightOfferService;
import com.codahale.metrics.annotation.Timed;
import javassist.NotFoundException;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@Path("/flight-offers")
public class TravelAgencyResource {
    private FlightOfferService flightOfferService;


    public TravelAgencyResource(FlightOfferService flightOfferService) {
        this.flightOfferService = flightOfferService;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Timed
    public FlightOffer createFlightOffer(CreateFlightOfferRequest createFlightOfferRequest) throws NotFoundException {
        return flightOfferService.createFlightOffer(createFlightOfferRequest);
    }

    @POST
    @Path("/{id}/update")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Timed
    public FlightOffer updateFlightOffer(@PathParam("id") UUID flightOfferId, UpdateFlightOfferRequest updateFlightOfferRequest) throws NotFoundException {
        return flightOfferService.updateFlightOffer(flightOfferId, updateFlightOfferRequest);

    }

    @POST
    @Path("/{id}/cancel")
    @Timed
    public Response cancelFlightOffer(@PathParam("id") UUID flightOfferId){
        if (flightOfferService.cancelFlightOffer(flightOfferId)){
            return Response.ok().build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Timed
    public FlightOffer getFlightOffer(@PathParam("id") UUID flightOfferId) throws NotFoundException {
        Optional<FlightOffer> flightOffer = flightOfferService.getFlightOffer(flightOfferId);
        if(flightOffer.isPresent()){
            return flightOffer.get();
        }
        throw new NotFoundException("FlightOffer not found!");
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Timed
    public Collection<FlightOffer> findFlightOfferByJourneyStart(@QueryParam("flight-origin") String flightOriginId, @QueryParam("date") String date){
        return flightOfferService.findFlightOfferByJourneyStart(flightOriginId, date);
    }

    @GET
    @Path("/nearest")
    @Produces(MediaType.APPLICATION_JSON)
    @Timed
    public Collection<FlightOffer> findNearestFlightOfferToJourneyEnd(@QueryParam("flight-destination") String flightDestinationId, @QueryParam("travel-origin") String travelOriginId, @QueryParam("date") String date) throws NotFoundException {
        return flightOfferService.findNearestFlightOfferToJourneyEnd(flightDestinationId, travelOriginId, date);
    }
}

package com.ciaran.upskill.travelagency.resource;

import com.ciaran.upskill.travelagency.domain.FlightOffer;
import com.ciaran.upskill.travelagency.representation.CreateFlightOfferRequest;
import com.ciaran.upskill.travelagency.representation.UpdateFlightOfferRequest;
import com.ciaran.upskill.travelagency.service.FlightOfferService;
import com.ciaran.upskill.travelagency.storage.CitiesRepository;
import com.ciaran.upskill.travelagency.storage.FlightOffersRepository;
import com.codahale.metrics.annotation.Timed;

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

    @POST
    @Path("/{id}/cancel")
    @Timed
    public Response cancelFlightOffer(@PathParam("id") String flightOfferId){
        if (flightOfferService.cancelFlightOffer(flightOfferId)){
            return Response.ok().build();
        }
        return Response.noContent().build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Timed
    public FlightOffer getFlightOffer(@PathParam("id") String flightOfferId){
        return flightOfferService.getFlightOffer(flightOfferId);
    }

    @GET
    @Path("/find/{outCity}")
    @Produces(MediaType.APPLICATION_JSON)
    @Timed
    public Collection<FlightOffer> findFlightOfferByJourneyStart(@PathParam("outCity") String outBoundCityId, @QueryParam("date") String date){
        return flightOfferService.findFlightOfferByJourneyStart(outBoundCityId, date);
    }

    @GET
    @Path("/find/nearest/{inCity}")
    @Produces(MediaType.APPLICATION_JSON)
    @Timed
    public FlightOffer findNearestFlightOfferToJourneyEnd(@PathParam("inCity") String inBoundCityId, @QueryParam("origin") String journeyStartCityId, @QueryParam("date") String date){
        return flightOfferService.findNearestFlightOfferToJourneyEnd(inBoundCityId, journeyStartCityId, date);
    }
}

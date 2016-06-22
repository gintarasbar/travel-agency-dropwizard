package com.ciaran.upskill.travelagency.resource;

import com.ciaran.upskill.travelagency.domain.AgencyDate;
import com.ciaran.upskill.travelagency.domain.FlightOffer;
import com.ciaran.upskill.travelagency.representation.CreateFlightOfferRequest;
import com.ciaran.upskill.travelagency.representation.UpdateFlightOfferRequest;
import com.ciaran.upskill.travelagency.storage.CitiesRepository;
import com.ciaran.upskill.travelagency.storage.FlightOffersRepository;
import com.codahale.metrics.annotation.Timed;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.UUID;

@Path("/flight-offers")
public class TravelAgencyResource {
    private FlightOffersRepository flightOffersRepository;
    private CitiesRepository citiesRepository;


    public TravelAgencyResource(FlightOffersRepository flightOffersRepository, CitiesRepository citiesRepository) {
        this.flightOffersRepository=flightOffersRepository;
        this.citiesRepository=citiesRepository;
    }

    @POST
    @Path("/")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Timed
    public FlightOffer createFlightOffer(CreateFlightOfferRequest createFlightOfferRequest){
        String[] requestDates = createFlightOfferRequest.getFlightDates();
        AgencyDate[] flightDates = new AgencyDate[requestDates.length];
        for(int i = 0; i<requestDates.length; i++){
            flightDates[i] = new AgencyDate(requestDates[i]);
        }
        FlightOffer flightOffer = new FlightOffer(createFlightOfferRequest.getPrice(), createFlightOfferRequest.getFlightOrigin(), createFlightOfferRequest.getFlightDestination(), createFlightOfferRequest.getAirline(), flightDates);
        flightOffersRepository.add(flightOffer);
        flightOffersRepository.save();
        return flightOffer;
    }

    @POST
    @Path("/{id}/update")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Timed
    public FlightOffer updateFlightOffer(@PathParam("id") String flightOfferId, UpdateFlightOfferRequest updateFlightOfferRequest){
        boolean updates = false;
        FlightOffer flightOffer = flightOffersRepository.getFLightOfferById(UUID.fromString(flightOfferId));
        if(flightOffer==null){
            return flightOffer;
        }
        if(updateFlightOfferRequest.getPrice()>0){
            flightOffer.setPrice(updateFlightOfferRequest.getPrice());
            updates = true;
        }
        if(updateFlightOfferRequest.getFlightDates()!=null){
            String[] dates = updateFlightOfferRequest.getFlightDates();
            AgencyDate[] agencyDates = new AgencyDate[dates.length];
            for(int i = 0; i<dates.length; i++){
                agencyDates[i] = new AgencyDate(dates[i]);
            }
            flightOffer.setFlightDates(agencyDates);
            updates = true;
        }
        if(updates){
            flightOffersRepository.save();
        }
        return flightOffer;
    }
}

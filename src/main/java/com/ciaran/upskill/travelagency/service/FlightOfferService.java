package com.ciaran.upskill.travelagency.service;

import com.ciaran.upskill.travelagency.domain.AgencyDate;
import com.ciaran.upskill.travelagency.domain.FlightOffer;
import com.ciaran.upskill.travelagency.representation.CreateFlightOfferRequest;
import com.ciaran.upskill.travelagency.representation.UpdateFlightOfferRequest;
import com.ciaran.upskill.travelagency.storage.CitiesRepository;
import com.ciaran.upskill.travelagency.storage.FlightOffersRepository;

import java.util.UUID;

public class FlightOfferService {

    private FlightOffersRepository flightOffersRepository;
    private CityService cityService;

    public FlightOfferService(FlightOffersRepository flightOffersRepository, CityService cityService) {
        this.flightOffersRepository = flightOffersRepository;
        this.cityService = cityService;
    }

    public FlightOffer createFlightOffer(CreateFlightOfferRequest createFlightOfferRequest) {
        if(cityService.getCityById(createFlightOfferRequest.getFlightOrigin())==null ||
                cityService.getCityById(createFlightOfferRequest.getFlightDestination())==null ){
            return null;
        }
        String[] requestDates = createFlightOfferRequest.getFlightDates();
        AgencyDate[] flightDates = new AgencyDate[requestDates.length];
        for(int i = 0; i<requestDates.length; i++){
            flightDates[i] = new AgencyDate(requestDates[i]);
        }
        FlightOffer flightOffer = new FlightOffer(UUID.randomUUID(), createFlightOfferRequest.getPrice(), createFlightOfferRequest.getFlightOrigin(), createFlightOfferRequest.getFlightDestination(), createFlightOfferRequest.getAirline(), flightDates);
        flightOffersRepository.add(flightOffer);
        flightOffersRepository.save();
        return flightOffer;
    }


    public FlightOffer updateFlightOffer(String flightOfferId, UpdateFlightOfferRequest updateFlightOfferRequest) {
        boolean updates = false;
        FlightOffer flightOffer = flightOffersRepository.getFLightOfferById(UUID.fromString(flightOfferId));
        if(flightOffer==null){
            return flightOffer;
        }
        double price = updateFlightOfferRequest.getPrice();
        if(price >0 && price != flightOffer.getPrice()){
            flightOffer.setPrice(price);
            updates = true;
        }
        String[] flightDates = updateFlightOfferRequest.getFlightDates();
        if(flightDates !=null){
            AgencyDate[] agencyDates = new AgencyDate[flightDates.length];
            for(int i = 0; i<flightDates.length; i++){
                agencyDates[i] = new AgencyDate(flightDates[i]);
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

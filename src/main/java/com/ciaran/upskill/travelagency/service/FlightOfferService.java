package com.ciaran.upskill.travelagency.service;

import com.ciaran.upskill.travelagency.domain.AgencyDate;
import com.ciaran.upskill.travelagency.domain.FlightOffer;
import com.ciaran.upskill.travelagency.representation.CreateFlightOfferRequest;
import com.ciaran.upskill.travelagency.representation.UpdateFlightOfferRequest;
import com.ciaran.upskill.travelagency.storage.CitiesRepository;
import com.ciaran.upskill.travelagency.storage.FlightOffersRepository;
import org.joda.time.DateTime;

import java.util.Collection;
import java.util.HashSet;
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
        DateTime[] flightDates = new DateTime[requestDates.length];
        for(int i = 0; i<requestDates.length; i++){
            flightDates[i] = new DateTime(requestDates[i]);
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
            return null;
        }
        double price = updateFlightOfferRequest.getPrice();
        if(price >0 && price != flightOffer.getPrice()){
            flightOffer.setPrice(price);
            updates = true;
        }
        String[] flightDates = updateFlightOfferRequest.getFlightDates();
        if(flightDates !=null){
            DateTime[] agencyDates = new DateTime[flightDates.length];
            for(int i = 0; i<flightDates.length; i++){
                agencyDates[i] = new DateTime(flightDates[i]);
            }
            flightOffer.setFlightDates(agencyDates);
            updates = true;
        }
        if(updates){
            flightOffersRepository.save();
        }
        return flightOffer;
    }

    public boolean cancelFlightOffer(String flightOfferId) {
        FlightOffer flightOffer = flightOffersRepository.getFLightOfferById(UUID.fromString(flightOfferId));
        if(flightOffer==null){
            return false;
        }
        if(flightOffersRepository.remove(flightOffer)){
            flightOffersRepository.save();
        }
        return true;
    }

    public FlightOffer getFlightOffer(String flightOfferId) {
        return flightOffersRepository.getFLightOfferById(UUID.fromString(flightOfferId));
    }

    public Collection<FlightOffer> findFlightOfferByJourneyStart(String outBoundCityId, String date) {
        Collection<FlightOffer> originFlightOfferCollection = flightOffersRepository.getFlightOfferByFlightOrigin(outBoundCityId);
        Collection<FlightOffer> responseFlightOfferCollection = new HashSet<>();
        DateTime requestDate = new DateTime(date);
        for (FlightOffer flightOffer: originFlightOfferCollection){
            boolean matchingDay = false;
            for(DateTime dateTime : flightOffer.getFlightDates()){
                if(requestDate.withTimeAtStartOfDay().isEqual(dateTime.withTimeAtStartOfDay())){
                    matchingDay = true;
                }

            }
            if(matchingDay){
                responseFlightOfferCollection.add(flightOffer);
            }
        }
        return responseFlightOfferCollection;
    }

    public FlightOffer findNearestFlightOfferToJourneyEnd(String inBoundCityId, String journeyStartCityId, String date) {
        return null;
    }
}

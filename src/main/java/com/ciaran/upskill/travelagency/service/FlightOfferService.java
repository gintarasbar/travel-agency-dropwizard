package com.ciaran.upskill.travelagency.service;

import com.ciaran.upskill.travelagency.domain.FlightOffer;
import com.ciaran.upskill.travelagency.representation.CreateFlightOfferRequest;
import com.ciaran.upskill.travelagency.representation.UpdateFlightOfferRequest;
import com.ciaran.upskill.travelagency.storage.FlightOffersRepository;
import javassist.NotFoundException;
import org.joda.time.DateTime;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;

public class FlightOfferService {

    private FlightOffersRepository flightOffersRepository;
    private CityService cityService;

    public FlightOfferService(FlightOffersRepository flightOffersRepository, CityService cityService) {
        this.flightOffersRepository = flightOffersRepository;
        this.cityService = cityService;
    }

    public FlightOffer createFlightOffer(CreateFlightOfferRequest createFlightOfferRequest) throws NotFoundException {
        double distance = cityService.getDistance(createFlightOfferRequest.getFlightOrigin(), createFlightOfferRequest.getFlightDestination());
        String[] requestDates = createFlightOfferRequest.getFlightDates();
        DateTime[] flightDates = new DateTime[requestDates.length];
        for (int i = 0; i < requestDates.length; i++) {
            flightDates[i] = new DateTime(requestDates[i]);
        }
        FlightOffer flightOffer = new FlightOfferBuilder()
                .withId(UUID.randomUUID())
                .withPrice(createFlightOfferRequest.getPrice())
                .withFlightOriginId(createFlightOfferRequest.getFlightOrigin())
                .withFlightDestinationId(createFlightOfferRequest.getFlightDestination())
                .withAirline(createFlightOfferRequest.getAirline())
                .withFlightDates(flightDates)
                .build();
        flightOffersRepository.add(flightOffer);
        flightOffersRepository.save();
        return flightOffer;
    }


    public FlightOffer updateFlightOffer(UUID flightOfferId, UpdateFlightOfferRequest updateFlightOfferRequest) throws NotFoundException {
        boolean updates = false;
        Optional<FlightOffer> flightOffer = flightOffersRepository.getFLightOfferById(flightOfferId);
        if (flightOffer.isPresent()) {
            double price = updateFlightOfferRequest.getPrice();
            if (price > 0 && price != flightOffer.get().getPrice()) {
                flightOffer.get().setPrice(price);
                updates = true;
            }
            String[] flightDates = updateFlightOfferRequest.getFlightDates();
            if (flightDates != null) {
                DateTime[] agencyDates = new DateTime[flightDates.length];
                for (int i = 0; i < flightDates.length; i++) {
                    agencyDates[i] = new DateTime(flightDates[i]);
                }
                flightOffer.get().setFlightDates(agencyDates);
                updates = true;
            }
            if (updates) {
                flightOffersRepository.save();
            }
            return flightOffer.get();
        }
        throw new NotFoundException("FlightOffer not found!");
    }

    public boolean cancelFlightOffer(UUID flightOfferId) {
        Optional<FlightOffer> flightOffer = flightOffersRepository.getFLightOfferById(flightOfferId);
        if (flightOffer.isPresent()) {
            if (flightOffersRepository.remove(flightOffer.get())) {
                flightOffersRepository.save();
                return true;
            }
        }
        return false;
    }

    public Optional<FlightOffer> getFlightOffer(UUID flightOfferId) {
        return flightOffersRepository.getFLightOfferById(flightOfferId);
    }

    public Collection<FlightOffer> findFlightOfferByJourneyStart(String flightOrigin, String date) {
        Collection<FlightOffer> originFlightOfferCollection = flightOffersRepository.getFlightOfferByFlightOrigin(flightOrigin);
        Collection<FlightOffer> responseFlightOfferCollection = new HashSet<>();
        DateTime requestDate = new DateTime(date);
        for (FlightOffer flightOffer : originFlightOfferCollection) {
            boolean matchingDay = false;
            for (DateTime dateTime : flightOffer.getFlightDates()) {
                if (requestDate.withTimeAtStartOfDay().isEqual(dateTime.withTimeAtStartOfDay())) {
                    matchingDay = true;
                }

            }
            if (matchingDay) {
                responseFlightOfferCollection.add(flightOffer);
            }
        }
        return responseFlightOfferCollection;
    }

    public Collection<FlightOffer> findNearestFlightOfferToJourneyEnd(String flightDestination, String travelOrigin, String dateString) throws NotFoundException {
        Collection<FlightOffer> flightOffersGoingToDestination = flightOffersRepository.getFlightOfferByFlightDestination(flightDestination);
        DateTime requestDate = new DateTime(dateString);
        String closestCityId = null;
        double closestDistance = 0;
        for (FlightOffer flightOffer : flightOffersGoingToDestination) {
            if (closestCityId == null) {
                closestCityId = flightOffer.getFlightOriginId();
                closestDistance = cityService.getDistance(travelOrigin, closestCityId);
            } else {
                double distance = cityService.getDistance(travelOrigin, flightOffer.getFlightOriginId());
                if (distance < closestDistance) {
                    closestDistance = distance;
                    closestCityId = flightOffer.getFlightOriginId();
                }
            }
        }
        Collection<FlightOffer> flightOffersFromNearestCity = new HashSet<>();
        DateTime nearestDate = null;
        int nearestDateCompare = -1;
        for (FlightOffer flightOffer : flightOffersGoingToDestination) {
            if (flightOffer.getFlightOriginId().matches(closestCityId)) {
                flightOffersFromNearestCity.add(flightOffer);
                for (DateTime flightDate : flightOffer.getFlightDates()) {
                    if (nearestDate == null) {
                        nearestDateCompare = flightDate.withTimeAtStartOfDay().compareTo(requestDate.withTimeAtStartOfDay());
                        if (nearestDateCompare >= 0) {
                            nearestDate = flightDate;
                        }
                    } else {
                        int flightDateCompare = flightDate.withTimeAtStartOfDay().compareTo(requestDate.withTimeAtStartOfDay());
                        nearestDateCompare = nearestDate.withTimeAtStartOfDay().compareTo(flightDate.withTimeAtStartOfDay());
                        if (nearestDateCompare > 0 && flightDateCompare >= 0) {
                            nearestDate = flightDate;
                        }
                    }
                }
            }
        }
        Collection<FlightOffer> resultingFlightOffers = new HashSet<>();
        for (FlightOffer flightOffer : flightOffersFromNearestCity) {
            for (DateTime dateTime : flightOffer.getFlightDates()) {
                if (dateTime.withTimeAtStartOfDay().isEqual(nearestDate.withTimeAtStartOfDay())) {
                    resultingFlightOffers.add(flightOffer);
                }
            }
        }
        return resultingFlightOffers;
    }
}

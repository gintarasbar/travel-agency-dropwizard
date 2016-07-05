package com.ciaran.upskill.travelagency.service;

import com.ciaran.upskill.travelagency.domain.FlightOffer;
import com.ciaran.upskill.travelagency.representation.CreateFlightOfferRequest;
import com.ciaran.upskill.travelagency.representation.UpdateFlightOfferRequest;
import com.ciaran.upskill.travelagency.storage.FlightOffersRepository;
import javassist.NotFoundException;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.ciaran.upskill.travelagency.service.FlightOfferBuilder.aFlightOffer;

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
        FlightOffer flightOffer = aFlightOffer()
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
        double closestDistance = 0;
        ArrayList<String> flightOriginIds = new ArrayList<>();

        Collection<FlightOffer> flightOffersMatchingDate = new HashSet<>();
        for (FlightOffer flightOffer : flightOffersGoingToDestination) {
            for (DateTime dateTime : flightOffer.getFlightDates()) {
                if (dateTime.withTimeAtStartOfDay().isEqual(requestDate.withTimeAtStartOfDay())) {
                    flightOffersMatchingDate.add(flightOffer);
                }
            }
        }

        for (FlightOffer flightOffer : flightOffersMatchingDate) {
            flightOriginIds.add(flightOffer.getFlightOriginId());
        }

        String closestCityId = cityService.chooseNearesCity(cityService.getCityById(travelOrigin), flightOriginIds);

        return flightOffersMatchingDate.stream()
                .filter(flightOffer -> flightOffer.getFlightOriginId().matches(closestCityId)).collect(Collectors.toList());
    }
}

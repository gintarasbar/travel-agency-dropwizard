package com.ciaran.upskill.travelagency.storage;

import com.ciaran.upskill.travelagency.domain.FlightOffer;
import com.ciaran.upskill.travelagency.service.FlightOfferBuilder;
import org.joda.time.DateTime;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;

public class FlightOffersRepository {
    private Collection<FlightOffer> flightOfferCollection;
    private String csvResourcePath;

    public FlightOffersRepository(String csvResourcePath){
        flightOfferCollection = new HashSet<FlightOffer>();
        this.csvResourcePath = csvResourcePath;
    }



    public void save() {
        BufferedWriter bufferedWriter = null;
        try {
            bufferedWriter = new BufferedWriter(new FileWriter(csvResourcePath));
            String headerLine = "Id,Price,FlightOriginId,FlightDestinationId,Airline,Dates";
            bufferedWriter.append(headerLine);
            bufferedWriter.newLine();
            for(FlightOffer flightOffer: flightOfferCollection){
                bufferedWriter.append(flightOffer.toCSVRow());
                bufferedWriter.append("\n");
            }


        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bufferedWriter != null){
                try {
                    bufferedWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void load(){
        BufferedReader bufferedReader = null;
        String line = "";
        String csvSplitBy = ",";

        try {
            bufferedReader = new BufferedReader(new FileReader(csvResourcePath));
            bufferedReader.readLine();
            FlightOfferBuilder flightOfferBuilder = new FlightOfferBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                String[] csvLine = line.split(csvSplitBy);
                csvLine[5] = csvLine[5].substring(1,csvLine[5].length()-1);
                String[] datesLine = csvLine[5].split(";");
                DateTime[] datesArray = new DateTime[datesLine.length];
                for (int i = 0; i<datesArray.length; i++){
                    datesArray[i] = new DateTime(datesLine[i]);
                }
                FlightOffer flightOffer = flightOfferBuilder.withId(UUID.fromString(csvLine[0]))
                        .withPrice(Double.parseDouble(csvLine[1]))
                        .withFlightOriginId(csvLine[2])
                        .withFlightDestinationId(csvLine[3])
                        .withAirline(csvLine[4])
                        .withFlightDates(datesArray)
                        .build();
                flightOfferCollection.add(flightOffer);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public boolean isEmpty() {
        return flightOfferCollection.isEmpty();
    }

    public boolean add(FlightOffer flightOffer) {
        return flightOfferCollection.add(flightOffer);
    }

    public Optional<FlightOffer> getFLightOfferById(UUID id) {
        return flightOfferCollection.stream()
                .filter(flightOffer -> flightOffer.getId().equals(id))
                .findFirst();
    }

    public void updateDates(UUID id, DateTime[] dates){
        getFLightOfferById(id).ifPresent(flight->flight.setFlightDates(dates));
    }

    public void updatePrice(UUID id, double newPrice) {
        getFLightOfferById(id).ifPresent(flightOffer -> flightOffer.setPrice(newPrice));
    }

    public boolean remove(FlightOffer flightOffer) {
        return flightOfferCollection.remove(flightOffer);
    }

    public Collection<FlightOffer> getFlightOfferByFlightOrigin(String outBoundCityId) {
        Collection<FlightOffer> newFlightOfferCollection = new HashSet<FlightOffer>();
        for(FlightOffer flightOffer : flightOfferCollection){
            if(flightOffer.getFlightOriginId().matches(outBoundCityId)){
                newFlightOfferCollection.add(flightOffer);
            }
        }
        return newFlightOfferCollection;
    }

    public Collection<FlightOffer> getFlightOfferByFlightDestination(String inBoundCityId) {
        Collection<FlightOffer> newFlightOfferCollection = new HashSet<FlightOffer>();
        for(FlightOffer flightOffer : flightOfferCollection){
            if(flightOffer.getFlightDestinationId().matches(inBoundCityId)){
                newFlightOfferCollection.add(flightOffer);
            }
        }
        return newFlightOfferCollection;
    }
}

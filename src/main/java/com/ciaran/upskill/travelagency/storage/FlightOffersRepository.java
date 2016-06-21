package com.ciaran.upskill.travelagency.storage;

import com.ciaran.upskill.travelagency.service.FlightOfferFactory;
import com.ciaran.upskill.travelagency.domain.FlightOffer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.UUID;

public class FlightOffersRepository {
    private Collection<FlightOffer> flightOfferCollection;
    private static final String csvFile = "/Users/ciaran.potter/projects/personal/travel-agency-dropwizard/src/main/resources/flightoffers.csv";

    public FlightOffersRepository(){
        flightOfferCollection = new HashSet<FlightOffer>();
    }

    public void save(){
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(csvFile));
            String headerLine = "Id,Price,FlightOriginId,FlightDestinationId,Airline,Dates";
            bufferedWriter.write(headerLine+"\n");
            for(FlightOffer flightOffer: flightOfferCollection){
                bufferedWriter.append(flightOffer.toString());
                bufferedWriter.append("\n");
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void load(){
        BufferedReader bufferedReader = null;
        String line = "";
        String csvSplitBy = ",";

        try {
            bufferedReader = new BufferedReader(new FileReader(csvFile));
            while ((line = bufferedReader.readLine()) != null) {
                String[] csvLine = line.split(csvSplitBy);
                csvLine[5] = csvLine[5].substring(1,csvLine[5].length()-1);
                String[] datesLine = csvLine[5].split(";");
                Date[] datesArray = new Date[datesLine.length];
                for (int i = 0; i<datesArray.length; i++){
                    datesArray[i] = new Date(datesLine[i]);
                }
                FlightOffer flightOffer = FlightOfferFactory.create(UUID.fromString(csvLine[0]),Double.parseDouble(csvLine[1]),csvLine[2],csvLine[3],csvLine[4], datesArray);
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
}

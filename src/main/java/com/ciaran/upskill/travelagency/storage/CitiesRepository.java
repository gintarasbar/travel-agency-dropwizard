package com.ciaran.upskill.travelagency.storage;

import com.ciaran.upskill.travelagency.domain.City;
import com.ciaran.upskill.travelagency.domain.CoOrdinate;
import com.ciaran.upskill.travelagency.service.CityBuilder;
import javassist.NotFoundException;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import static com.ciaran.upskill.travelagency.service.CityBuilder.aCity;

public class CitiesRepository {

    private Collection<City> citiesCollection;
    private String csvResourcePath;

    public CitiesRepository(String csvResourcePath){
        citiesCollection = new HashSet<City>();
        this.csvResourcePath = csvResourcePath;
    }

    public boolean isEmpty() {
        return citiesCollection.isEmpty();
    }

    public City getCityById(String id) throws NotFoundException {
        for(City city : citiesCollection){
            if(id.matches(city.getId())){
                return city;
            }
        }
        throw new NotFoundException("City not found");
    }

    public void load(){
        BufferedReader br = null;
        String line = "";
        String csvSplitBy = ",";

        try {
            br = new BufferedReader(new FileReader(csvResourcePath));
            //to ignore headers
            br.readLine();
            CityBuilder cityBuilder = aCity();

            while ((line = br.readLine()) != null) {
                String[] csvLine = line.split(csvSplitBy);
                CoOrdinate location = new CoOrdinate(Double.parseDouble(csvLine[5]), Double.parseDouble(csvLine[6]));
                City city = cityBuilder
                        .withCountryCode(csvLine[0].toUpperCase())
                        .withName(csvLine[1])
                        .withPrintName(csvLine[2])
                        .withRegion(csvLine[3])
                        .withPopulation(Integer.parseInt(csvLine[4]))
                        .withLocation(location)
                        .build();
                new City(csvLine[0].toUpperCase(), csvLine[1], csvLine[2], csvLine[3], Integer.parseInt(csvLine[4]), location);
                citiesCollection.add(city);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}


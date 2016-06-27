package com.ciaran.upskill.travelagency.service;

import com.ciaran.upskill.travelagency.domain.City;
import com.ciaran.upskill.travelagency.domain.CoOrdinate;

public final class CityBuilder {
    private String name;
    private String printName;
    private String countryCode;
    private CoOrdinate location;
    private String region;
    private int population;

    private CityBuilder() {
    }

    public static CityBuilder aCity() {
        return new CityBuilder();
    }

    public CityBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public CityBuilder withPrintName(String printName) {
        this.printName = printName;
        return this;
    }

    public CityBuilder withCountryCode(String countryCode) {
        this.countryCode = countryCode;
        return this;
    }

    public CityBuilder withLocation(CoOrdinate location) {
        this.location = location;
        return this;
    }

    public CityBuilder withRegion(String region) {
        this.region = region;
        return this;
    }

    public CityBuilder withPopulation(int population) {
        this.population = population;
        return this;
    }

    public City build() {
        City city = new City(countryCode, name, printName, region, population, location);
        return city;
    }
}

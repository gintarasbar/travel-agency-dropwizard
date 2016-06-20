package com.ciaran.upskill.travelagency.domain;

public class City {
    private String id;
    private String name;
    private String countryCode;
    private CoOrdindate location;
    private int region;
    private int population;

    public City(String name, String countryCode, CoOrdindate location) {
        this.name = name;
        this.countryCode = countryCode;

        this.id = name+ countryCode;
        this.location = location;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public CoOrdindate getLocation() {
        return location;
    }
}

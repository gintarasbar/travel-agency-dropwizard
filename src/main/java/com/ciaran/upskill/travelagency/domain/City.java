package com.ciaran.upskill.travelagency.domain;

public class City {
    private String id;
    private String name;

    private String printName;

    private String countryCode;
    private CoOrdinate location;
    private String region;
    private int population;

    public City(String countryCode, String name, String printName, String region, int population, CoOrdinate location) {
        this.printName = printName;
        this.name = name;
        this.population = population;
        this.region = region;
        this.countryCode = countryCode;
        this.location = location;
        this.id = name.replace(' ','-')+ countryCode.toUpperCase();
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

    public CoOrdinate getLocation() {
        return location;
    }

    public String getPrintName() {
        return printName;
    }

    public String getRegion() {
        return region;
    }

    public int getPopulation() {
        return population;
    }

    public double getDistance(City city){
        return this.getLocation().getDistance(city.getLocation());
    }

    @Override
    public String toString() {
        return  countryCode+","+name+","+printName+","+region+","+population+","+location.toString();
    }
}

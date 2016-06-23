package com.ciaran.upskill.travelagency.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AgencyDate {

    @JsonIgnore
    Date dateObject;

    @JsonProperty
    String date;

    @JsonIgnore
    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);


    public AgencyDate(String date){
        this.date=date;
        try {
            dateObject = dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return dateFormat.format(dateObject);
    }
}

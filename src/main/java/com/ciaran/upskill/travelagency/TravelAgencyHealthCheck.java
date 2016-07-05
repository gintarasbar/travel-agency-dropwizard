package com.ciaran.upskill.travelagency;

import com.codahale.metrics.health.HealthCheck;

public class TravelAgencyHealthCheck extends HealthCheck{

    public TravelAgencyHealthCheck() {
    }

    @Override
    protected Result check() throws Exception {
        return Result.healthy();
    }
}

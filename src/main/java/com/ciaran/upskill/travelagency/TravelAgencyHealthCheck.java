package com.ciaran.upskill.travelagency;

import com.ciaran.upskill.travelagency.config.TravelAgencyConfig;
import com.codahale.metrics.health.HealthCheck;

public class TravelAgencyHealthCheck extends HealthCheck{
    private final String template;

    public TravelAgencyHealthCheck(String template) {
        this.template = template;
    }
    //TODO make better healthcheck
    @Override
    protected Result check() throws Exception {
        return Result.healthy();
    }
}

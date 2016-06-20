package com.ciaran.upskill.travelagency;

import com.ciaran.upskill.travelagency.config.TravelAgencyConfig;
import com.codahale.metrics.health.HealthCheck;

public class TravelAgencyHealthCheck extends HealthCheck{
    private final String template;

    public TravelAgencyHealthCheck(String template) {
        this.template = template;
    }
    //TODO find better healthcheck
    @Override
    protected Result check() throws Exception {
        final String saying = String.format(template, "TEST");
        if (!saying.contains("TEST")) {
            return Result.unhealthy("template doesn't include a name");
        }
        return Result.healthy();
    }
}
